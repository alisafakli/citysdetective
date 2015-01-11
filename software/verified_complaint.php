<?php
require_once 'Db_Functions_cd.php';
$db = new Db_Functions_cd();


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
        <li role="presentation" class="active"><a href="verified_complaint.php">Onaylanmış Şikayetler</a></li>
        <li role="presentation"><a href="users.php">Kullanıcılar</a></li>
        <li role="presentation" ><a href="push.php">Push Notifiaction</a></li>

    </ul>

    <?php


    $result = mysql_query("SELECT * FROM sikayet WHERE sikayet_onay='onaylandi'") or die(mysql_error());
    echo "<br><table class='table'>";
    echo "<tr><td>Şikayet ID</td><td>Fotoğraf</td><td>Açıklama</td><td>Email</td><td>Kategori</td><td>Tarih</td></tr>";
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
            </tr>";
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