@echo off
cd /d "%~dp0"

echo ========================================
echo  Iniciando base de datos (MariaDB)...
echo ========================================
docker compose up -d

echo ========================================
echo  Compilando y empaquetando la aplicacion...
echo ========================================
call mvn clean package -DskipTests
if %ERRORLEVEL% neq 0 (
    echo ERROR: La compilacion fallo.
    pause
    exit /b %ERRORLEVEL%
)

echo ========================================
echo  Ejecutando la aplicacion...
echo ========================================
java -jar target/Proyecto-final-poo-0.0.1-SNAPSHOT.jar

pause
