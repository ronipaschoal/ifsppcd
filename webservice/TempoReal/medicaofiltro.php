<?php

	require_once(__DIR__ . '/../Util/DBConnection.php');

	$data = json_decode(file_get_contents('php://input'), true);
	
	if(isset($data['data_inicial']) && isset($data['data_final']) && isset($data['id_sensor'])) {
		$dataInicial = $data['data_inicial'];	
		$dataFinal = $data['data_final'];	
		$idSensor = $data['id_sensor'];		 
	}

	$conn = getConn();

  	$sql = 'SELECT e.nome as estacao_nome, s.nome as sensor_nome, l.nome as leitura_nome, t.valor, l.unidadeMedida, t.data, l.ativo ' .
											'FROM temporeal t ' .
											'JOIN leitura l ' .
											'ON l.id = t.idLeitura ' .
											'JOIN estacao e ' .
											'ON e.id = l.idEstacao ' .
											'JOIN sensor s ' .
											'ON s.id = l.idSensor ' .
											'WHERE s.id = :idSensor ' .
											'AND t.data >= :dataInicial ' .
											'AND t.data <= :dataFinal';
  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("idSensor",$idSensor);
  	$stmt->bindParam("dataInicial",$dataInicial);
  	$stmt->bindParam("dataFinal",$dataFinal);
  	$stmt->execute();
  	$result = $stmt->fetchAll(PDO::FETCH_ASSOC);

  	
  	header('Content-Type: application/json');
  	echo json_encode($result);		

?>

