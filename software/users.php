<?php
require_once 'Db_Functions_cd.php';
$db = new Db_Functions_cd();
error_reporting(0)
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
        <li role="presentation" ><a href="complaint.php">Şikayetler</a></li>
        <li role="presentation" ><a href="verified_complaint.php">Onaylanmış Şikayetler</a></li>

        <li role="presentation" class="active"><a href="users.php">Kullanıcılar</a></li>
        <li role="presentation" ><a href="push.php">Push Notifiaction</a></li>

    </ul>


    <?php
    if($_GET['id']){
        $id = $_GET['id'];
        $result = mysql_query("DELETE FROM kullanici_bilgileri WHERE kullanici_id='$id' ") or die(mysql_error());
        if($result){
            echo "<div class=\"alert alert-danger\" role=\"alert\">Kullanıcı Silindi</div>";
            echo " <script type=\"text/JavaScript\">
        setTimeout(\"location.href = 'users.php';\",1500);
        </script>";
        } else {
            echo "<div class=\"alert alert-danger\" role=\"alert\">Hata Oluştu !! </div>";
        }
    }
    $result = mysql_query("SELECT * FROM kullanici_bilgileri WHERE 1") or die(mysql_error());
    echo "<br><table class='table'>";
    echo "<tr><td>Kullanıcı ID</td><td>Adı</td><td>Soyadı</td><td>Mail</td><td>Telefon</td><td>İşlem</td></tr>";
    while($row = mysql_fetch_array($result))
    {
        echo "<tr><td>".$row['kullanici_id']."</td><td>".$row['kullanici_adi']."</td><td>".$row['kullanici_soyadi']."</td><td>".$row['kullanici_mail']."</td><td>".$row['kullanici_telefon']."</td>
            <td><a  class=\"btn btn-danger\" href=\"users.php?id=".$row['kullanici_id']."\">Sil</a></td></tr>";
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