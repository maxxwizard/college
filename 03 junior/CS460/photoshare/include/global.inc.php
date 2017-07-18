<?php

# database constants
define (DB_HOST, 'localhost');
define (DB_USER, 'root');
define (DB_PASS, '');
define (DB_NAME, 'photoshare');

function isLoggedIn() {
	if ($_SESSION['email'] && $_SESSION['user_id']) {
		return true;
	}
	return false;
}

function stripslashes_deep($value) {
	$value = is_array($value) ?
					array_map('stripslashes_deep', $value) :
					stripslashes($value);
					
	return $value;
}

/* Function that returns a thumbnail image of an image */
function createThumbnail($data, $content_type, $new_w, $new_h)
{
	$src_img = imagecreatefromstring($data);
	
	$old_x = imageSX($src_img);
	$old_y = imageSY($src_img);
	
	/*
	$thumb_w = $new_w;
	$thumb_h = $new_h;
	$ratio_orig = $old_x / $old_y;
	if ($new_x/$new_y > $ratio_orig) {
		$thumb_w = $new_y * $ratio_orig;
	} else {
		$thumb_y = $new_x / $ratio_orig;
	}
	*/
	/*
	if ($old_x <= $new_x && $old_y <= $new_y) { // if the requested dimensions fit the old ones, don't resize
		$scale = 1.0;
	} else {
		if ($old_x > $old_y) {
			$scale = $new_x / $old_x;
		}
		$scale = (float)$new_y / $old_y;
	}
	$thumb_w = $old_x * $scale;
	$thumb_h = $old_y * $scale;
	*/
	
	if ($old_x > $old_y) {
		$thumb_w = $new_w;
		$thumb_h = $old_y * ($new_h/$old_x);
	}
	if ($old_x < $old_y) {
		$thumb_w = $old_x * ($new_w/$old_y);
		$thumb_h = $new_h;
	}
	if ($old_x == $old_y) {
		$thumb_w = $new_w;
		$thumb_h = $new_h;
	}
	
	$dst_img = imagecreatetruecolor($thumb_w, $thumb_h);
	imagecopyresampled($dst_img, $src_img, 0, 0, 0, 0, $thumb_w, $thumb_h, $old_x, $old_y);
	$tmpfile = tempnam("/tmp", "tmpImg");
	if (strstr($content_type, "png"))
	{
		imagepng($dst_img, $tmpfile);
	} else if (strstr($content_type, "jpg") || strstr($content_type, "jpeg")) {
		imagejpeg($dst_img, $tmpfile);
	} else if (strstr($content_type, "gif")) {
		imagegif($dst_img, $tmpfile);
	}
	imagedestroy($src_img);
	imagedestroy($dst_img);
	return file_get_contents($tmpfile);
}

/* Wrapper functions for common MySQL DB functions. */
function db_connect() {
	$result = mysql_connect(DB_HOST, DB_USER, DB_PASS) or die(mysql_error());
	mysql_select_db(DB_NAME, $result) or die(mysql_error());
	return $result;
}

function db_query($query, $dbConn) {
	$result = mysql_query($query, $dbConn) or die(mysql_error());
	return $result;
}

function isPhotoOwner($owner_id, $photo_id) {
	$dbConn = db_connect();
	$query = sprintf("SELECT A.owner_id FROM Albums A, Photos P WHERE photo_id=%d AND A.album_id=P.album_id", mysql_real_escape_string($photo_id));
	$result = db_query($query, $dbConn);
	$row = mysql_fetch_array($result);
	if ($owner_id == $row['owner_id'])
		return true;
	else
		return false;
}

function areFriends($user_id1, $user_id2)
{
	$dbConn = db_connect();
	$query = sprintf("SELECT * FROM Friends WHERE (requestor_id='$user_id1' AND requestee_id='$user_id2') OR (requestor_id='$user_id2' AND requestee_id='$user_id1')");
	$result = db_query($query, $dbConn);
	if (mysql_num_rows($result) == 1) {
		return true;
	} else {
		return false;
	}
}

function createFriendship($requestor_id, $requestee_id)
{
	$dbConn = db_connect();
	$queryFriend = "INSERT INTO Friends (requestor_id, requestee_id) VALUES ($requestor_id, $requestee_id)";
	$resultFriend = db_query($queryFriend, $dbConn);
	if (mysql_affected_rows($dbConn) == 1) {
		return true;
	} else {
		return false;
	}
}

function deleteFriendship($requestor_id, $requestee_id)
{
	$dbConn = db_connect();
	$queryUnfriend = "DELETE FROM Friends
							WHERE (requestor_id='$requestor_id' AND requestee_id='$requestee_id')
									OR (requestor_id='$requestee_id' AND requestee_id='$requestor_id')";
	$resultUnfriend = db_query($queryUnfriend, $dbConn);
	if (mysql_affected_rows($dbConn) == 1) {
		return true;
	} else {
		return false;
	}
}

?>