<?php

define('COUNT', 5);
define('LAT', 35.710);
define('LNG', 51.381);
define('EMAIL', "mm@mm.com");
require_once './Db.php';

$db = new Db();
$data = array(
    'email' => EMAIL,
    'latitude'=> LAT,
    'longitude' => LNG,
    'datetime' => date('Y-m-d H:i:s')
);

for($i = 0 ; $i < COUNT ; $i = $i + 1){
    $db->con->insert(Db::$TB_LOCS, $data);
}

echo "done";
