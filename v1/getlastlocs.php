<?php

require_once './Db.php';
date_default_timezone_set("America/Los_Angeles");

if( isset($_GET['email']))
{

    $db = new Db();
    $email = $_GET['email'];
    if($db->con->has(Db::$TB_USERS)){
        $datefrom = date('Y-m-d H:i:s', strtotime('-60 days'));
        $result = $db->con->where("datetime", $datefrom, ">=")
            ->get(Db::$TB_LOCS, null, array("latitude", "longitude"));
        if($result){
            echo json_encode(array("error" => false, "msg" => "ok", "data"=> $result));
        } else {
            echo json_encode(array("error" => true, "msg" => "error", "data"=>null));
        }
    } else {
        echo json_encode(array("error" => true, "msg" => "user not found", "data"=>null));
    }
} else {
    echo json_encode(array("error" => true, "msg" => "wrong parameters", "data"=>null));
}


