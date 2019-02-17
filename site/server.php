<?php

$url = $_GET["url"];


$curlSession = curl_init();
curl_setopt($curlSession, CURLOPT_URL, 'http://34.197.30.158/webservice/TempoReal/' . $url . '.php');
curl_setopt($curlSession, CURLOPT_BINARYTRANSFER, true);
curl_setopt($curlSession, CURLOPT_RETURNTRANSFER, true);

$jsonData = curl_exec($curlSession);
print_r($jsonData);

curl_close($curlSession);
?>