<?php
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type");
include 'db.php';

$data = json_decode(file_get_contents("php://input"));

if (isset($data->id) && isset($data->customer_name) && isset($data->phone_number) && isset($data->total_price) && isset($data->status)) {
    $id            = $conn->real_escape_string($data->id);
    $customer_name = $conn->real_escape_string($data->customer_name);
    $phone_number  = $conn->real_escape_string($data->phone_number);
    $total_price   = $conn->real_escape_string($data->total_price);
    $status        = $conn->real_escape_string($data->status);

    $sql = "UPDATE `orders`
            SET customer_name='$customer_name', phone_number='$phone_number', total_price='$total_price', status='$status'
            WHERE id=$id";

    if ($conn->query($sql) === TRUE) {
        echo json_encode(["message" => "Order updated successfully"]);
    } else {
        echo json_encode(["message" => "Error updating record: " . $conn->error]);
    }
} else {
    echo json_encode(["message" => "Incomplete data"]);
}
$conn->close();
?>
