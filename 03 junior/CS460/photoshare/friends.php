<?php
include_once ('include/header.inc.php');

// If postback, try to create/delete friend relationship.
if (isLoggedIn()) {
	if (isset($_POST['friendButton'])) {
		$requestor_id = mysql_real_escape_string($_SESSION['user_id']);
		$requestee_id = mysql_real_escape_string($_POST['requestee_id']);
		if ($_POST['friendButton'] == "Friend User") {
			if (createFriendship($requestor_id, $requestee_id)) {
				echo "<h2>Friendship established!</h2>";
			} else {
				echo "<h2>Friending error!</h2>";
			}
		} else if ($_POST['friendButton'] == "Unfriend User") {
			if (deleteFriendship($requestor_id, $requestee_id)) {
				echo "<h2>Friendship deleted!</h2>";
			} else {
				echo "<h2>Unfriending error!</h2>";
			}
		}
	}
} else {
	echo "<h2>You must be logged in to view this page!</h2>";
	exit();
}

// Display current friends.
?>
<br />

<center>
	<fieldset>
	<legend>My Friends</legend>
	<form name="formAddRemoveFriend" action="<?=$_SERVER['PHP_SELF']?>" method="post">
<?	// Get user's friends.
	$dbConn = db_connect();
	$queryFriends = "(SELECT requestor_id AS user_id FROM Friends WHERE requestee_id='$_SESSION[user_id]') UNION (SELECT requestee_id AS user_id FROM Friends WHERE requestor_id='$_SESSION[user_id]')";
	$result = db_query($queryFriends, $dbConn);
	if (mysql_num_rows($result) > 0) { ?>
	<table align="left" width="100%">
		<thead align="left">
			<tr>
				<th>
					User
				</th>
				<th>
					Name
				</th>
				<th>
					Date of Birth
				</th>
				<th>
					&nbsp;
				</th>
			</tr>
		</thead>
		<tbody align="left">
		<?
		while ($friend = mysql_fetch_array($result)) {
			// Grab friend info.
			$queryInfo = "SELECT user_id, email, CONCAT(firstName,' ',lastName) AS name, dob FROM Users WHERE user_id='$friend[user_id]'";
			$row = mysql_fetch_array(db_query($queryInfo, $dbConn)); ?>
		<tr>
			<td>
				<a href="profile.php?id=<?=$row['user_id']?>"><?=$row['email']?></a>
			</td>
			<td>
				<?=$row['name']?>
			</td>
			<td>
				<?=$row['dob']?>
			</td>
<?	if (isLoggedIn()) {
		if (!areFriends($_SESSION['user_id'], $row['user_id'])) { ?>
			<td colspan="2" align="center">
				<input type="submit" name="friendButton" value="Friend User" onclick="document.formAddRemoveFriend.requestee_id.value=<?=$row['user_id']?>;" />
			</td>
	<?	} else { ?>
			<td colspan="2" align="center">
				<input type="submit" name="friendButton" value="Unfriend User" onclick="document.formAddRemoveFriend.requestee_id.value=<?=$row['user_id']?>;" />
			</td>
	<? }
	} ?>
		</tr>
	<? } ?>
		</tbody>
<?	} else {
		echo '<tr><td colspan="4">You currently have no friends.</td></tr>';
	} ?>
	</table>
	<input type="hidden" name="requestee_id" id="requestee_id" />
	</form>
	</fieldset>
</center>

<?
include_once ('include/footer.inc.php');
?>