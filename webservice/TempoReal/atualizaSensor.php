<?php

	require_once(__DIR__ . '/../Util/DBConnection.php');

	$data = json_decode(file_get_contents('php://input'), true);
	
	if(isset($data['id_sensor']) && isset($data['ativo'])) {
		$idSensor = $data['id_sensor'];	
		$ativo = $data['ativo'];			 
	}

	$conn = getConn();

  	$sql = 'update leitura set ativo = :ativo where idSensor = :id_sensor';
  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("ativo",$ativo);
  	$stmt->bindParam("id_sensor",$idSensor);
  	$stmt->execute();
  	$result = $stmt->fetchAll(PDO::FETCH_ASSOC);

  	
  	header('Content-Type: application/json');
  	echo json_encode($result);		

?>

