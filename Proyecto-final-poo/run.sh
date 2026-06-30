#!/bin/bash
cd "$(dirname "$0")"

echo "========================================"
echo " Iniciando base de datos (MariaDB)..."
echo "========================================"
docker compose up -d

echo "========================================"
echo " Compilando y empaquetando la aplicacion..."
echo "========================================"
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "ERROR: La compilacion fallo."
    exit 1
fi

echo "========================================"
echo " Ejecutando la aplicacion..."
echo "========================================"
java -jar target/Proyecto-final-poo-0.0.1-SNAPSHOT.jar
