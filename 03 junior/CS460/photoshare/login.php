<?php
include_once ('include/header.inc.php');

// If user is already logged in, redirect to homepage.
if (isLoggedIn()) {
	echo "<h1>You're already logged in!</h1>";
	exit;
}
	
// If postback, log user in and redirect to homepage.
if (isset($_POST['email']) && isset($_POST['password'])) {
	$dbConn = db_connect();
	$result = db_query("SELECT `user_id`, `email` FROM `Users` WHERE `email`='".mysql_real_escape_string(strtolower($_POST['email'])).
								"' AND `password`='".mysql_real_escape_string($_POST['password'])."'", $dbConn);
	if (mysql_num_rows($result) == 1) {
		$row = mysql_fetch_array($result);
		$_SESSION['user_id'] = $row['user_id'];
		$_SESSION['email'] = $row['email'];
		mysql_free_result($result);
		echo '<script type="text/javascript">window.location="http://'.$_SERVER['SERVER_NAME'].'/photoshare/index.php";</script>';
	} else {
		echo '<h2>Login error!</h2>';
	}
} else {
?>

<br />

<fieldset>
<legend>Log In</legend>
<form method="POST" action="<?= $_SERVER['PHP_SELF'] ?>" onsubmit="return validate_login_form(this)">
<table>
	<tr>
		<th>Email</th>
		<td>
			<input type="text" name="email" value="<?=$_POST['email']?>">
		</td>
	</tr>
	<tr>
		<th>Password</th>
		<td>
			<input type="password" name="password" value="<?=$_POST['password']?>">
		</td>
	</tr>
	<tr>
		<td colspan="2" align="right">
			<input type="submit" value="Login" />
		</td>
	</tr>
</table>
</form>
</fieldset>

<?
}
include_once ('include/footer.inc.php');
?>