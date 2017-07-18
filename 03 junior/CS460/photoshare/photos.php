<?php
include_once ('include/header.inc.php');

// Check for albumid...
if (!isset($_GET['albumid']))
{
	echo '<h2>Parameter "albumid" not found!</h2>';
	exit;
}

// If postback and user requested deletion...
if (isset($_POST['delete']) && isset($_GET['deleteId']) && is_numeric($_GET['deleteId'])) {
	$dbConn = db_connect();
	$query = sprintf("DELETE FROM `Photos` WHERE `photo_id` = '%s' LIMIT 1", mysql_real_escape_string($_GET['deleteId']));
	//echo "<pre>$query</pre>";
	$result = db_query($query, $dbConn);
	if (mysql_affected_rows($dbConn) == 1) { ?>
		<h2>Photo ID#<?=$_GET['deleteId']?> successfully deleted!</h2>
	<?
	} else {
		echo '<h2>Photo deletion error!</h2>';
	}
}

// If postback, try to upload photo.
if (isset($_GET['albumid']) && isset($_FILES['imagefile'])) {
	$content_type = mysql_real_escape_string($_FILES['imagefile']['type']);
	if (!strstr($content_type, "jpg") && !strstr($content_type, "jpeg") && !strstr($content_type, "gif") && !strstr($content_type, "png")) {
		echo "<h2>Image must be of type jpg, gif, or png!</h2>";
	} else {
		$album_id = mysql_real_escape_string($_GET['albumid']);
		$caption = mysql_real_escape_string($_POST['caption']);
		$data = mysql_real_escape_string(file_get_contents($_FILES['imagefile']['tmp_name']));
		$filesize = mysql_real_escape_string($_FILES['imagefile']['size']);
		$thumbnail = mysql_real_escape_string(createThumbnail(file_get_contents($_FILES['imagefile']['tmp_name']), $content_type, 200, 200));
		
		$dbConn = db_connect();
		$query = "INSERT INTO `Photos` (`album_id`, `caption`, `data`, `thumbnail`, `filesize`, `content_type`) ".
						"VALUES ('$album_id', '$caption', '$data', '$thumbnail', '$filesize', '$content_type')";
		//echo "<pre>$query</pre>";
		$result = db_query($query, $dbConn);
		if (mysql_affected_rows($dbConn) == 1) {
			echo '<h2>Photo successfully uploaded!</h2>';
			$_POST['caption'] = null;
		} else {
			echo '<h2>Photo upload error!</h2>';
		}
	}
}

if (isLoggedIn()) { // Show single photo upload form if user is logged in and album belongs to user.
	$dbConn = db_connect();
	$query = sprintf("SELECT owner_id FROM Albums A WHERE A.album_id='%s' AND A.owner_id='%s'", mysql_real_escape_string($_GET['albumid']), mysql_real_escape_string($_SESSION['user_id']));
	$result = db_query($query, $dbConn);
	if (mysql_num_rows($result) != 0) {
?>

<br />

<fieldset>
<legend>Add Photo to Album:</legend>
<form name="formUploadPhoto" action="<?=$_SERVER['REQUEST_URI']?>" enctype="multipart/form-data" method="post">
<table>
	<tr>
		<td>
			Filename:
		</td>
		<td>
			<input type="file" name="imagefile"/>
		</td>
	</tr>
	<tr>
		<td>
			Caption:
		</td>
		<td>
			<input type="text" name="caption" maxlength="100" />
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td colspan="2" align="center">
			<input type="submit" name="submit" value="Upload"/>
		</td>
	</tr>
</table>
<!-- XAMPP's MySQL default max_packet_size is 1MB -->
<input type="hidden" name="MAX_FILE_SIZE" value="1000000" />
</form>
</fieldset>

<?
	}
}
// display album info and photos
?>
<br />

<form name="formDeletePhoto" action="<?=$_SERVER['PHP_SELF']?>?albumid=<?=$_GET['albumid']?>" method="post">
<fieldset>
	<table align="left" width="100%">
		<thead align="left">
			<tr>
				<th width="225px">
					Photo
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
		$dbConn = db_connect();
		$query = "SELECT U.user_id, P.photo_id, P.caption, P.album_id FROM Albums A, Users U, Photos P WHERE P.album_id = '$_GET[albumid]' AND A.album_id = P.album_id AND A.owner_id = U.user_id";
		$result = db_query($query, $dbConn);
		if (mysql_num_rows($result) == 0) {
			echo '<tr><td colspan="3">There are no photos in this album.</td></tr>';
		}
		while ($row = mysql_fetch_array($result)) {
		?>
		<tr>
			<td>
				<a href="viewPhoto.php?id=<?=$row['photo_id']?>&albumid=<?=$_GET['albumid']?>"><img src="photo.php?id=<?=$row['photo_id']?>&thumb=true" /></a>
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
	</table>
</fieldset>
</form>

<?
include_once ('include/footer.inc.php');
?>