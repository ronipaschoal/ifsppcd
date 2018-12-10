<?php

	require_once(__DIR__ . '/../Util/DBConnection.php');

	$data = json_decode(file_get_contents('php://input'), true);
	
	if(isset($data['id_estacao'])) {
		$idEstacao = $data['id_estacao'];		 
	}

	$conn = getConn();

  	$sql = 'SELECT   e.id as id_estacao, ' .
		  			'e.nome as nome_estacao, ' .
		  			'e.tempoLeitura, ' .
		  			'e.ativo as estacao_ativo, ' .
		  			's.*, ' .
		  			'l.codigo as codigo, ' .
		  			'l.nome as leitura_nome, ' .
		  			'l.unidadeMedida, ' .
		  			'l.ativo as leitura_ativo ' .
		'FROM leitura l '  .											 
		'JOIN estacao e ' .
		'ON e.id = l.idEstacao ' .
		'JOIN sensor s ' .
		'ON s.id = l.idSensor ' .
		'WHERE e.id = :idEstacao' ;

		

  	$stmt = $conn->prepare($sql);
  	$stmt->bindParam("idEstacao",$idEstacao);
  	$stmt->execute();
  	$result = $stmt->fetchAll(PDO::FETCH_ASSOC);

  	$dados = array();

  	foreach ($result as $linha) {
  		$dados[] = array(
  			"id" => $linha["id_estacao"], 
  			"codigo" => $linha["codigo"],
  			"nome" => $linha["nome_estacao"],  			
  			"unidadeMedida" => $linha["unidadeMedida"],
  			"ativo" => $linha["leitura_ativo"],
  			"sensor" => array(
  				"id" => $linha["id"],
  				"nome" => $linha["nome"]
  			),
  			"estacao" => array(
  				"id" => $linha["id_estacao"],
  				"nome" => $linha["nome_estacao"],
  				"tempoLeitura" => $linha["tempoLeitura"],
  				"ativo" => $linha["estacao_ativo"]
  			)
  		);

  
  	} 
  	
  	header('Content-Type: application/json');
  	echo json_encode($dados);		

?>

