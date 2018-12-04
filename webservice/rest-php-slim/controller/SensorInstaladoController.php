<?php 

	function getAllSensoresInstalados()
{  
	$stmt = getConn()->query("SELECT * FROM SENSOR_INSTALADO");
  	$sensores = $stmt->fetchAll(PDO::FETCH_OBJ);
  	echo "{\"sensores_instalados\":".json_encode($sensores)."}";
}

function getSensorInstalado($id)
{  
	$conn = getConn();
  	$sql = "SELECT * FROM SENSOR_INSTALADO WHERE ID=:ID";
  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("ID",$id);
  	$stmt->execute();
  	$sensor = $stmt->fetchObject();
  	echo json_encode($sensor);
}

function setSensorInstalado()
{  
	$request = \Slim\Slim::getInstance()->request();
  	$sensor = json_decode($request->getBody());
  	$sql = "INSERT INTO SENSOR_INSTALADO (ID_SENSOR, ID_TIPO_MEDICAO, ID_ESTACAO, ID_MEDICOES) values (:ID_SENSOR, :ID_TIPO_MEDICAO, :ID_ESTACAO, :ID_MEDICOES) ";
  	$conn = getConn();
  	$stmt = $conn->prepare($sql);  	
  	$stmt->bindParam("ID_SENSOR",$sensor->ID_SENSOR);
    $stmt->bindParam("ID_TIPO_MEDICAO",$sensor->ID_TIPO_MEDICAO);
    $stmt->bindParam("ID_ESTACAO",$sensor->ID_ESTACAO);
    $stmt->bindParam("ID_MEDICOES",$sensor->ID_MEDICOES);
  	$stmt->execute();
  	$sensor->id = $conn->lastInsertId();
  	echo json_encode($sensor);
}

function updateSensorInstalado($id)
{  
	$request = \Slim\Slim::getInstance()->request();
  	$sensor = json_decode($request->getBody());
  	$sql = "UPDATE SENSOR_INSTALADO set ID_SENSOR = :ID_SENSOR, ID_TIPO_MEDICAO = :ID_TIPO_MEDICAO, ID_ESTACAO = :ID_ESTACAO, ID_MEDICOES = :ID_MEDICOES WHERE ID = :ID ";
  	$conn = getConn();
  	$stmt = $conn->prepare($sql);  	
  	$stmt->bindParam("ID",$id);
    $stmt->bindParam("ID_SENSOR",$sensor->ID_SENSOR);
    $stmt->bindParam("ID_TIPO_MEDICAO",$sensor->ID_TIPO_MEDICAO);
    $stmt->bindParam("ID_ESTACAO",$sensor->ID_ESTACAO);
    $stmt->bindParam("ID_MEDICOES",$sensor->ID_MEDICOES);
  	$stmt->execute();
  	$sensor->id = $id;
  	echo json_encode($sensor);
}

function deleteSensorInstalado($id)
{  
	$sql = "DELETE FROM SENSOR_INSTALADO WHERE ID=:ID";
  	$conn = getConn();
  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("ID",$id);
  	$stmt->execute();
  	echo "{'message':'Sensor Instalado apagado'}";
}
	
 ?>