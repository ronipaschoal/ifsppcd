<?php

ob_start();
require_once(__DIR__ . '/../Util/DBConnection.php');
require_once(__DIR__ . '/../Util/Database.php');

$postdata = file_get_contents("php://input");

$debug = false;

if((isset($postdata) && !empty($postdata)) OR $debug){
    $request = json_decode($postdata);

	if((isset($request) && !empty($request)) OR $debug){

		if (!$debug) {
			$codigoNovaLeitura = $request->leitura->codigo;
			$valorNovaLeitura = str_replace(',', '.', $request->leitura->valor);
		} else {
			$codigoNovaLeitura = 'MOV-03';
			$valorNovaLeitura = 'teste';
		}

		date_default_timezone_set('America/Sao_Paulo');
		$dataAtual = date('Y-m-d H:i:s');

		$DBFactory = new Database();
		$DBFactory->openConnection();
		
		$result = $DBFactory->executeQuery("SELECT id, idEstacao, ativo FROM leitura WHERE codigo = '" . $codigoNovaLeitura . "'");
		
		if(!is_null($result)){

			$idLeitura = $result[0]["id"];
			$sensorAtivo = $result[0]["ativo"];
			$idEstacao = $result[0]["idEstacao"];

			$result = $DBFactory->executeQuery("SELECT tempoLeitura, ativo FROM estacao WHERE id = '" . $idEstacao . "'");

			$tempoLeitura = $result[0]["tempoLeitura"] / 1000;
			$estacaoAtiva = $result[0]["ativo"];

			if ($sensorAtivo && $estacaoAtiva) {

				
				$result = $DBFactory->executeQuery("SELECT COUNT(*) numReg FROM temporeal WHERE idLeitura = '" . $idLeitura . "'");
				$registers = $result[0]["numReg"];

				if(!is_null($result)){

					$conn = getConn();
					
					if($registers > 0) {

						$result = $DBFactory->executeQuery("SELECT * FROM temporeal WHERE idLeitura = '" . $idLeitura . "'");

						if((strtotime($dataAtual) - strtotime($result[0]["data"])) > $tempoLeitura) {

							$sql = 'INSERT INTO historico (idLeitura, valor, data) VALUES (:idLeitura, :valor, :data)';
							$stmt = $conn->prepare($sql);
							$stmt->bindParam("idLeitura", $result[0]["idLeitura"]);
							$stmt->bindParam("valor", $result[0]["valor"]);
							$stmt->bindParam("data", $result[0]["data"]);
							$stmt->execute();
							$stmt->fetchAll(PDO::FETCH_ASSOC);

							$DBFactory->executeCommand("UPDATE temporeal SET valor = '" . $valorNovaLeitura . "', data = '". $dataAtual ."' WHERE id = '" . $result[0]["id"] . "'");
						}

					} else {

						$sql = 'INSERT INTO temporeal (idLeitura, valor, data) VALUES (:idLeitura, :valor, :data)';
						$stmt = $conn->prepare($sql);
						$stmt->bindParam("idLeitura",$idLeitura);
						$stmt->bindParam("valor",$valorNovaLeitura);
						$stmt->bindParam("data",$dataAtual);
						$stmt->execute();
						$stmt->fetchAll(PDO::FETCH_ASSOC);
					
					}
				}
			}
		}
		
		$DBFactory->closeConnection();
	}

}