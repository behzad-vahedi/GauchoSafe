
<?php
$servername = "127.0.0.1";
$username = "root";
$password = "salam_dada";

// Create connection
$conn = new mysqli($servername, $username, $password);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 
echo "Connected successfully";


$sql = "SELECT * FROM table_locs";
$result = $conn->query($sql);

echo $result;

?>