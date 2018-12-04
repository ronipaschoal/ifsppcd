<?php 

	function getAllMedicoes()
{  
	$stmt = getConn()->query("SELECT * FROM MEDICOES");
  	$medicoes = $stmt->fetchAll(PDO::FETCH_OBJ);
  	echo "{\"medicoes\":".json_encode($medicoes)."}";
}

function getMedicao($id)
{  
	$conn = getConn();
  	$sql = "SELECT * FROM MEDICOES WHERE ID=:ID";
  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("ID",$id);
  	$stmt->execute();
  	$medicao = $stmt->fetchObject();
  	echo json_encode($medicao);
}

function setMedicao()
{  
	$request = \Slim\Slim::getInstance()->request();
  	$medicao = json_decode($request->getBody());
  	$sql = "INSERT INTO MEDICOES (Data, VALOR) values (:Data, :VALOR) ";
  	$conn = getConn();
  	$stmt = $conn->prepare($sql);  	
  	$stmt->bindParam("Data",$medicao->Data);
    $stmt->bindParam("VALOR",$medicao->VALOR);
  	$stmt->execute();
  	$medicao->id = $conn->lastInsertId();
  	echo json_encode($medicao);
}


function updateMedicao($id)
{  
	$request = \Slim\Slim::getInstance()->request();
  	$medicao = json_decode($request->getBody());
  	$sql = "UPDATE MEDICOES set Data = :Data, VALOR = :VALOR WHERE ID = :ID ";
  	$conn = getConn();
  	$stmt = $conn->prepare($sql);  	
  	$stmt->bindParam("ID",$id);
  	$stmt->bindParam("Data",$medicao->Data);
    $stmt->bindParam("VALOR",$medicao->VALOR);
  	$stmt->execute();
  	$medicao->id = $id;
  	echo json_encode($medicao);
}

function deleteMedicao($id)
{  
	$sql = "DELETE FROM MEDICOES WHERE ID=:ID";
  	$conn = getConn();
  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("ID",$id);
  	$stmt->execute();
  	echo "{'message':'Medicao apagada'}";
}
	
 ?>