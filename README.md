# Proyecto Final POO: Gestión de Juguetería

Aplicación de escritorio Java Swing + MariaDB, dockerizada para facilitar su ejecución en cualquier PC.

## Requisitos

- **Docker Desktop** (https://www.docker.com/products/docker-desktop/)
- **Java 21 JDK** (https://adoptium.net/)
- **Git**

## Ejecutar (recomendado)

```bash
git clone <repo>
cd Proyecto-final-poo
```

### Windows
Haz doble clic en `run.bat` o ejecuta en terminal:
```batch
run.bat
```

### Linux / Mac
```bash
chmod +x run.sh
./run.sh
```

Esto levanta MariaDB en Docker, compila el proyecto con Maven y ejecuta la aplicación.

## Abrir en IntelliJ

1. File → Open → seleccionar la carpeta `Proyecto-final-poo`
2. IntelliJ detecta automáticamente el `pom.xml` (Maven) y descarga las dependencias
3. Si pide configurar el JDK, seleccionar **Java 21**
4. Ejecutar la clase `xyz.jugueteria.main.Main`

## Arquitectura

- **`xyz.jugueteria.models`** — POJOs del negocio (`Cliente`, `Producto`, `Venta`, `DetalleVenta`)
- **`xyz.jugueteria.dao`** — Capa de persistencia (CRUD contra MariaDB)
- **`xyz.jugueteria.database.ConexionDB`** — Conexión a BD (Singleton)
- **`xyz.jugueteria.views`** — Interfaces gráficas Java Swing + FlatLaf
- **`xyz.jugueteria.main.Main`** — Punto de entrada

## Tecnologías

Java 21, Java Swing, FlatLaf, MariaDB, Maven, Docker.
