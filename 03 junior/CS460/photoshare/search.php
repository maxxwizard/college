<?php
include_once ('include/header.inc.php');

// If postback and user requested deletion...
if (isset($_POST['delete']) && isset($_GET['deleteId']) && is_numeric($_GET['deleteId'])) {
	$dbConn = db_connect();
	$queryDelete = sprintf("DELETE FROM `Photos` WHERE `photo_id` = '%s' LIMIT 1", mysql_real_escape_string($_GET['deleteId']));
	//echo "<pre>$query</pre>";
	$resultDelete = db_query($queryDelete, $dbConn);
	if (mysql_affected_rows($dbConn) == 1) { ?>
		<h2>Photo ID#<?=$_GET['deleteId']?> successfully deleted!</h2>
	<?
	} else {
		echo '<h2>Photo deletion error!</h2>';
	}
}

// Display search box.
?>
<br />

<center>
	<fieldset>
	<legend>Search by Tag</legend>
	<h3>Type in tags you wish to search for (e.g. "manhattan skyline").</h3>
	<form action="<?=$_SERVER['PHP_SELF'].'?q='.$_GET['q']?>" name="formSearch" method="get">
		<div>
			<input type="text" name="q" align="middle" size="60" value="<?=$_GET['q']?>" />
			<br />
			<input type="submit" value="Search" />
		</div>
	</form>
	</fieldset>

<? // If search string found, display search results.
if (isset($_GET['q']) && $_GET['q'] != "") { ?>
	<form name="formDeletePhoto" action="<?=$_SERVER['PHP_SELF'].'?q='.$_GET['q'].'&my='.$_GET['my']?>" method="post">
	<fieldset>
	<legend>
		<? if (isLoggedIn()) {
				if (isset($_GET['my']) && $_GET['my'] != "") {
					echo 'Results in My Photos (<a href="'.$_SERVER['PHP_SELF'].'?q='.$_GET['q'].'">Toggle</a>)';
					
				} else {
					echo 'Results in All Photos (<a href="'.$_SERVER['PHP_SELF'].'?q='.$_GET['q'].'&my=true">Toggle</a>)';
				}
		 } else {
		 	echo 'Results in All Photos';
		 } ?>
	</legend>
<? $dbConn = db_connect();
	// Parse search string into multiple tags.
	$tags = explode(" ", $_GET['q']);
	// Grab all photo_ids that match all tags.
	foreach ($tags as $tag) {
		if ($query != "")
			$query = " AND photo_id IN ($query)";
		$query = sprintf("SELECT photo_id FROM Tags WHERE tag_name='%s'", mysql_real_escape_string($tag)) . $query;
	}
	$result = db_query($query, $dbConn);
	// If there are search results, display them.
	if (mysql_num_rows($result) > 0) { ?>
	<table align="left" width="100%">
		<thead align="left">
			<tr>
				<th width="225px">
					Photo
				</th>
				<th>
					Owner
				</th>
				<th>
					Caption
				</th>
				<th>
					&nbsp;
				</th>
			</tr>
		</thead>
		<tbody align="left">
		<?
		while ($photo_id = mysql_fetch_array($result)) {
			// Grab photo and related info.
			$queryInfo = sprintf("SELECT P.photo_id, A.album_id, U.user_id, CONCAT(U.firstName,' ',U.lastName) AS name, P.caption FROM Photos P, Albums A, Users U WHERE P.album_id=A.album_id AND A.owner_id=U.user_id AND P.photo_id=%d", mysql_real_escape_string($photo_id['photo_id']));
			// If user requested only his/her photos.
			if (isset($_GET['my']) && $_GET['my'] != "") {
				$queryInfo .= " AND U.user_id='$_SESSION[user_id]'";
			}
			$row = mysql_fetch_array(db_query($queryInfo, $dbConn));
		?>
		<tr>
			<td>
				<a href="viewPhoto.php?id=<?=$row['photo_id']?>&albumid=<?=$row['album_id']?>"><img src="photo.php?id=<?=$row['photo_id']?>&thumb=true" /></a>
			</td>
			<td>
				<a href="profile.php?id=<?=$row['user_id']?>"><?=$row['name']?></a>
			</td>
			<td>
				<?=$row['caption']?>
			</td>
			<? if (isLoggedIn() && $_SESSION['user_id'] == $row['user_id']) { ?>
			<td>
				<input type="submit" id="delete" name="delete" value="Delete" onclick="if (confirm('Are you sure you wish to delete this photo?')) {
																								document.formDeletePhoto.action += '&deleteId=<?=$row['photo_id']?>';
																								return true;
																								} return false;" />
			</td>
			<? } else { ?>
			<td>
				&nbsp;
			</td>
			<? } ?>
		</tr>
		<?
		}
		?>
		</tbody>
<?	} else {
		echo '<tr><td colspan="4">No photos found with requested tags.</td></tr>';
	}
} ?>
	</table>
	</fieldset>
</center>



<?
include_once ('include/footer.inc.php');
?>