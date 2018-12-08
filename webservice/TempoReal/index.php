<?php

ob_start();
require_once(__DIR__ . '/../Util/Database.php');

$postdata = file_get_contents("php://input");

if(isset($postdata) && !empty($postdata)){
    $request = json_decode($postdata);
	
	if(isset($request) && !empty($request)){
		$DBFactory = new Database();
		$DBFactory->openConnection();	
		
		$result = $DBFactory->executeQuery("SELECT id FROM leitura WHERE codigo = '" . $request->leitura->codigo . "'");
		
		if(!is_null($result)){
			$idLeitura = $result[0]["id"];
			
			$result = $DBFactory->executeQuery("SELECT COUNT(*) numReg FROM temporeal WHERE idLeitura = '" . $idLeitura . "'");
			
			if(!is_null($result)){
				if($result[0]["numReg"] > 0) {
					$result = $DBFactory->executeCommand("UPDATE temporeal SET valor = '" . str_replace(',', '.', $request->leitura->valor) . "', data = NOW() WHERE idLeitura = '" . $idLeitura . "'"); 
				} else {
					$result = $DBFactory->executeCommand("INSERT INTO temporeal(idLeitura, valor, data) VALUES('" . $idLeitura . "', '" . str_replace(',', '.', $request->leitura->valor) . "', NOW())"); 
				}
			}
		}
		
		$DBFactory->closeConnection();
	}
}

?>

