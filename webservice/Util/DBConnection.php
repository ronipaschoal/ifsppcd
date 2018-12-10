<?php 

	// Conexão PDO com o banco de dados
	function getConn()
	{
	 return new PDO('mysql:host=localhost;dbname=estacaometeriologicapdm',
	  'root',
	  '1234',
	  array(PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES utf8")
	  
	  );
	}