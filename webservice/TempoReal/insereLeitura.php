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
	
	date_default_timezone_set('America/Sao_Paulo');
	$dataAtual = date('Y-m-d H:i:s');

	$data = json_decode(file_get_contents('php://input'), true);
	
	if(isset($data['leitura']) && isset($data['id_sensor']) && isset($data['id_estacao'])) {
		$leitura = $data['leitura'];	
		$idSensor = $data['id_sensor'];
		$idEstacao = $data['id_estacao'];		 
	}

	$conn = getConn();

  	$sql = 'SELECT h.idLeitura, h.data, e.tempoLeitura
					FROM historico h
					INNER JOIN leitura l ON l.id = h.idLeitura
					INNER JOIN estacao e ON e.id = l.idEstacao
					WHERE l.idSensor = :idSensor
					AND l.idEstacao = :idEstacao
					AND l.ativo = 1
					ORDER BY h.id DESC LIMIT 1';
					
  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("idSensor",$idSensor);
	$stmt->bindParam("idEstacao",$idEstacao);
  	$stmt->execute();
  	$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
	
	foreach($result as $item){
	   $idLeitura = $item["idLeitura"];
	   $dataAntiga = $item["data"];
	   $tempoLeitura = $item["tempoLeitura"];
	}

	
	$difData = strtotime($dataAtual) - strtotime($dataAntiga);
	
	if($difData > $tempoLeitura){
		$sql = 'INSERT INTO historico (idLeitura, valor, data) VALUES (:idLeitura, :valor, :data)';		
		$stmt = $conn->prepare($sql);
		$stmt->bindParam("idLeitura",$idLeitura);
		$stmt->bindParam("valor",$leitura);
		$stmt->bindParam("data",$dataAtual);
		$stmt->execute();
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
	}
	
	$sql = 'INSERT INTO temporeal (idLeitura, valor, data) VALUES (:idLeitura, :valor, :data)';
	$stmt = $conn->prepare($sql);
	$stmt->bindParam("idLeitura",$idLeitura);
	$stmt->bindParam("valor",$leitura);
	$stmt->bindParam("data",$dataAtual);
	$stmt->execute();
	$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
	
?>

