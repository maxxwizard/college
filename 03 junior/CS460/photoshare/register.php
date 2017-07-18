<?php
include_once 'include/header.inc.php';

// Try to register the user on postback.
if (isset($_POST['email'])) {
	$dbConn = db_connect();
	$email 			= mysql_real_escape_string(strtolower($_POST['email']));
	$password 		= mysql_real_escape_string($_POST['password1']);
	$firstName 		= mysql_real_escape_string($_POST['firstName']);
	$lastName 		= mysql_real_escape_string($_POST['lastName']);
	$dob 				= mysql_real_escape_string($_POST['dob']);
	$gender			= mysql_real_escape_string($_POST['gender']);
	$home_city		= mysql_real_escape_string($_POST['home_city']);
	$home_state		= mysql_real_escape_string($_POST['home_state']);
	$home_country	= mysql_real_escape_string($_POST['home_country']);
	$addr_city		= mysql_real_escape_string($_POST['addr_city']);
	$addr_state		= mysql_real_escape_string($_POST['addr_state']);
	$addr_country	= mysql_real_escape_string($_POST['addr_country']);
	$eduLevel		= mysql_real_escape_string($_POST['eduLevel']);
	$query = "INSERT INTO Users (email, password, firstName, lastName, dob, gender, home_city, home_state, home_country, addr_city, addr_state, addr_country, eduLevel) VALUES ('$email', '$password', '$firstName', '$lastName', '$dob', '$gender', '$home_city', '$home_state', '$home_country', '$addr_city', '$addr_state', '$addr_country', '$eduLevel')";
	//echo "<pre>$query</pre>";
	$result = db_query($query, $dbConn);
	if (mysql_affected_rows($dbConn) == 1) { ?>
		<h2>You are successfully registered with email <em><?php echo $_POST['email']?></em>! Click <a href="login.php">here</a> to go to the login page.</h2>
	<?php
	} else {
		echo '<h2>Registration error!</h2>';
	}
} else { // Show form if not postback.
?>

<br />

<fieldset>
<legend>User Registration</legend>
<form action="<?php echo $_SERVER['PHP_SELF']?>" method="post" onsubmit="return (validate_reg_form(this) && checkPasswordMatch(this))">
	<table>
		<tr>
			<td><strong>Email:</strong></td>
			<td><input type="text" name="email" value="<?php echo $_POST['email']?>"/></td>
		</tr>
		<tr>
			<td><strong>Password:</strong></td>
			<td><input type="password" name="password1" value="<?php echo $_POST['password1']?>"/></td>
		</tr>
		<tr>
			<td><strong>Re-enter password:</strong></td>
			<td><input type="password" name="password2" value="<?php echo $_POST['password2']?>"/></td>
		</tr>
		<tr>
			<td><strong>First Name:</strong></td>
			<td><input type="text" name="firstName" value="<?php echo $_POST['firstName']?>"/></td>
		</tr>
		<tr>
			<td><strong>Last Name:</strong></td>
			<td><input type="text" name="lastName" value="<?php echo $_POST['lastName']?>"/></td>
		</tr>
		<tr>
			<td><strong>Date of Birth (yyyy-mm-dd):</strong></td>
			<td><input type="text" name="dob" value="<?php echo $_POST['dob']?>"/></td>
		</tr>
		<tr>
			<td>Gender:</td>
			<td>
				<input type="radio" name="gender" value="Male">Male</input>
				<input type="radio" name="gender" value="Female">Female</input>
			</td>
		</tr>
		<tr>
			<td>Home City:</td>
			<td><input type="text" name="home_city" value="<?php echo $_POST['home_city']?>" /></td>
		</tr>
		<tr>
			<td>Home State:</td>
			<td><input type="text" name="home_state" value="<?php echo $_POST['home_state']?>" /></td>
		</tr>
		<tr>
			<td>Home Country:</td>
			<td><input type="text" name="home_country" value="<?php echo $_POST['home_country']?>" /></td>
		</tr>
		<tr>
			<td>Current City:</td>
			<td><input type="text" name="addr_city" value="<?php echo $_POST['addr_city']?>" /></td>
		</tr>
		<tr>
			<td>Current State:</td>
			<td><input type="text" name="addr_state" value="<?php echo $_POST['addr_state']?>" /></td>
		</tr>
		<tr>
			<td>Current Country:</td>
			<td><input type="text" name="addr_country" value="<?php echo $_POST['addr_country']?>" /></td>
		</tr>
		<tr>
			<td>Education Level:</td>
			<td>
				<select name="eduLevel">
					<option value=""></option>
					<option value="High School">High School</option>
					<option value="College">College</option>
					<option value="Graduate">Graduate</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>
				&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<input type="submit" value="Register" />
			</td>
		</tr>
	</table>
</form>
</fieldset>

<?php
}
include_once 'include/footer.inc.php';
?>