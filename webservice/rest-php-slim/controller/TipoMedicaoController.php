<?php 

	function getAllTiposMedicao()
{  
	$stmt = getConn()->query("SELECT * FROM TIPO_MEDICAO");
  	$tipos = $stmt->fetchAll(PDO::FETCH_OBJ);
  	echo "{\"tipos_medicao\":".json_encode($tipos)."}";
}

function getTipoMedicao($id)
{  
	$conn = getConn();
  	$sql = "SELECT * FROM TIPO_MEDICAO WHERE id=:id";
  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("id",$id);
  	$stmt->execute();
  	$tipo = $stmt->fetchObject();
  	echo json_encode($tipo);
}

function setTipoMedicao()
{  
	$request = \Slim\Slim::getInstance()->request();
  	$tipo = json_decode($request->getBody());
  	$sql = "INSERT INTO TIPO_MEDICAO (UNIDADE_MEDIDA) values (:UNIDADE_MEDIDA) ";
  	$conn = getConn();
  	$stmt = $conn->prepare($sql);  	
  	$stmt->bindParam("UNIDADE_MEDIDA",$tipo->UNIDADE_MEDIDA);
  	$stmt->execute();
  	$tipo->id = $conn->lastInsertId();
  	echo json_encode($tipo);
}

function updateTipoMedicao($id)
{  
	$request = \Slim\Slim::getInstance()->request();
  	$tipo = json_decode($request->getBody());
  	$sql = "UPDATE TIPO_MEDICAO set UNIDADE_MEDIDA = :UNIDADE_MEDIDA WHERE ID = :ID ";
  	$conn = getConn();
  	$stmt = $conn->prepare($sql);  	
  	$stmt->bindParam("ID",$id);
  	$stmt->bindParam("UNIDADE_MEDIDA",$tipo->UNIDADE_MEDIDA);
  	$stmt->execute();
  	$tipo->id = $id;
  	echo json_encode($tipo);
}

function deleteTipoMedicao($id)
{  
	$sql = "DELETE FROM TIPO_MEDICAO WHERE ID=:ID";
  	$conn = getConn();
  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("ID",$id);
  	$stmt->execute();
  	echo "{'message':'Tipo de medicao apagada'}";
}
	
 ?>