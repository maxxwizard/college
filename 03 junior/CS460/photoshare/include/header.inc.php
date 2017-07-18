<?php
	include_once ('include/global.inc.php');
	session_start();
?>
<html>
<head>
	<title>Photo Sharing Application</title>
</head>

<body>
<script type="text/javascript" src="include/validation.js"></script>

<div style="width: 800px; margin-left: auto; margin-right: auto;">
<h1>A Photo Sharing Application for CS460/660 PA1</h1>

<table width="100%">
	<tr>
		
		<? if (isLoggedIn()) { ?> <td><strong><?=$_SESSION['email']?></strong></td> <? } ?>
		<td><a href="index.php">Home</a></td>
		<? if (isLoggedIn()) { ?> <td><a href="profile.php?id=<?=$_SESSION['user_id']?>">Profile</a></td>
		<? } else { ?>
		<td><a href="login.php">Log In</a></td>
		<td><a href="register.php">Register</a></td><? } ?>
		<td><a href="albums.php">Albums</a></td>
		<? if (isLoggedIn()) { ?> <td><a href="friends.php">Friends</a></td> <? } ?>
		<td><a href="users.php">Users</a></td>
		<td><a href="search.php">Search</a></td>
		<td><a href="tools.php">Tools</a></td>
		<? if (isLoggedIn()) { ?><td><a href="logout.php">Log Out</a></td><? } ?>
	</tr>
</table>

