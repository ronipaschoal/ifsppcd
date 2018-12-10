<?php
	// Conexão PDO com o banco de dados
	function getConn()
	{
	 return new PDO('mysql:host=localhost;dbname=estacaometeriologicapdm',
	  'root',
	  '1234',
	  array(PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES utf8")
	  
	  );
	}
	$conn = getConn();
	
  	$sql = 'SELECT * from estacao';
  	$stmt = $conn->prepare($sql);
  	$stmt->execute();
  	$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
  	
  	header('Content-Type: application/json');
  	echo json_encode($result);		
?>