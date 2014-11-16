<?php

header('Content-Type: application/json; charset=UTF-8');
error_reporting(E_ALL ^ E_DEPRECATED);


if (isset($_POST['tag']) && $_POST['tag'] != '') {
    // get tag
    $tag = $_POST['tag'];

    // include db handler
    require_once 'Db_Functions_cd.php';
    $db = new Db_Functions_cd();

    // response Array
    $response = array("tag" => $tag, "success" => 0, "error" => 0);

    if ($tag == 'login') {

        $mail = $_POST['mail'];
        $sifre = $_POST['sifre'];

        // check for user
        $user = $db->loginUser($mail, $sifre);
        if ($user != false) {
            // user found
            // echo json with success = 1
            $response["success"] = 1;
            $response["user"]["id"] = $user["kullanici_id"];
            $response["user"]["ad"] = $user["kullanici_adi"];
            $response["user"]["soyad"] = $user["kullanici_soyadi"];
            $response["user"]["mail"] = $user["kullanici_mail"];
            $response["user"]["telefon"] = $user["kullanici_telefon"];

            echo json_encode($response);
        } else {
            // user not found
            // echo json with error = 1
            $response["error"] = 1;
            $response["error_msg"] = "Yanlış email veya şifre!";
            echo json_encode($response);
        }
    }
    else if ($tag == 'signup') {

		$mail = $_POST['mail'];
        $sifre = $_POST['sifre'];
        $ad=$_POST['ad'];
        $soyad = $_POST['soyad'];
        $tel = $_POST['tel'];
        $user = $db->signupUser($mail,$sifre,$ad,$soyad,$tel);

        if ($user != false) {

		    $response["success"] = 1;
            echo json_encode($response);
        } else {
            $response["error"] = 1;
            $response["error_msg"] = "E-mail address already exist!";
            echo json_encode($response);
        }
     }

		
    //
}
else{

    echo "Access Denied";
}

?>
