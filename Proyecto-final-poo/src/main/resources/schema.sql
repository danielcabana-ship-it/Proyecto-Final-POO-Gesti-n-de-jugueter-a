-- =============================================================================
-- SCRIPT DE CREACIÓN DE BASE DE DATOS Y TABLAS (schema.sql)
-- =============================================================================
-- Este script define la estructura relacional de la juguetería.
-- Ejecutar este archivo en MariaDB para preparar el entorno de datos.

CREATE DATABASE IF NOT EXISTS proyecto_finall_POO
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE proyecto_finall_POO;

-- -----------------------------------------------------------------------------
-- 1. Tabla: categorias
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 2. Tabla: productos
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS productos (
    codigo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL CHECK (precio >= 0),
    stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    id_categoria INT NOT NULL,
    requiere_baterias BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 3. Tabla: usuarios
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    rol VARCHAR(50) NOT NULL DEFAULT 'vendedor' CHECK (rol IN ('admin', 'vendedor', 'auditor'))
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 4. Creación de Usuario de Aplicación y Permisos
-- -----------------------------------------------------------------------------
-- Creamos el usuario 'admin_g02' para conectarse desde local
CREATE USER IF NOT EXISTS 'admin_g02'@'localhost' IDENTIFIED BY 'wl61047821';

-- Otorgamos privilegios completos sobre la base de datos del proyecto
GRANT ALL PRIVILEGES ON proyecto_finall_POO.* TO 'admin_g02'@'localhost';

-- Aplicamos los cambios en el sistema de privilegios de MariaDB
FLUSH PRIVILEGES;
