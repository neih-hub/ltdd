<?php
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type");
include 'db.php';

$data = json_decode(file_get_contents("php://input"));

if (isset($data->id)) {
    $id  = $conn->real_escape_string($data->id);
    $sql = "DELETE FROM `orders` WHERE id=$id";

    if ($conn->query($sql) === TRUE) {
        echo json_encode(["message" => "Order deleted successfully"]);
    } else {
        echo json_encode(["message" => "Error deleting record: " . $conn->error]);
    }
} else {
    echo json_encode(["message" => "Incomplete data"]);
}
$conn->close();
?>
