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

        <li role="presentation"><a href="users.php">Kullanıcılar</a></li>
        <li role="presentation" class="active"><a href="push.php">Push Notifiaction</a></li>

    </ul>

    <iframe src="http://citydetective.webatu.com/ws/index.php" width="1100" height="800" align="right"></iframe>


</div>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>
</body>
</html>