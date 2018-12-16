<?php

	require_once(__DIR__ . '/../Util/DBConnection.php');

	$data = json_decode(file_get_contents('php://input'), true);
	
	if(isset($data['data_inicial']) && isset($data['data_final']) && isset($data['id_leitura'])) {
		$dataInicial = $data['data_inicial'];
		$dataFinal = $data['data_final'];
		$idLeitura = $data['id_leitura']; 
	}

	$conn = getConn();

  	$sql = 'SELECT e.nome as estacao_nome, s.nome as sensor_nome, l.nome as leitura_nome, h.valor, l.unidadeMedida, h.data, l.ativo ' .
											'FROM historico h ' .
											'JOIN leitura l ' .
											'ON l.id = h.idLeitura ' .
											'JOIN estacao e ' .
											'ON e.id = l.idEstacao ' .
											'JOIN sensor s ' .
											'ON s.id = l.idSensor ' .
											'WHERE h.idLeitura = :idLeitura ' .
											'AND h.data >= :dataInicial ' .
											'AND h.data <= :dataFinal';
  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("idLeitura",$idLeitura);
  	$stmt->bindParam("dataInicial",$dataInicial);
  	$stmt->bindParam("dataFinal",$dataFinal);
  	$stmt->execute();
  	$result = $stmt->fetchAll(PDO::FETCH_ASSOC);

  	header('Content-Type: application/json');
  	echo json_encode($result);

?>