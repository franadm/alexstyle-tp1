-- Creación de la Base de Datos
CREATE DATABASE IF NOT EXISTS alexstyle_db;
USE alexstyle_db;

-- Tabla de Clientes
CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    correo VARCHAR(100)
);

-- Tabla de Barberos / Personal
CREATE TABLE barberos (
    id_barbero INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    especialidad VARCHAR(50)
);

-- Tabla de Servicios (Corte, Barba, etc.)
CREATE TABLE servicios (
    id_servicio INT AUTO_INCREMENT PRIMARY KEY,
    nombre_servicio VARCHAR(50) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    duracion_minutos INT NOT NULL
);

-- Tabla de Turnos (Relaciona Cliente, Barbero y Servicio)
CREATE TABLE turnos (
    id_turno INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_barbero INT NOT NULL,
    id_servicio INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    estado VARCHAR(20) DEFAULT 'Pendiente',
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente),
    FOREIGN KEY (id_barbero) REFERENCES barberos(id_barbero),
    FOREIGN KEY (id_servicio) REFERENCES servicios(id_servicio)
);

-- Tabla de Inventario / Insumos
CREATE TABLE inventario (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre_producto VARCHAR(50) NOT NULL,
    stock INT NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL
);
