<?php
require_once ('include/global.inc.php');

// Page that outputs the requested photo.

if (is_numeric($_GET['id'])) {
	$dbConn = db_connect();
	if (isset($_GET['thumb'])) {
		$query = sprintf("SELECT thumbnail, content_type FROM `Photos` WHERE `photo_id` = %s", mysql_real_escape_string($_GET['id']));
	} else {
		$query = sprintf("SELECT data, filesize, content_type FROM `Photos` WHERE `photo_id` = %s", mysql_real_escape_string($_GET['id']));
	}
	//echo "<pre>$query</pre>";
	$result = db_query($query, $dbConn);
	if (mysql_num_rows($result) == 1) {
		$row = mysql_fetch_array($result);
		header("Content-type: $row[content_type]");
		if (isset($_GET['thumb'])) {
			echo $row['thumbnail'];
		}
		else {
			if (isset($_GET['width']) && isset($_GET['height'])) { // Show picture according to requested dimensions.
				echo createThumbnail($row['data'], $row['content_type'], $_GET['width'], $_GET['height']);
			} else { // Show picture in database.
				header("Content-length: $row[filesize]");
				echo $row['data'];
			}
		}
	} else {
		echo '<h2>Could not retrieve your photo!</h2>';
	}
} else {
	echo '<h2>No "id" parameter detected in querystring!</h2>';
}

?>