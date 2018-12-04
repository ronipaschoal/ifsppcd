<?php 

	function getAllEstacoes()
{  
	$stmt = getConn()->query("SELECT * FROM ESTACAO_PCD");
  	$estacoes = $stmt->fetchAll(PDO::FETCH_OBJ);
  	echo "{\"estacoes_pcd\":".json_encode($estacoes)."}";
}

function getEstacao($id)
{  
	$conn = getConn();
  	$sql = "SELECT * FROM ESTACAO_PCD WHERE ID=:ID";
  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("ID",$id);
  	$stmt->execute();
  	$estacao = $stmt->fetchObject();
  	echo json_encode($estacao);
}

function setEstacao()
{  
	$request = \Slim\Slim::getInstance()->request();
  	$estacao = json_decode($request->getBody());
  	$sql = "INSERT INTO ESTACAO_PCD (TEMPO_MEDICAO, IP, PORTA, CHAVE_ACESSO) values (:TEMPO_MEDICAO, :IP, :PORTA, :CHAVE_ACESSO) ";
  	$conn = getConn();
  	$stmt = $conn->prepare($sql);  	
  	$stmt->bindParam("TEMPO_MEDICAO",$estacao->TEMPO_MEDICAO);
    $stmt->bindParam("IP",$estacao->IP);
    $stmt->bindParam("PORTA",$estacao->PORTA);
    $stmt->bindParam("CHAVE_ACESSO",$estacao->CHAVE_ACESSO);
  	$stmt->execute();
  	$estacao->id = $conn->lastInsertId();
  	echo json_encode($estacao);
}

function updateEstacao($id)
{  
	$request = \Slim\Slim::getInstance()->request();
  	$estacao = json_decode($request->getBody());
  	$sql = "UPDATE ESTACAO_PCD set TEMPO_MEDICAO = :TEMPO_MEDICAO, IP = :IP, PORTA = :PORTA, CHAVE_ACESSO = :CHAVE_ACESSO WHERE ID = :ID ";
  	$conn = getConn();
  	$stmt = $conn->prepare($sql);  	
  	$stmt->bindParam("ID",$id);
    $stmt->bindParam("TEMPO_MEDICAO",$estacao->TEMPO_MEDICAO);
    $stmt->bindParam("IP",$estacao->IP);
    $stmt->bindParam("PORTA",$estacao->PORTA);
    $stmt->bindParam("CHAVE_ACESSO",$estacao->CHAVE_ACESSO);
  	$stmt->execute();
  	$estacao->id = $id;
  	echo json_encode($estacao);
}

function deleteEstacao($id)
{  
	$sql = "DELETE FROM ESTACAO_PCD WHERE ID=:ID";
  	$conn = getConn();
  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("ID",$id);
  	$stmt->execute();
  	echo "{'message':'Estacao PCD apagada'}";
}
	
 ?>