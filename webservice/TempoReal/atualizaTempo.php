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

	$data = json_decode(file_get_contents('php://input'), true);
	
	if(isset($data['id_estacao']) && isset($data['tempo_medicao'])) {
		$idEstacao = $data['id_estacao'];	
		$tempoMedicao = $data['tempo_medicao'];			 
	}

	$conn = getConn();

  	$sql = 'update estacao set tempoLeitura = :tempo_medicao where id = :id_estacao';
  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("tempo_medicao",$tempoMedicao);
  	$stmt->bindParam("id_estacao",$idEstacao);
  	$stmt->execute();
  	$result = $stmt->fetchAll(PDO::FETCH_ASSOC);

  	
  	header('Content-Type: application/json');
  	echo json_encode($result);		

?>

