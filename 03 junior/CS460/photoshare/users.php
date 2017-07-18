<?php
include_once ('include/header.inc.php');

// If postback, try to create/delete friend relationship.
if (isLoggedIn() && isset($_POST['friendButton'])) {
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

// Display search box to search for other users.
?>
<br />

<center>
	<fieldset>
	<legend>Search Users</legend>
	<h3>Type in the email address of the person you are searching for (e.g. "test@bu.edu").</h3>
	<form action="<?=$_SERVER['PHP_SELF']?>" name="formSearch" method="get">
		<div>
			<input type="text" name="q" align="middle" size="60" value="<?=$_GET['q']?>" />
			<br />
			<input type="submit" value="Search" />
		</div>
	</form>
	</fieldset>
<? // If search string found, display search results.
if (isset($_GET['q']) && $_GET['q'] != "") { ?>
	<form name="formAddRemoveFriend" action="<?=$_SERVER['PHP_SELF'].'?q='.$_GET['q']?>" method="post">
	<fieldset>
	<legend>Results</legend>
<?	$dbConn = db_connect();
	$query = "SELECT user_id, email, CONCAT(firstName,' ',lastName) AS name, dob FROM Users WHERE email LIKE '%".$_GET['q']."%'";
	$result = db_query($query, $dbConn);
	// If there are search results, display them.
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
		while ($row = mysql_fetch_array($result)) { ?>
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
		echo '<tr><td colspan="4">User not found.</td></tr>';
	}
} ?>
	</table>
	</fieldset>
	<input type="hidden" name="requestee_id" id="requestee_id" />
	</form>
</center>

<?
include_once ('include/footer.inc.php');
?>