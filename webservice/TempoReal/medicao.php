<?php

	require_once(__DIR__ . '/../Util/DBConnection.php');

	$conn = getConn();
	
  	$sql = 'SELECT e.nome as estacao_nome, s.id as id_sensor, l.id as id_leitura, s.nome as sensor_nome, l.nome as leitura_nome, t.valor, l.unidadeMedida, t.data, l.ativo ' .
											'FROM temporeal t ' .
											'JOIN leitura l ' .
											'ON l.id = t.idLeitura ' .
											'JOIN estacao e ' .
											'ON e.id = l.idEstacao ' .
											'JOIN sensor s ' .
											'ON s.id = l.idSensor ';
  	$stmt = $conn->prepare($sql);
  	$stmt->execute();
  	$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
  	
  	header('Content-Type: application/json');
  	echo json_encode($result);		

?>

