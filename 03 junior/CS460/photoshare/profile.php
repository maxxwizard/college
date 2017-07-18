<?php
include_once ('include/header.inc.php');

// Display user profile if id specified.
if (!isset($_GET['id']) || !is_numeric($_GET['id'])) {
	echo "<h2>Parameter 'id' not found in querystring!</h2>";
} else {
	
	// If postback, try to create/delete friend relationship.
	if (isLoggedIn() && isset($_POST['friendButton'])) {
		$requestor_id = mysql_real_escape_string($_SESSION['user_id']);
		$requestee_id = mysql_real_escape_string($_GET['id']);
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
	
	$dbConn = db_connect();
	$query = sprintf("SELECT * FROM Users WHERE user_id='%d'", mysql_real_escape_string($_GET['id']));
	$result = db_query($query, $dbConn);
	$row = mysql_fetch_array($result);
?>
<br />
<fieldset>
<legend>Profile</legend>
<form name="formAddRemoveFriend" action="profile.php?id=<?=$_GET['id']?>" method="post">
<table>
	<tr>
		<td><strong>Name:</strong></td>
		<td><?=$row['firstName']?> <?=$row['lastName']?></td>
	</tr>
	<tr>
		<td><strong>Email:</strong></td>
		<td><?=$row['email']?></td>
	</tr>
	<tr>
		<td><strong>Date of Birth:</strong></td>
		<td><?=date("F j, Y", $row['dob'])?></td>
	</tr>
	<tr>
		<td><strong>Gender:</strong></td>
		<td><?=$row['gender']?></td>
	</tr>
	<tr>
		<td><strong>Home Address:</strong></td>
		<td><?=$row['home_city']?>, <?=$row['home_state']?>, <?=$row['home_country']?></td>
	</tr>
	<tr>
		<td><strong>Current Address:</strong></td>
		<td><?=$row['addr_city']?>, <?=$row['addr_state']?>, <?=$row['addr_country']?></td>
	</tr>
	<tr>
		<td><strong>Education Level:</strong></td>
		<td><?=$row['eduLevel']?></td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
<?	if (isLoggedIn()) {
		if (!areFriends($_SESSION['user_id'], $row['user_id'])) { ?>
	<tr>
		<td colspan="2" align="center">
			<input type="submit" name="friendButton" value="Friend User" />
		</td>
	</tr>
<?		} else { ?>
	<tr>
		<td colspan="2" align="center">
			<input type="submit" name="friendButton" value="Unfriend User" />
		</td>
	</tr>
<? 	}
	} ?>
</table>
</form>
</fieldset>

<?
}

include_once ('include/footer.inc.php');
?>