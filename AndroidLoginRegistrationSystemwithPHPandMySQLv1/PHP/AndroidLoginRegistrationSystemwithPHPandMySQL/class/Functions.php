<?php
	
	/**
	* Functions
	*/
	class Functions
	{
		private $conn;

		function __construct()
		{
			include 'Connect.php';;
			$database = new Connect();
			$this->conn = $database->connect();
		}

		/**
		 * User Authentication
		 * @param $email
		 * @param $password
		 * @return mixed
		 */
		public function Authentication($email,$password){

			$password = md5($password);

			$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
			$stmt = $this->conn->prepare($sql);
			$stmt->execute(array($email,$password));
			$row = $stmt->fetch();
			return $row;
		}

		/**
		 * User registration
		 */
		public function UserRegistration($fullname,$email,$password){

			$password = md5($password);

			$sql = "INSERT INTO users(fullname,email,password)VALUES (?,?,?)";
			$stmt = $this->conn->prepare($sql);
			$stmt->execute(array($fullname,$email,$password));
			$result = $stmt->fetch();
			return $result;
		}

		/**
		 * finding email duplication
		 */
		public function EmailDuplication($email){

			$sql = "SELECT * FROM users WHERE email = ?";
			$stmt = $this->conn->prepare($sql);
			$stmt->execute(array($email));
			$row = $stmt->fetch();
			return $row;
		}
	}
?>