-- ALEXSTYLE Manager - Script de base de datos (TP N°4)
-- Ejecutar en phpMyAdmin (XAMPP) o con: mysql -u root < script_base_datos.sql

CREATE DATABASE IF NOT EXISTS alexstyle_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE alexstyle_db;
SET NAMES utf8mb4;  -- evita problemas con tildes y "ñ" según el cliente que ejecute el script

-- Se borran en orden inverso a las dependencias (primero turnos, que referencia a las demás)
DROP TABLE IF EXISTS turnos;
DROP TABLE IF EXISTS inventario;
DROP TABLE IF EXISTS servicios;
DROP TABLE IF EXISTS barberos;
DROP TABLE IF EXISTS clientes;

CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre     VARCHAR(50)  NOT NULL,
    apellido   VARCHAR(50)  NOT NULL,
    dni        VARCHAR(8)   NOT NULL UNIQUE,
    telefono   VARCHAR(20)  NOT NULL,
    correo     VARCHAR(100)
);

CREATE TABLE barberos (
    id_barbero          INT AUTO_INCREMENT PRIMARY KEY,
    nombre              VARCHAR(50)  NOT NULL,
    apellido            VARCHAR(50)  NOT NULL,
    dni                 VARCHAR(8)   NOT NULL UNIQUE,
    telefono            VARCHAR(20)  NOT NULL,
    especialidad        VARCHAR(50),
    comision_porcentaje DECIMAL(5,2) NOT NULL DEFAULT 20.00
);

CREATE TABLE servicios (
    id_servicio      INT AUTO_INCREMENT PRIMARY KEY,
    nombre_servicio  VARCHAR(50)    NOT NULL,
    precio           DECIMAL(10,2)  NOT NULL,
    duracion_minutos INT            NOT NULL,
    tipo_servicio    VARCHAR(10)    NOT NULL DEFAULT 'CORTE',  -- CORTE | BARBA
    detalle          VARCHAR(50)    NOT NULL                   -- tipo de corte o estilo de barba
);

CREATE TABLE turnos (
    id_turno    INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente  INT      NOT NULL,
    id_barbero  INT      NOT NULL,
    id_servicio INT      NOT NULL,
    fecha_hora  DATETIME NOT NULL,
    estado      VARCHAR(20) NOT NULL DEFAULT 'Pendiente',
    FOREIGN KEY (id_cliente)  REFERENCES clientes(id_cliente),
    FOREIGN KEY (id_barbero)  REFERENCES barberos(id_barbero),
    FOREIGN KEY (id_servicio) REFERENCES servicios(id_servicio)
);

CREATE TABLE inventario (
    id_producto     INT AUTO_INCREMENT PRIMARY KEY,
    nombre_producto VARCHAR(50)   NOT NULL,
    stock           INT           NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL
);

-- Datos de ejemplo
INSERT INTO clientes (nombre, apellido, dni, telefono, correo) VALUES
    ('Martín', 'Gómez',  '40123456', '3794-111111', 'martin.gomez@mail.com'),
    ('Lucas',  'Pereyra','38987654', '3794-222222', NULL);

INSERT INTO barberos (nombre, apellido, dni, telefono, especialidad, comision_porcentaje) VALUES
    ('Alex',  'Style',  '35111222', '3794-333333', 'Degradados', 20.00),
    ('Nicolás','Ruiz',  '36222333', '3794-444444', 'Barba clásica', 25.00);

INSERT INTO servicios (nombre_servicio, precio, duracion_minutos, tipo_servicio, detalle) VALUES
    ('Corte Degradado', 8000.00, 45, 'CORTE', 'Degradado Completo'),
    ('Corte Clásico',   6000.00, 30, 'CORTE', 'Clásico'),
    ('Arreglo de Barba',4000.00, 30, 'BARBA', 'Perfilado Completo');

INSERT INTO inventario (nombre_producto, stock, precio_unitario) VALUES
    ('Cera para cabello', 12, 5500.00),
    ('Aceite para barba',  3, 7200.00),
    ('Navajas descartables', 40, 300.00);
