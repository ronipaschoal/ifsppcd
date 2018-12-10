<?php

	require_once(__DIR__ . '/../Util/DBConnection.php');

	$data = json_decode(file_get_contents('php://input'), true);
	
	if(isset($data['id_leitura'])) {
		$idLeitura = $data['id_leitura'];			 
	}

	$conn = getConn();

  	$sql =  'SELECT h.*, l.unidadeMedida ' .
			'FROM historico h ' .
			'inner join leitura l ' .
			'on (l.id = h.idLeitura) ' .
			'WHERE l.id = :idLeitura' ;

  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("idLeitura", $idLeitura);
  	$stmt->execute();
  	$result = $stmt->fetchAll(PDO::FETCH_ASSOC);

  	
  	header('Content-Type: application/json');
  	echo json_encode($result);		

?>

