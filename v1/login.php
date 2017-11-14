<?php

require_once './Db.php';
require_once './MyUtils.php';
// email, password
if(!isset($_GET["email"]) || !isset($_GET["password"])){
    echo MyUtils::response(true, "Required parameters email and password are missing.", null, true);
    return;
}

$email = $_GET["email"];
$pass = $_GET["password"];
$db = new Db();
$user = MyUtils::getUserByEmailAndPassword($db, $email, $pass);
if($user === NULL){
    echo MyUtils::response(true, "Login credentials are wrong! please try again later.", null, true);
} else {
    echo MyUtils::response(false, "Successfully logged in.", $user, true);
}

