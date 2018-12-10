<?php
	
	require_once(__DIR__ . '/../Util/DBConnection.php');
	
	$conn = getConn();
	
  	$sql = 'SELECT * from estacao';
  	$stmt = $conn->prepare($sql);
  	$stmt->execute();
  	$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
  	
  	header('Content-Type: application/json');
  	echo json_encode($result);		
?>