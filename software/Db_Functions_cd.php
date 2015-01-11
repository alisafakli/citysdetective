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

    function saveComplaint($image_path,$aciklama,$lat,$long,$kategori_id,$kullanici_id,$lokasyon_id){

        $result = mysql_query("INSERT INTO sikayet (sikayet_fotograf,sikayet_aciklama,sikayet_latitude,sikayet_longitude,kategori_id,kullanici_id,lokasyon_id) VALUES ('$image_path','$aciklama','$lat','$long','$kategori_id','$kullanici_id','$lokasyon_id')") or die(mysql_error());

        if($result){

            return $result;//mysql_fetch_array($result);
        } else {
            return false;

        }
    }
	   function addComplaint($kullanici_email,$kullanici_id,$sikayet_fotograf,$sikayet_aciklama,$sikayet_latitude,$sikayet_longitude,$sikayet_kategori_id, $sikayet_onay,$sikayet_onay_aciklama,$sikayet_tarih){

	$result = mysql_query("INSERT INTO sikayet (kullanici_email,kullanici_id,sikayet_fotograf,sikayet_aciklama,sikayet_latitude,sikayet_longitude,sikayet_kategori_id,sikayet_onay,sikayet_onay_aciklama,sikayet_tarih) VALUES ('$kullanici_email','$kullanici_id','$sikayet_fotograf','$sikayet_aciklama','$sikayet_latitude','$sikayet_longitude','$sikayet_kategori_id', '$sikayet_onay','$sikayet_onay_aciklama','$sikayet_tarih')") or die(mysql_error());


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

    public function getMyComplaints($mail){
        
        $result =  mysql_query("SELECT * FROM sikayet WHERE kullanici_email = '$mail'") or die(mysql_error());
        $query = mysql_num_rows($result);
        
        if($result && $query > 0){
            return $result;
        }
        else{
            return false;
        }     
    }
    public function getApprovedComplaints($onay){
        
        $result =  mysql_query("SELECT * FROM sikayet WHERE sikayet_onay = '$onay'") or die(mysql_error());
        $query = mysql_num_rows($result);
        
        if($result && $query > 0){
            return $result;
        }
        else{
            return false;
        }     
    }
    //NEW METHODS*****************************************************************************************************
    //ADDED IN 21.12.2014
    public function getWaitingComplaints(){
        
        $result =  mysql_query("SELECT * FROM sikayet WHERE sikayet_onay = 'waiting'") or die(mysql_error());
        $query = mysql_num_rows($result);
        
        if($result && $query > 0){
            return $result;
        }
        else{
            return false;
        }     
    }
    //ADDED IN 21.12.2014
    public function updateCompaintStatus($idsikayet, $sikayet_onay, $sikayet_onay_aciklama){
        
        $result = mysql_query("UPDATE sikayet SET sikayet_onay = '$sikayet_onay' , sikayet_onay_aciklama = '$sikayet_onay_aciklama' 
                    WHERE idsikayet = '$idsikayet'");
        if($result){
            return true;
        }
        else{
            return false;
        }     
    }
    public function updateUserPassword($kullanici_id, $kullanici_mail, $kullanici_yeni_sifre){
        
        $result = mysql_query("UPDATE kullanici_bilgileri SET kullanici_sifre = '$kullanici_yeni_sifre' 
                    WHERE kullanici_id = '$kullanici_id' and kullanici_mail = '$kullanici_mail'");
        if($result){
            return true;
        }
        else{
            return false;
        }     
    }
    public function updateUserInfo($kullanici_id, $kullanici_mail, $kullanici_telefon){
        
        $result = mysql_query("UPDATE kullanici_bilgileri SET kullanici_telefon = '$kullanici_telefon' 
                    WHERE kullanici_id = '$kullanici_id' and kullanici_mail = '$kullanici_mail'");
        if($result){
            return true;
        }
        else{
            return false;
        }     
    }   
    public function getComplaintsViaCategory($sikayet_kategori_id){
        
        $result =  mysql_query("SELECT * FROM sikayet WHERE sikayet_kategori_id = '$sikayet_kategori_id' and sikayet_onay = 'onaylandi'") or die(mysql_error());
        $query = mysql_num_rows($result);
        
        if($result && $query > 0){
            return $result;
        }
        else{
            return false;
        }     
    }
	public function resetPassword($kul_email){

		$check = mysql_query("SELECT kullanici_sifre FROM kullanici_bilgileri WHERE kullanici_mail = '$kul_email' ")or 		die(mysql_error());
        $row = mysql_fetch_array($check);
        $sifre = $row["kullanici_sifre"];

		require_once("class.phpmailer.php");


		// echo $name,$email,$message;
	# PHPMailer class tanımla
		$mail = new PHPMailer();
		$mail->CharSet="utf-8";
	// Classa SMTP başlat
		$mail->IsSMTP();

	// Test / Gerçek işlem
		$mail->SMTPDebug = 0;

	// SMTP Authentication aktif et
		$mail->SMTPAuth = true;

	//SMTP Server
		$mail->Host = "webmail.safakli.com";

	// Port SMTP Sunucu 25 / 587
		$mail->Port = 587;

	// SMTP kullanıcı adı
		$mail->Username = "citydetective@safakli.com";

	// SMTP kullanıcı şifre
		$mail->Password = "sw_cd_123";

	// gönderen hesap
		$mail->SetFrom("citydetective@safakli.com", "City's Detective");

	// Mail konusu
		$mail->Subject = "City's Detective Giriş Şifreniz";

	// Mail içeriği
		$body = "<p><h3>City's Detective Şifre Yenileme İsteği Gönderdiniz,Şifreniz:".$sifre."</h3></p></br>";
		$mail->MsgHTML($body);

	// hedef adresi ekle
		$to = $kul_email;
		$mail->AddAddress($to, "City's Detective");
	// Maili gönder
		if(!$mail->Send())
		{
			echo "Mailer Hata: " . $mail->ErrorInfo;
			return false;
			//echo "<script>window.location = './iletisim.php';</script>";
		}
		else
		{
			return true;
		}		
	}
    //*****************************************************************************************************************
	
}
?>
