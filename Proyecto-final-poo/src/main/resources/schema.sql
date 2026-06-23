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
-- 4. Tabla: clientes
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(15) NOT NULL UNIQUE,
    nombre_completo VARCHAR(150) NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(255)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 5. Tabla: ventas
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS ventas (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    fecha_venta DATETIME DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10, 2) NOT NULL CHECK (total >= 0),
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 6. Tabla: detalle_ventas
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS detalle_ventas (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_unitario DECIMAL(10, 2) NOT NULL CHECK (precio_unitario >= 0),
    subtotal DECIMAL(10, 2) NOT NULL CHECK (subtotal >= 0),
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(codigo)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 7. Creación de Usuario de Aplicación y Permisos
-- -----------------------------------------------------------------------------
-- Creamos el usuario 'admin_g02' para conectarse desde local
CREATE USER IF NOT EXISTS 'admin_g02'@'localhost' IDENTIFIED BY 'wl61047821';

-- Otorgamos privilegios completos sobre la base de datos del proyecto
GRANT ALL PRIVILEGES ON proyecto_finall_POO.* TO 'admin_g02'@'localhost';

-- Aplicamos los cambios en el sistema de privilegios de MariaDB
FLUSH PRIVILEGES;

