<?php

require_once './Db.php';

if( isset($_POST['email']) && isset($_POST['lat']) && isset($_POST['lng']) && isset($_POST['phoneNumber']))
{

    $db = new Db();
    $email = $_POST['email'];
    if($db->con->where('email', $email)->has(Db::$TB_USERS)){
        $data = array(
            'email' => $email,
            'phoneNumber' => $_POST['phoneNumber'],
            'latitude'=>$_POST['lat'],
            'longitude' => $_POST['lng'],
            'datetime' => date('Y-m-d H:i:s')
        );
        if($db->con->insert(Db::$TB_LOCS_EMS, $data)){
            echo json_encode(array("error" => false, "msg" => "ok", "data"=>null));
        } else {
            echo json_encode(array("error" => true, "msg" => "error", "data"=>null));
        }
    } else {
        echo json_encode(array("error" => true, "msg" => "user not found", "data"=>null));
    }
} else {
    echo json_encode(array("error" => true, "msg" => "wrong parameters", "data"=>null));
}


