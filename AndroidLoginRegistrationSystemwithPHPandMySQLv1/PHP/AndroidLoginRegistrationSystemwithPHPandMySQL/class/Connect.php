<?php
	
	/**
	* Connect
	*/
	class Connect{

		private $conn;
		
		function __construct()
		{
			
		}

		function connect(){

			$this->conn = new PDO('mysql:host=localhost;dbname=api','root','');
			return $this->conn;
		}
	}
?>