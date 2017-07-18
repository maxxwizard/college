<?php
include_once ('include/header.inc.php');
?>

<br />

<fieldset>
<legend>Photostream</legend>
<table width="100%">
	<tbody>
		<tr>
	<?	$dbConn = db_connect();
		$query = "SELECT album_id, photo_id FROM Photos ORDER BY photo_id DESC LIMIT 3";
		$result = db_query($query, $dbConn);
		while ($row = mysql_fetch_array($result)) {
		?>
			<td><a href="viewPhoto.php?id=<?=$row['photo_id']?>&albumid=<?=$row['album_id']?>"><img src="photo.php?id=<?=$row['photo_id']?>&thumb=true" /></a></td>
	<?	} ?>
		</tr>
	</tbody>
</table>
</fieldset>

<br />

<fieldset>
<legend>Top 10 Tags</legend>
<table>
	<tbody>
<?	$dbConn = db_connect();
	$query = "SELECT tag_name, COUNT( photo_id ) AS count FROM Tags GROUP BY tag_name ORDER BY count DESC LIMIT 10";
	$result = db_query($query, $dbConn);
	while ($row = mysql_fetch_array($result)) {
	?>
		<tr>
			<td><a href="search.php?q=<?=$row['tag_name']?>"><?=$row['tag_name']?></a> (<?=$row['count']?>)</td>
		</tr>
<?	} ?>
	</tbody>
</table>
</fieldset>

<br />

<fieldset>
<legend>Top 10 Users</legend>
<table>
	<tbody>
<?	$dbConn = db_connect();
	$query = "	SELECT U.user_id, CONCAT(U.firstName,' ', U.lastName) AS name, (IFNULL(PhotoC.count, 0)+IFNULL(CommentC.count, 0)) AS count
					FROM Users U LEFT JOIN (SELECT U.user_id, COUNT( comment_id ) AS count
													FROM Users U
													LEFT JOIN Comments C ON U.user_id = C.owner_id
													GROUP BY U.user_id) AS CommentC ON U.user_id=CommentC.user_id
									 LEFT JOIN (SELECT U.user_id, count(photo_id) AS count
													FROM Users U LEFT JOIN Albums A ON U.user_id=A.owner_id INNER JOIN Photos P ON A.album_id=P.album_id
													GROUP BY U.user_id) AS PhotoC ON U.user_id=PhotoC.user_id
					GROUP BY U.user_id
					ORDER BY count DESC
					LIMIT 10";
	$result = db_query($query, $dbConn);
	$i = 1;
	while ($row = mysql_fetch_array($result)) {
	?>
		<tr>
			<td><strong><?=$i++?></strong></td>
			<td><a href="profile.php?id=<?=$row['user_id']?>"><?=$row['name']?></a> (<?=$row['count']?>)</td>
		</tr>
<?	} ?>
	</tbody>
</table>
</fieldset>

<?
include_once('include/footer.inc.php');
?>