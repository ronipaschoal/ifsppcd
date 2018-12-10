<?php

	require_once(__DIR__ . '/../Util/DBConnection.php');

	$data = json_decode(file_get_contents('php://input'), true);
	
	if(isset($data['id_leitura']) && isset($data['ativo'])) {
		$idLeitura = $data['id_leitura'];	
		$ativo = $data['ativo'];		 
	}

	$conn = getConn();

  	$sql = 'update leitura set ativo = :ativo where id = :id_leitura';
  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("ativo",$ativo);
  	$stmt->bindParam("id_leitura",$idLeitura);
  	$stmt->execute();
  	$result = $stmt->fetchAll(PDO::FETCH_ASSOC);

  	
  	header('Content-Type: application/json');
  	echo json_encode($result);		

?>

