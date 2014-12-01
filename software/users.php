<?php

?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Kullanıcılar</title>

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
<ul class="nav nav-pills" style="display: table;margin: 0 auto;">
    <li role="presentation" ><a href="anasayfa.php">Anasayfa</a></li>
    <li role="presentation"><a href="complaints.php">Onay Bekleyen Şikayetler</a></li>
    <li role="presentation" ><a href="all_complaints.php">Bütün Şikayetler</a></li>
    <li role="presentation" class="active"><a href="users.php">Kullanıcılar</a></li>

</ul>

    <table class="table table-striped">
        <?php
        require_once 'Db_Functions_cd.php';
        $db = new Db_Functions_cd();
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
        ?>
        <tr><td>Kullanıcı ID</td><td>Adı</td><td>Soyadı</td><td>Email</td><td>Telefon</td><td>İşlem</td></tr>
        <?php


        $db->getUsers();
        ?>
    </table>


</div>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>
</body>
</html>