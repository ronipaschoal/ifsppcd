<?php 

	function getAllSensores()
{  
	$stmt = getConn()->query("SELECT * FROM SENSOR");
  	$sensores = $stmt->fetchAll(PDO::FETCH_OBJ);
  	echo "{\"sensores\":".json_encode($sensores)."}";
}

function getSensor($id)
{  
	$conn = getConn();
  	$sql = "SELECT * FROM SENSOR WHERE id=:id";
  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("id",$id);
  	$stmt->execute();
  	$sensor = $stmt->fetchObject();
  	echo json_encode($sensor);
}
function setSensor()
{  
	$request = \Slim\Slim::getInstance()->request();
  	$sensor = json_decode($request->getBody());
  	$sql = "INSERT INTO SENSOR (ID,Nome) values (:ID,:Nome) ";
  	$conn = getConn();
  	$stmt = $conn->prepare($sql);  	
  	$stmt->bindParam("ID",$sensor->ID);
  	$stmt->bindParam("Nome",$sensor->Nome);
  	$stmt->execute();
  	$sensor->id = $conn->lastInsertId();
  	echo json_encode($sensor);
}


function updateSensor($id)
{  
	$request = \Slim\Slim::getInstance()->request();
  	$sensor = json_decode($request->getBody());
  	$sql = "UPDATE SENSOR set Nome = :Nome WHERE ID = :ID ";
  	$conn = getConn();
  	$stmt = $conn->prepare($sql);  	
  	$stmt->bindParam("ID",$id);
  	$stmt->bindParam("Nome",$sensor->Nome);
  	$stmt->execute();
  	$sensor->id = $id;
  	echo json_encode($sensor);
}

function deleteSensor($id)
{  
	$sql = "DELETE FROM SENSOR WHERE id=:id";
  	$conn = getConn();
  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("id",$id);
  	$stmt->execute();
  	echo "{'message':'Sensor apagado'}";
}
	
 ?>