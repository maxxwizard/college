<?php
include_once ('include/header.inc.php');

// If postback, try to post comment.
if (isset($_POST['Comment']) && isset($_POST['comments']) && trim($_POST['comments']) != "") {
	$dbConn = db_connect();
	// Photo owner may not comment on own photo.
	if (isLoggedIn() && isPhotoOwner($_SESSION['user_id'], $_GET['id'])) {
		echo '<h2>You may not comment on your own photo.</h2>';
	} else {
		if (isLoggedIn()) {
			$query = sprintf("INSERT INTO Comments (photo_id, owner_id, data) VALUES ('%d', '%d', '%s')", mysql_real_escape_string($_GET['id']), mysql_real_escape_string($_SESSION['user_id']), mysql_real_escape_string($_POST['comments']));
		} else {
			$query = sprintf("INSERT INTO Comments (photo_id, data) VALUES ('%d', '%s')", mysql_real_escape_string($_GET['id']), mysql_real_escape_string($_POST['comments']));
		}
		$result = db_query($query, $dbConn);
		if (mysql_affected_rows($dbConn) == 1) {
			echo '<h2>Comment posted.</h2>';
		} else {
			echo '<h2>Comment post error!</h2>';
		}
	}
}

// If postback, try to add tag.
if (isset($_POST['Tag']) && isset($_POST['tagTxtbox']) && $_POST['tagTxtbox'] != "") {
	$dbConn = db_connect();
	$query = sprintf("INSERT INTO Tags (photo_id, tag_name) VALUES ('%d', '%s')", mysql_real_escape_string($_GET['id']), mysql_real_escape_string($_POST['tagTxtbox']));
	$result = db_query($query, $dbConn);
	if (mysql_affected_rows($dbConn) == 1) {
		echo '<h2>Tag added.</h2>';
	} else {
		echo '<h2>Tag add error!</h2>';
	}
}

/* Display image with comments and tags if id parameter is given. */
if (is_numeric($_GET['id'])) {
	// Display link back to album.
	if (is_numeric($_GET['albumid'])) {
		echo '<br /><center><a href="photos.php?albumid='.$_GET['albumid'].'">Back to Album</a></center>';
	}
	// Print the thumbnailed linked to full photo.
	echo '<br /><center><div><a href="photo.php?id='.$_GET['id'].'"><img src="photo.php?id='.$_GET['id'].'&thumb=true" /></a></div></center><br />';
	echo '<fieldset><legend>Info</legend><table>';
	// Display caption.
	$dbConn = db_connect();
	$query = sprintf("SELECT caption FROM Photos WHERE photo_id = %d", mysql_real_escape_string($_GET['id']));
	$result = db_query($query, $dbConn);
	$row = mysql_fetch_array($result);
	echo "<tr><td><strong>Caption:</strong></td><td>$row[caption]</td></tr>";
	// Display tags.
	echo '<tr><td><strong>Tags:</strong></td>';
	$query = sprintf("SELECT photo_id, tag_name FROM Tags WHERE photo_id = %d", mysql_real_escape_string($_GET['id']));
	$result = db_query($query, $dbConn);
	if (mysql_num_rows($result) != 0) {
		while ($row = mysql_fetch_array($result)) {
			if ($tags != "") {
				$tags .= ', ';
			}
			$tags .= '<a href="search.php?q='.$row['tag_name'].'">'.$row['tag_name'].'</a>';
		}
		echo "<td>$tags</td></tr>";
	} else {
		echo '<td>There are no tags.</td></tr>';
	}
	// Display tag box if user owns photo.
	if (isLoggedIn() && isPhotoOwner($_SESSION['user_id'], $_GET['id'])) { ?>
		<tr>
			<td>
				&nbsp;
			</td>
			<td>
				<form name="formTagAdd" action="<?=$_SERVER['PHP_SELF'].'?id='.$_GET['id'].'&albumid='.$_GET['albumid']?>" method="post">
					<input type="text" name="tagTxtbox" />
					<input type="submit" name="Tag" value="Add Tag" />
				</form>
			</td>
		</tr>
<?	}
	echo '</table></fieldset>'; // end info fieldset
	// Display comments.
	
	$query = sprintf("SELECT U.user_id, CONCAT(U.firstName, ' ', U.lastName) AS name, C.data AS comment, C.date FROM Comments C LEFT JOIN Users U ON C.owner_id=U.user_id, Photos P WHERE P.photo_id = %d AND P.photo_id=C.photo_id", mysql_real_escape_string($_GET['id']));
	//echo "<pre>$query</pre>";
	$result = db_query($query, $dbConn);
	
	echo '<fieldset><legend>Comments</legend><table>';
	if (mysql_num_rows($result) != 0) {
		while ($row = mysql_fetch_array($result)) {
			if (is_null($row['user_id'])) {
				echo "<tr><td><strong>Anonymous User";
			} else {
				echo "<tr><td><strong><a href=\"profile.php?id=$row[user_id]\">$row[name]</a>";
			}
			echo " (".date("g:ia F j, Y", strtotime($row['date']))."):</strong> $row[comment]</td></tr>";
		}
		
	} else {
		echo '<tr><td>There are no comments for this photo.</td></tr>';
	}
	// Display comment box.
	?>
		<tr>
			<td width="100%">
				<form name="formCommentAdd" action="<?=$_SERVER['PHP_SELF'].'?id='.$_GET['id']?>" method="post">
					<textarea name="comments" rows="3" style="width: 100%"></textarea>
					<br />
					<input type="submit" name="Comment" value="Post Comment" />
				</form>
			</td>
		</tr>
<?
	echo '</table>'; // end comments fieldset
} else {
	echo '<h2>No "id" parameter detected in querystring!</h2>';
}
?>

<?
include_once ('include/footer.inc.php');
?>