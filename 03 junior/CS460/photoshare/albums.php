<?php
include_once ('include/header.inc.php');

if (isset($_POST['delete']) && isset($_GET['deleteId']) && $_GET['deleteId'] != "") {
	$dbConn = db_connect();
	$query = sprintf("DELETE FROM `Albums` WHERE `album_id` = '%s' LIMIT 1", mysql_real_escape_string($_GET['deleteId']));
	//echo "<pre>$query</pre>";
	$result = db_query($query, $dbConn);
	if (mysql_affected_rows($dbConn) == 1) { ?>
		<h2>Album ID#<?=$_GET['deleteId']?> successfully deleted!</h2>
	<?
	} else {
		echo '<h2>Album deletion error!</h2>';
	}
}

// If postback, try to create new album.
if (isset($_POST['name'])) {
	$dbConn = db_connect();
	$query = "INSERT INTO `Albums` (`owner_id`, `name`) VALUES ('".mysql_real_escape_string($_SESSION['user_id']).
					"', '".mysql_real_escape_string($_POST['name'])."')";
	//echo "<pre>$query</pre>";
	$result = db_query($query, $dbConn);
	if (mysql_affected_rows($dbConn) == 1) { ?>
		<h2>Album '<a href="photos.php?albumid=<?=mysql_insert_id($dbConn)?>"><?=$_POST['name']?></a>' successfully created!</h2>
	<?
	} else {
		echo '<h2>Album creation error!</h2>';
	}
} else if (isLoggedIn()) { // Else show album creation form if user is logged in.
?>

<br />

<fieldset>
<legend>Create a New Album</legend>
<form name="formCreateAlbum" action="<?=$_SERVER['REQUEST_URI']?>" method="post" onsubmit="return validate_album_form(this)">
    Album Title: <input type="text" name="name" value="<?=$_POST['name']?>" />
    <input type="submit" value="Create"/>
</form>
</fieldset>

<?
}
// Display all albums.
?>

<br />

<form name="formDeleteAlbum" action="<?=$_SERVER['PHP_SELF']?>" method="post">
<fieldset>
	<legend>
		<? if (isLoggedIn()) {
				if (isset($_GET['my'])) {
					echo 'My Albums (<a href="'.$_SERVER['PHP_SELF'].'">View All Albums</a>)';
					
				} else {
					echo 'All Albums (<a href="'.$_SERVER['PHP_SELF'].'?my=true">View My Albums</a>)';
				}
		 } else {
		 	echo 'All Albums';
		 } ?>
	</legend>
	<table align="left" width="100%">
		<thead align="left">
			<tr>
				<th>
					Album Name
				</th>
				<th>
					Author
				</th>
				<th>
					Creation Date
				</th>
				<th>
					&nbsp;
				</th>
			</tr>
		</thead>
		<tbody align="left">
		<?
		$dbConn = db_connect();
		$query = "SELECT * FROM Albums, Users WHERE `owner_id` = `user_id`";
		if (isLoggedIn() && isset($_GET['my'])) {
			$query .= " AND `owner_id` = $_SESSION[user_id]";
		}
		$result = db_query($query, $dbConn);
		if (mysql_num_rows($result) == 0) {
			echo '<tr><td colspan="4">There are no albums in the system.</td></tr>';
		}
		while ($row = mysql_fetch_array($result)) {
		?>
		<tr>		
			<td>
				<a href="photos.php?albumid=<?=$row['album_id']?>"><?=$row['name']?></a>
			</td>
			<td>
				<?=$row['email']?>
			</td>
			<td>
				<?=date("F j, Y (g:ia)", strtotime($row['creationDate']))?>
			</td>
			<? if (isLoggedIn() && $_SESSION['email'] == $row['email']) { ?>
			<td>
				<input type="submit" id="delete" name="delete" value="Delete" onclick="if (confirm('Are you sure you wish to delete this album?')) {
																								document.formDeleteAlbum.action += '?deleteId=<?=$row['album_id']?>';
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
	</table>
</fieldset>
</form>

<?
include_once ('include/footer.inc.php');
?>