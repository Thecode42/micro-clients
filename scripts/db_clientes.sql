DROP DATABASE IF EXISTS db_clientes;
CREATE DATABASE db_clientes;
USE db_clientes;

CREATE TABLE Persona (
    id_persona INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100),
    genero VARCHAR(20),
    edad INT,
    identificacion VARCHAR(20) UNIQUE NOT NULL,
    direccion TEXT,
    telefono VARCHAR(20)
);

CREATE TABLE Cliente (
    id_cliente INT PRIMARY KEY AUTO_INCREMENT,
    contrasena VARCHAR(50),
    estado VARCHAR(20),
    id_persona INT UNIQUE,
    FOREIGN KEY (id_persona) REFERENCES Persona(id_persona)
);
CREATE INDEX idx_cliente_id_persona ON Cliente (id_persona);