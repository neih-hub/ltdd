CREATE DATABASE IF NOT EXISTS OrderDB
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE OrderDB;

CREATE TABLE IF NOT EXISTS `Order` (
    id            INT          NOT NULL AUTO_INCREMENT,
    customer_name VARCHAR(100) NOT NULL,
    phone_number  VARCHAR(20)  NOT NULL,
    total_price   DOUBLE       NOT NULL DEFAULT 0,
    status        VARCHAR(50)  NOT NULL DEFAULT 'Pending',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `Order` (customer_name, phone_number, total_price, status) VALUES
    ('Nguyễn Văn A', '0123456789', 500000, 'Pending'),
    ('Trần Thị B',   '0987654321', 1200000, 'Completed'),
    ('Lê Văn C',     '0911223344', 350000, 'Cancelled');
