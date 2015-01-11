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
            $response["user"]["sifre"] = $user["kullanici_sifre"];
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
    else if ($tag == 'upload') {
        if($_FILES['image']['name']) {
            $image_path =$_FILES['image']['name'];

            list($file,$error) = upload('image','uploads/','jpeg,gif,png,jpg');
            if($error) print $error;
            $magicianObj = new imageLib("uploads/".$image_path);
            //$magicianObj -> resizeImage(1280, 960, 'crop');
            $magicianObj -> saveImage("uploads/".$image_path);


            $aciklama = $_POST['aciklama'];
            $lat = $_POST['latitude'];
            $long=$_POST['longitude'];
            $kategori_id = $_POST['kategori_id'];
            $kullanici_id = $_POST['kullanici_id'];
            $lokasyon_id = $_POST['lokasyon_id'];
            $user = $db->saveComplaint($image_path,$aciklama,$lat,$long,$kategori_id,$kullanici_id,$lokasyon_id);

            if ($user != false) {

                $response["success"] = 1;
                echo json_encode($response);
            } else {
                $response["error"] = 1;
                $response["error_msg"] = "Error!! Cannot Upload";
                echo json_encode($response);
            }
        }
    }
	else if($tag == 'add_complaint'){
		
		 $kullanici_email = $_POST['kullanici_email'];
         $kullanici_id = $_POST['kullanici_id'];
         $sikayet_fotograf=$_POST['sikayet_fotograf'];
         $sikayet_aciklama = $_POST['sikayet_aciklama'];
         $sikayet_latitude = $_POST['sikayet_latitude'];
         $sikayet_longitude = $_POST['sikayet_longitude'];
		 $sikayet_kategori_id = $_POST['sikayet_kategori_id'];
		 $sikayet_onay = $_POST['sikayet_onay'];
		 $sikayet_onay_aciklama = $_POST['sikayet_onay_aciklama'];
		 $sikayet_tarih = $_POST['sikayet_tarih'];
		 
		 $user = $db->addComplaint($kullanici_email,$kullanici_id,$sikayet_fotograf,$sikayet_aciklama,$sikayet_latitude,$sikayet_longitude,$sikayet_kategori_id, $sikayet_onay,$sikayet_onay_aciklama,$sikayet_tarih);
		 
		 if ($user != false) {
                $response["success"] = 1;
                echo json_encode($response);
         } else {
                $response["error"] = 1;
                $response["error_msg"] = "Error!! Cannot Add";
                echo json_encode($response);
         }
		   
	}

	else if ($tag == 'getMyComplaints') {
        $mail = $_POST['mail'];
        $getcomplaint = $db->getMyComplaints($mail);
        $response["complaints"] = array();
        if ($getcomplaint) {
            $response["success"] = 1;
            while ($row = mysql_fetch_array($getcomplaint)) {
                $complaint["kullanici_email"] = $row["kullanici_email"];
                $complaint["kullanici_id"] = $row["kullanici_id"];
                $complaint["sikayet_fotograf"] = $row["sikayet_fotograf"];
                $complaint["sikayet_aciklama"] = $row["sikayet_aciklama"];
                $complaint["sikayet_latitude"] = $row["sikayet_latitude"];
				$complaint["sikayet_longitude"] = $row["sikayet_longitude"];
                $complaint["sikayet_kategori_id"] = $row["sikayet_kategori_id"];
                $complaint["sikayet_onay"] = $row["sikayet_onay"];
				$complaint["sikayet_onay_aciklama"] = $row["sikayet_onay_aciklama"];
				$complaint["sikayet_tarih"] = $row["sikayet_tarih"];
                array_push($response["complaints"], $complaint);
            }
            echo json_encode($response);
        } else {
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in Getting Place";
            echo json_encode($response);
        }
    }
		else if ($tag == 'getApprovedComplaints') {
		$onay = $_POST['onay'];
        $getcomplaint = $db->getApprovedComplaints($onay);
        $response["complaints"] = array();
        if ($getcomplaint) {
            $response["success"] = 1;
            while ($row = mysql_fetch_array($getcomplaint)) {
                $complaint["kullanici_email"] = $row["kullanici_email"];
                $complaint["kullanici_id"] = $row["kullanici_id"];
                $complaint["sikayet_fotograf"] = $row["sikayet_fotograf"];
                $complaint["sikayet_aciklama"] = $row["sikayet_aciklama"];
                $complaint["sikayet_latitude"] = $row["sikayet_latitude"];
				$complaint["sikayet_longitude"] = $row["sikayet_longitude"];
                $complaint["sikayet_kategori_id"] = $row["sikayet_kategori_id"];
                $complaint["sikayet_onay"] = $row["sikayet_onay"];
				$complaint["sikayet_onay_aciklama"] = $row["sikayet_onay_aciklama"];
				$complaint["sikayet_tarih"] = $row["sikayet_tarih"];
                array_push($response["complaints"], $complaint);
            }
            echo json_encode($response);
        } else {
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in Getting Place";
            echo json_encode($response);
        }
    }
    
    /*
    *
    *ADDED IN 21.12.2014 NEW METHODS
    */
    else if ($tag == 'getWaitingComplaints') {
        $getcomplaint = $db->getWaitingComplaints();
        $response["complaints"] = array();
        if ($getcomplaint) {
            $response["success"] = 1;
            while ($row = mysql_fetch_array($getcomplaint)) {
                $complaint["idsikayet"] = $row["idsikayet"];
                $complaint["kullanici_email"] = $row["kullanici_email"];
                $complaint["kullanici_id"] = $row["kullanici_id"];
                $complaint["sikayet_fotograf"] = $row["sikayet_fotograf"];
                $complaint["sikayet_aciklama"] = $row["sikayet_aciklama"];
                $complaint["sikayet_latitude"] = $row["sikayet_latitude"];
				$complaint["sikayet_longitude"] = $row["sikayet_longitude"];
                $complaint["sikayet_kategori_id"] = $row["sikayet_kategori_id"];
                $complaint["sikayet_onay"] = $row["sikayet_onay"];
				$complaint["sikayet_onay_aciklama"] = $row["sikayet_onay_aciklama"];
				$complaint["sikayet_tarih"] = $row["sikayet_tarih"];
                array_push($response["complaints"], $complaint);
            }
            echo json_encode($response);
        } else {
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in Getting Place";
            echo json_encode($response);
        }
    }
    else if ($tag == 'update_complaint') {
        $idsikayet = $_POST['idsikayet'];
        $sikayet_onay = $_POST['sikayet_onay'];
        $sikayet_onay_aciklama = $_POST['sikayet_onay_aciklama'];

        $status = $db->updateComplaintStatus($idsikayet, $sikayet_onay, $sikayet_onay_aciklama);
        if ($status) {
            $response["success"] = 1;
            echo json_encode($response);
        } else {
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in Updating Rating";
            echo json_encode($response);
        }
    }
    else if ($tag == 'update_password') {
        $kullanici_id = $_POST['kullanici_id'];
        $kullanici_mail = $_POST['kullanici_mail'];
        $kullanici_yeni_sifre = $_POST['kullanici_yeni_sifre'];

        $status = $db->updateUserPassword($kullanici_id, $kullanici_mail, $kullanici_yeni_sifre);
        if ($status) {
            $response["success"] = 1;
            echo json_encode($response);
        } else {
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in Updating Rating";
            echo json_encode($response);
        }
    }
    else if ($tag == 'update_user_info') {
        $kullanici_id = $_POST['kullanici_id'];
        $kullanici_mail = $_POST['kullanici_mail'];
        $kullanici_telefon = $_POST['kullanici_telefon'];

        $status = $db->updateUserInfo($kullanici_id, $kullanici_mail, $kullanici_telefon);
        if ($status) {
            $response["success"] = 1;
            echo json_encode($response);
        } else {
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in Updating Rating";
            echo json_encode($response);
        }
    }
    else if ($tag == 'getComplaintsViaCategory') {
		$sikayet_kategori_id = $_POST['sikayet_kategori_id'];
        $getcomplaint = $db->getComplaintsViaCategory($sikayet_kategori_id);
        $response["complaints"] = array();
        if ($getcomplaint) {
            $response["success"] = 1;
            while ($row = mysql_fetch_array($getcomplaint)) {
                $complaint["kullanici_email"] = $row["kullanici_email"];
                $complaint["kullanici_id"] = $row["kullanici_id"];
                $complaint["sikayet_fotograf"] = $row["sikayet_fotograf"];
                $complaint["sikayet_aciklama"] = $row["sikayet_aciklama"];
                $complaint["sikayet_latitude"] = $row["sikayet_latitude"];
				$complaint["sikayet_longitude"] = $row["sikayet_longitude"];
                $complaint["sikayet_kategori_id"] = $row["sikayet_kategori_id"];
                $complaint["sikayet_onay"] = $row["sikayet_onay"];
				$complaint["sikayet_onay_aciklama"] = $row["sikayet_onay_aciklama"];
				$complaint["sikayet_tarih"] = $row["sikayet_tarih"];
                array_push($response["complaints"], $complaint);
            }
            echo json_encode($response);
        } else {
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in Getting Place";
            echo json_encode($response);
        }
    }
	
	else if($tag == 'resetPassword'){
		
		 $kullanici_email = $_POST['kullanici_email']; 
		 $user = $db->resetPassword($kullanici_email);
		 
		 if ($user) {
                $response["success"] = 1;
                echo json_encode($response);
         } else {
                $response["error"] = 1;
                $response["error_msg"] = "Error!! Cannot Add";
                echo json_encode($response);
         }
		   
	}
	/*
    *
    *NEW METHODS
    */
		
    //
}
else{

    echo "Access Denied";
}
?>
