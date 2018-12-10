<?php

	require_once(__DIR__ . '/../Util/DBConnection.php');

	$data = json_decode(file_get_contents('php://input'), true);
	
	if(isset($data['id_estacao']) && isset($data['tempo_medicao']) && isset($data['ativo'])) {
		$idEstacao = $data['id_estacao'];	
		$tempoMedicao = $data['tempo_medicao'];	
		$ativo = $data['ativo'];		 
	}

	$conn = getConn();

  	$sql = 'update estacao set tempoLeitura = :tempo_medicao, ativo = :ativo where id = :id_estacao';
  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("tempo_medicao",$tempoMedicao);
  	$stmt->bindParam("ativo",$ativo);
  	$stmt->bindParam("id_estacao",$idEstacao);
  	$stmt->execute();
  	$result = $stmt->fetchAll(PDO::FETCH_ASSOC);

  	
  	header('Content-Type: application/json');
  	echo json_encode($result);		

?>

