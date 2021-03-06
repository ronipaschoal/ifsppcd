<?php
	
	require_once(__DIR__ . '/../Util/DBConnection.php');
	
	$data = json_decode(file_get_contents('php://input'), true);
	
	if(isset($data['id_estacao'])) {			
		$idEstacao = $data['id_estacao'];		 
	}
	$conn = getConn();
  	$sql = 'SELECT e.* s.*, l.nome as leitura_nome, t.valor, l.unidadeMedida, t.data, l.ativo ' .
											'FROM temporeal t ' .
											'JOIN leitura l ' .
											'ON l.id = t.idLeitura ' .
											'JOIN estacao e ' .
											'ON e.id = l.idEstacao ' .
											'JOIN sensor s ' .
											'ON s.id = l.idSensor ' ;
											'WHERE e.id = :idEstacao ' ;
  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("idEstacao",$idEstacao);
  	$stmt->execute();
  	$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
  	
  	header('Content-Type: application/json');
  	echo json_encode($result);		
?>
