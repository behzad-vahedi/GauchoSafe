<?php
ob_start();
session_start();

//set timezone
date_default_timezone_set('America/Los_Angeles');

//database credentials
define('DBHOST','127.0.0.1');
define('DBUSER','root');
define('DBPASS','salam_dada');
define('DBNAME','apiloc_db');

//application address
define('DIR','http://localhost/apiloc/web/loginregister-master/');
define('SITEEMAIL','behzad.vahedi@yahoo.com');

try {

	//create PDO connection
	$db = new PDO("mysql:host=".DBHOST.";dbname=".DBNAME, DBUSER, DBPASS);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

} catch(PDOException $e) {
	//show error
    echo '<p class="bg-danger">'.$e->getMessage().'</p>';
    exit;
}

//include the user class, pass in the database connection
include('classes/user.php');
include('classes/phpmailer/mail.php');
$user = new User($db);
?>
