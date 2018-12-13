<?php

ob_start();
require_once(__DIR__ . '/../Util/Database.php');

$postdata = file_get_contents("php://input");

if(isset($postdata) && !empty($postdata)){
    $request = json_decode($postdata);
	
	if(isset($request) && !empty($request)){
		$DBFactory = new Database();
		$DBFactory->openConnection();	
		
		$result = $DBFactory->executeQuery("SELECT id, idEstacao FROM leitura WHERE codigo = '" . $request->leitura->codigo . "'");
		
		if(!is_null($result)){
			$idLeitura = $result[0]["id"];
			$idEstacao = $result[0]["idEstacao"];
			
			$result = $DBFactory->executeQuery("SELECT COUNT(*) numReg, max(data) as data FROM temporeal WHERE idLeitura = '" . $idLeitura . "'");
			
			if(!is_null($result)){

				date_default_timezone_set('America/Sao_Paulo');
				$dataAtual = date('Y-m-d H:i:s');

				$difData = strtotime($dataAtual) - strtotime($result[0]["data"]);
	
				$result = $DBFactory->executeQuery("SELECT tempoLeitura FROM estacao WHERE id = '" . $idEstacao . "'");

				if($difData > $result['tempoLeitura']){

					if($result[0]["numReg"] > 0) {
						$result = $DBFactory->executeCommand("UPDATE temporeal SET valor = '" . str_replace(',', '.', $request->leitura->valor) . "', data = NOW() WHERE idLeitura = '" . $idLeitura . "'"); 
					} else {
						$result = $DBFactory->executeCommand("INSERT INTO temporeal(idLeitura, valor, data) VALUES('" . $idLeitura . "', '" . str_replace(',', '.', $request->leitura->valor) . "', NOW())"); 
					}

					$result = $DBFactory->executeCommand("INSERT INTO historico(idLeitura, valor, data) VALUES('" . $idLeitura . "', '" . str_replace(',', '.', $request->leitura->valor) . "', NOW())"); 
				}
			}
		}
		
		$DBFactory->closeConnection();
	}
}

?>

