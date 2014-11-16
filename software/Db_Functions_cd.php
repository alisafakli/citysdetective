<?php

class Db_Functions_cd{

    private $db;

    //put your code here
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }

    // destructor
    function __destruct() {

    }
	
   function signupUser($mail,$sifre,$ad,$soyad,$telefon){

	$result = mysql_query("INSERT INTO kullanici_bilgileri (kullanici_adi,kullanici_soyadi,kullanici_mail,kullanici_sifre,kullanici_telefon) VALUES ('$ad','$soyad','$mail','$sifre','$telefon')") or die(mysql_error());


        //$query = mysql_num_rows($result);

        	if($result){
            // get user details
            //$uid = mysql_insert_id(); // last inserted id
           // $result = mysql_query("SELECT * FROM kullanici_bilgileri WHERE kullanici_id = $uid");
            // return user details
            return $result;//mysql_fetch_array($result);
        } else {
            return false;
        
        }

    }
    function checkEmail($mail){
        $result = mysql_query("SELECT kullanici_mail FROM kullanici_bilgileri  WHERE kullanici_mail = '$mail'") or die(mysql_error());
        if($result){

            return $result;//mysql_fetch_array($result);
        } else {
            return false;

        }

    }



   function loginUser($mail,$sifre){
       $result = mysql_query("SELECT * FROM kullanici_bilgileri WHERE kullanici_mail = '$mail' and kullanici_sifre='$sifre'") or die(mysql_error());
       // check for result
       $no_of_rows = mysql_num_rows($result);
       if ($no_of_rows > 0) {
           $result = mysql_fetch_array($result);
           return $result;
       } else {
           return false;
       }
   }


	
}
?>
