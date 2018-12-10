<?php

	require_once(__DIR__ . '/../Util/DBConnection.php');

	$data = json_decode(file_get_contents('php://input'), true);
	
	if(isset($data['id_leitura'])) {
		$idLeitura = $data['id_leitura'];			 
	}

	$conn = getConn();

  	$sql =  'SELECT t.*, l.unidadeMedida ' .
			'FROM temporeal t ' .
			'inner join leitura l ' .
			'on (l.id = t.idLeitura) ' .
			'WHERE l.id = :idLeitura' ;

  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("idLeitura", $idLeitura);
  	$stmt->execute();
  	$result = $stmt->fetchAll(PDO::FETCH_ASSOC);

  	
  	header('Content-Type: application/json');
  	echo json_encode($result);		

?>

