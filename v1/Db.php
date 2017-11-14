<?php

require_once './MysqliDb.php';

define("HOST" , "127.0.0.1");
define("DB" , "gauchosafe_db");
define("DB_USERNAME" , "root");
define("DB_PASSWORD" , "salam_dada");


class Db {

    public static $TB_USERS = "table_users";
    public static $TB_LOCS = "table_locs";
    public static $TB_LOCS_EMS = "table_locs_ems";

    public $con ;

    function __construct(){
        $this->con = $this->connect();
    }

    private function connect(){
        $instance = MysqliDb::getInstance();
        if(! $this->con){
            $instance = new MysqliDb(array(
                "host" => HOST,
                "db" => DB,
                "username" => DB_USERNAME,
                "password" => DB_PASSWORD,
                "charset" => "utf8"
            ));
        }
        return $instance;
    }

}
