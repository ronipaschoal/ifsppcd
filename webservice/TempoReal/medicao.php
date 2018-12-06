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
	
  	$sql = 'SELECT e.nome as estacao_nome, s.id as id_sensor, s.nome as sensor_nome, l.nome as leitura_nome, t.valor, l.unidadeMedida, t.data, l.ativo ' .
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

