<?php

require_once './Db.php';

class MyUtils {
    
    private static function checkRegisterParams($params){
        $req = array("firstname", "lastname", "phoneNumber", "email", "password");
        foreach($req as $key){
            if( ! isset($params[$key])){
                return FALSE;
            }
        }
        return TRUE;
    }
    
    public static function userExistsByEmail(Db $db, $email){
        return $db->con->where("email", $email)->has(Db::$TB_USERS);
    }
    // Db : MysqliDb instance
    // params : user params ("firstname", "lastname", "email", "password") -> array
    public static function addUser(Db $db, $params){
        if(! self::checkRegisterParams($params) ){
            return self::response(true, "missing parameters");
        } else if(self::userExistsByEmail($db, $params["email"])){
            return self::response(true, "user already exists");
        }
        $enc = self::encrypt($params["password"]);
        unset($params["password"]);
        $params["salt"] = $enc["salt"];
        $params["encrypted_pass"] = $enc["encrypted_pass"];
        $id = $db->con->insert(Db::$TB_USERS, $params);

        if(! $id){
            return self::response(true, $db->con->getLastError());
        } else {
            return self::response(false, "registered", 
                        array(  "firstname" => $params["firstname"],
                                "lastname" => $params["lastname"],
                                "email" => $params["email"])
                    );
        }
    }
    
    // $password : user password (simple format)
    // returns array("salt" => "....", "encrypted_pass" => ...)
    public static function encrypt($password){
		$salt = substr(sha1(rand()), 0, 10);
        $encrypted_pass = self::encode($salt, $password);
        return array("salt" => $salt, "encrypted_pass"=>$encrypted_pass);
    }

    private static function encode($salt, $password){
        return base64_encode(sha1($salt . $password, true) . $salt);
    }

    public static function getUserByEmailAndPassword(Db $db, $email, $password){
        $user = $db->con->where("email", $email)->getOne(Db::$TB_USERS);
        if(! $user || empty($user)){
            return NULL;
        }
        $salt = $user['salt'];
        $encrypted_pass = $user['encrypted_pass'];
        if(self::encode($salt, $password) === $encrypted_pass){
            unset($user['salt']);
            unset($user['encrypted_pass']);
            return $user;
        } else {
            return NULL;
        }
    }


    public static function response($error, $msg, $data = null, $json = false){
        $res = array(
            "error" => $error,
            "msg" => $msg,
            "data" => $data
        );
        return $json ? json_encode($res) : $res; 
    }
}