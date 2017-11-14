<?php

require_once './Db.php';
require_once './MyUtils.php';

$db = new Db();
$params = $_POST;
if(count($params) != 5){
    echo MyUtils::response(true, "wrong parameters", null, true);
    return;
}
$res = MyUtils::addUser($db, $params);
echo json_encode($res);
