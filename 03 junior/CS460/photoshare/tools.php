<?php
include_once ('include/header.inc.php');

// Display tag recommendation tool.
?>
<br />
<center>
	<fieldset>
	<legend>Tag Recommendation Tool</legend>
	<form name="formTagRecTool" action="<?=$_SERVER['PHP_SELF']?>" method="post">
		<div>
			<h3>Type in a couple of tags and you'll get recommendations (e.g. "city building").</h3>
			<input type="text" name="tags" align="middle" size="60" value="<?=$_POST['tags']?>" />
			<br />
			<input type="submit" value="Get Recommendations" />
		</div>
	</form>
<?	if (isset($_POST['tags']) && $_POST['tags'] != "") {
		// Parse search string into multiple tags.
		$tags = explode(" ", $_POST['tags']);
		// Grab all related tag_names in DB, ordering by frequency.
		$dbConn = db_connect();
		$query = "	SELECT tag_name, count(*) as count
						FROM (SELECT DISTINCT photo_id
						FROM Tags
						WHERE %s) AS P INNER JOIN Tags T ON P.photo_id=T.photo_id
						GROUP BY tag_name
						ORDER BY count DESC
						LIMIT 10";
		$where = "";
		foreach ($tags as $tag) {
			if ($where != "")
				$where .= " OR ";
			$where .= "tag_name='".mysql_real_escape_string($tag)."'";
		}
		$query = sprintf($query, $where);
		$result = db_query($query, $dbConn);
		// Show all related tags.
		if (mysql_num_rows($result) > 0) {
			echo '<table>';		
			while ($row = mysql_fetch_array($result)) {
				if (!in_array($row['tag_name'], $tags)) { // Exclude the search tags.
					echo '<tr><td><a href="search.php?q='.$row['tag_name'].'">'.$row['tag_name']."</a> ($row[count])</td></tr>";
				}
			}
			echo '</table>';
		} else {
			echo '<h4>No related tags could be found.</h4>';
		}
	}
?>
	</fieldset>

<? if (isLoggedIn()) { ?>
	<fieldset>
	<legend>You-May-Also-Like...</legend>
	<form name="formPhotoRecTool" action="<?=$_SERVER['PHP_SELF']?>" method="post">
		<div>
			<h3>Click the button to get a listing of photos you may also like.</h3>
			<input type="submit" name="submit" value="I might also like..." />
		</div>
	</form>
<?	if (isset($_POST['submit'])) {
		echo '<table>';
		$dbConn = db_connect();
		$query5Tags = "SELECT T.tag_name FROM Tags T, Photos P, Albums A WHERE T.photo_id=P.photo_id AND P.album_id=A.album_id AND A.owner_id=1
							GROUP BY T.tag_name ORDER BY COUNT( P.photo_id ) DESC LIMIT 5";
		//echo "<pre>$query5Tags</pre>";
		$result = db_query($query5Tags, $dbConn);
		$tags = "";
		while ($row = mysql_fetch_array($result)) {
			if ($tags != "")
				$tags .= ", ";
			$tags .= "'".$row['tag_name']."'";
		}
		//echo "<pre>tags: $tags</pre>";
		$queryPhotoByTagCount = "	SELECT photo_id, count(tag_name) as count
											FROM Tags
											WHERE tag_name IN ($tags)
											GROUP BY photo_id
											ORDER BY count DESC";
		//echo "<pre>$queryPhotoByTagCount</pre>";
		$result = db_query($queryPhotoByTagCount, $dbConn);
		while ($row = mysql_fetch_array($result)) {
		?>
			<tr>
				<td><a href="viewPhoto.php?id=<?=$row['photo_id']?>"><img src="photo.php?id=<?=$row['photo_id']?>&thumb=true" /></a> (<?=$row['count']?>)</td>
		</tr>
	<?	} ?>
	</table>
	</fieldset>
<?	}
} ?>
</center>

<?
include_once ('include/footer.inc.php');
?>