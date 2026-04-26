<?php
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");
include 'db.php';

$sql = "SELECT * FROM `Order`";
$result = $conn->query($sql);

$orders = array();
if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $orders[] = $row;
    }
}
echo json_encode($orders);
$conn->close();
?>
