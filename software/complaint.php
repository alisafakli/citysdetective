<?php
require_once 'Db_Functions_cd.php';
$db = new Db_Functions_cd();
error_reporting(0);
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>City's Detective</title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="container">

    <ul class="nav nav-pills">
        <li role="presentation" class="active"><a href="complaint.php">Şikayetler</a></li>
        <li role="presentation" ><a href="verified_complaint.php">Onaylanmış Şikayetler</a></li>

        <li role="presentation"><a href="users.php">Kullanıcılar</a></li>
        <li role="presentation" ><a href="push.php">Push Notifiaction</a></li>

    </ul>

    <?php
    if($_GET['red_id']){
        $red_id = $_GET['red_id'];
        $result = mysql_query("UPDATE sikayet SET sikayet_onay='red' WHERE idsikayet='$red_id' ") or die(mysql_error());
        if($result){
            echo "<div class=\"alert alert-danger\" role=\"alert\">Şikayet Reddedildi</div>";
            echo " <script type=\"text/JavaScript\">
        setTimeout(\"location.href = 'complaint.php';\",1500);
        </script>";
        } else {
            echo "<div class=\"alert alert-danger\" role=\"alert\">Hata Oluştu !! </div>";
        }
    }
    if($_GET['id']){
        $onay_id = $_GET['id'];
        $result = mysql_query("UPDATE sikayet SET sikayet_onay='onaylandi' WHERE idsikayet='$onay_id' ") or die(mysql_error());
        if($result){
            error_reporting(0);
            require_once("class.phpmailer.php");


            if(true){

                $kategori_type = null;
                $result = mysql_query("SELECT * FROM sikayet WHERE idsikayet='$onay_id'") or die(mysql_error());
                echo "<br><table class='table'>";
                echo "<tr><td>Şikayet ID</td><td>Fotoğraf</td><td>Açıklama</td><td>Email</td><td>Kategori</td><td>Tarih</td><td>Onay</td><td>Red</td></tr>";
                while($row = mysql_fetch_array($result))
                {
                    switch ($row['sikayet_kategori_id']){
                        case 0:
                            $kategori_type = "Trafik";
                            break;
                        case 1:
                            $kategori_type = "Işıklandırma";
                            break;
                        case 2:
                            $kategori_type = "Atık";
                            break;
                        case 3:
                            $kategori_type = "Sokak Hayvanları";
                            break;
                        case 4:
                            $kategori_type = "Engelli Hakları";
                            break;

                        default:
                            $kategori_type = "Diğer";
                    }




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
                $mail->Host = "cpanel4.webadam.com";

                // Port SMTP Sunucu 25 / 587
                $mail->Port = 587;

                // SMTP kullanıcı adı
                $mail->Username = "info@buyabilisim.com";

                // SMTP kullanıcı şifre
                $mail->Password = "info26buya";

                // gönderen hesap
                $mail->SetFrom("info@buyabilisim.com", "City's Detective");

                // Mail konusu
                $mail->Subject = "City's Detective";
                $message = "<html><head></head><body>";
                $message .= "<h2><p>Sikayet Fotoğrafı</p></h2>";

                $message .= "<img src=\"http://citydetective.safakli.com/upload2/uploads/".$row['sikayet_fotograf']."\"alt='' /></body></html>";
                $message .= "<h2><p>Sikayet Kategorisi</p></h2>";
                $message .= $kategori_type;
                $message .= "<h2><p>Sikayet Açıklama</p></h2>";
                $message .= $row['sikayet_aciklama'];
                $message .= "<h2><p>Sikayet Harita</p></h2>";
                //$message .= "<img src=\"https://maps.googleapis.com/maps/api/staticmap?center=".$row['sikayet_latitude'].",".$row['sikayet_longitude']."&zoom=17&size=400x400\"alt='' /></body></html>";
                $message .= "<img src=\"https://maps.googleapis.com/maps/api/staticmap?zoom=17&size=400x400&sensor=false&maptype=roadmap&markers=color:red|".$row['sikayet_latitude'].",".$row['sikayet_longitude']."\"alt='' /></body></html>";

                $message .= "<h2><p>Sikayetçi Email</p></h2>";
                $message .= $row['kullanici_email'];
                $message .= "<h2><p>Tarih</p></h2>";
                $message .= $row['sikayet_tarih'];




                // Mail içeriği
                //$body = "<p><h3>Ad-Soyad: </h3>".$name."<br><h3>E-mail: </h3>".$email."<br><h3>Mesaj: </h3>".$message;
                $mail->MsgHTML($message);

                // hedef adresi ekle
                $to = "emreduman16@gmail.com";
                $mail->AddAddress($to, "SMTP Test");
                // Maili gönder
                if(!$mail->Send())
                {
                    echo "Mailer Hata: " . $mail->ErrorInfo;
                    echo "<script>window.location = './complaint.php';</script>";
                }
                else
                {
                    //echo "Mesaj başarıyla gönderildi!";



                }}
            }else{
                //echo "<script>window.location = './iletisim.php';</script>";
            }

            echo "<div class=\"alert alert-success\" role=\"alert\">Şikayet Onaylandı ve Mail Gönderildi</div>";
            echo " <script type=\"text/JavaScript\">
        setTimeout(\"location.href = 'complaint.php';\",1500);
        </script>";
        } else {
            echo "<div class=\"alert alert-danger\" role=\"alert\">Hata Oluştu !! </div>";
        }
    }

    $kategori_type = null;
    $result = mysql_query("SELECT * FROM sikayet WHERE sikayet_onay='waiting'") or die(mysql_error());
    echo "<br><table class='table'>";
    echo "<tr><td>Şikayet ID</td><td>Fotoğraf</td><td>Açıklama</td><td>Email</td><td>Kategori</td><td>Tarih</td><td>Onay</td><td>Red</td></tr>";
    while($row = mysql_fetch_array($result))
    {
        switch ($row['sikayet_kategori_id']){
            case 0:
                $kategori_type = "Trafik";
                break;
            case 1:
                $kategori_type = "Işıklandırma";
                break;
            case 2:
                $kategori_type = "Atık";
                break;
            case 3:
                $kategori_type = "Sokak Hayvanları";
                break;
            case 4:
                $kategori_type = "Engelli Hakları";
                break;

            default:
                $kategori_type = "Diğer";
        }
        echo "<tr><td>".$row['idsikayet']."</td><td><img width=\"200px\" height=\"300px\" src=\"upload2/uploads/".$row['sikayet_fotograf']."\"></td><td>".$row['sikayet_aciklama']."</td><td>".$row['kullanici_email']."</td><td>".$kategori_type."</td><td>".$row['sikayet_tarih']."</td>
            <td><a  class=\"btn btn-success\" href=\"complaint.php?id=".$row['idsikayet']."\">Onayla</a></td><td><a  class=\"btn btn-danger\" href=\"complaint.php?red_id=".$row['idsikayet']."\">Reddet</a></td></tr>";


    }
    echo "</table>";
    ?>



</div>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>
</body>
</html>