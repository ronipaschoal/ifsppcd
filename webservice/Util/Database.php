<?php

class Database
{
	private $conn;
	private $host;
	private $user;
	private $password;
	private $database;
	
	public function __construct()
	{		
		$this->host = "127.0.0.1";
	  	$this->user = "root";
	   	$this->password = "1234";
	  	$this->database = "estacaometeriologicapdm";
	}
	
	public function openConnection()
	{
		$this->conn = new mysqli($this->host, $this->user, $this->password, $this->database);
		
		if($this->conn->connect_errno)
			throw new Exception("Falha ao conectar com o banco de dados." . $this->conn->connect_error);
    }
	
	public function executeCommand($sqlCommand)
	{		
		$query = $this->conn->query($sqlCommand);
		
		if($query)
		{
			if($this->conn->affected_rows > 0)
				return $query;
			else
				return false;
		}
	}
	
	public function executeQuery($sqlQuery)
	{		
		$query = $this->executeCommand($sqlQuery);

		if($query)
		{
			$result = array();
			
			while($reader = $query->fetch_array())
				array_push($result, $reader);
			
			if(!$result)
				return null;
			else
				return $result;
		}
	}
	
	public function getLastIdentity()
	{
		return $this->conn->insert_id;
	}
	
	public function closeConnection()
	{
		mysqli_close($this->conn);			
	}
	
	public function beginTransaction()
	{
		return mysqli_autocommit($this->conn, FALSE);
	}
	
	public function commitTransaction()
	{
		return mysqli_commit($this->conn);
	}
	
	public function rollbackTransaction()
	{
		return mysqli_rollback($this->conn);
	}

			
}
 
?>
