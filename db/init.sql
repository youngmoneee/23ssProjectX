DROP DATABASE IF EXISTS px;
CREATE DATABASE px default character set utf8 collate utf8_general_ci;

USE px;
CREATE USER IF NOT EXISTS 'dev'@'localhost' IDENTIFIED BY 'dev';
GRANT ALL PRIVILEGES ON px.* TO 'dev'@'localhost';

FLUSH PRIVILEGES ;
CREATE TABLE IF NOT EXISTS 상품(
  상품코드 INT AUTO_INCREMENT,
  상품명 VARCHAR(255) NOT NULL,
  가격 INT NOT NULL,
  재고수량 INT NOT NULL,
  PRIMARY KEY (상품코드)
);
CREATE TABLE IF NOT EXISTS 상품주문(
  주문번호 INT PRIMARY KEY AUTO_INCREMENT,
  상품코드 INT NOT NULL,
  주문수량 INT NOT NULL,
  FOREIGN KEY (상품코드) REFERENCES 상품(상품코드)
);
INSERT INTO 상품(상품명, 가격, 재고수량) VALUES ('샘플데이터', 42, 42);
INSERT INTO 상품주문(상품코드, 주문수량) VALUES (1, 42);
FLUSH PRIVILEGES ;