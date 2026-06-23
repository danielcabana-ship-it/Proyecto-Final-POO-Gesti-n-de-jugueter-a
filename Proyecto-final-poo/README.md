# Proyecto Final POO: Gestión de Juguetería

Este proyecto es una aplicación de escritorio desarrollada en Java utilizando el paradigma de Programación Orientada a Objetos (POO) y el patrón de diseño arquitectónico MVC (Modelo-Vista-Controlador) (adaptado con DAOs para persistencia).

Arquitectura y Componentes Principales

El proyecto está estructurado de manera lógica para separar las responsabilidades, lo cual facilita su mantenimiento y escalabilidad:

- **`xyz.jugueteria.models`**: Contiene las clases que representan los datos del negocio (`Cliente`, `Producto`, `Venta`, `DetalleVenta`). Son objetos simples (POJOs) que viajan por todo el sistema.
- **`xyz.jugueteria.dao`**: El corazón de la persistencia de datos (Data Access Object). Aquí viven las clases (`ClienteDAO`, `ProductoDAO`, `VentaDAO`) encargadas de hacer las consultas CRUD (Crear, Leer, Actualizar, Borrar) directamente a la base de datos MariaDB. 
- **`xyz.jugueteria.database.ConexionDB`**: Utiliza el patrón de diseño **Singleton** para garantizar que solo haya una conexión activa a la base de datos en toda la vida útil de la aplicación. Lee de forma segura las credenciales desde el archivo `config.properties`.
- **`xyz.jugueteria.views`**: Las interfaces de usuario construidas con Java Swing y embellecidas con la librería **FlatLaf**. 
- **`xyz.jugueteria.main.Main`**: El punto de entrada oficial que inicializa el tema gráfico (Look and Feel) y levanta la vista principal en el hilo adecuado (`Event Dispatch Thread`).

Tecnologías Utilizadas

- **Lenguaje:** Java (JDK 17+)
- **Interfaz Gráfica:** Java Swing con **FlatLaf** (Tema oscuro para un look moderno).
- **Base de Datos:** MariaDB.
- **Gestor de Dependencias:** Maven.

Configuración e Instalación

1. Clona el repositorio.
2. Crea la base de datos en MariaDB (o MySQL) con las tablas `clientes`, `productos`, `ventas` y `detalles_venta`.
3. Configura el archivo de propiedades que se encuentra en `src/main/resources/config.properties`:
   ```properties
   db.url=jdbc:mariadb://localhost:3306/tu_base_de_datos
   db.user=tu_usuario
   db.password=tu_contraseña
   ```
4. Ejecuta `mvn clean install` para descargar el driver de MariaDB y la librería FlatLaf.
5. Ejecuta la clase `xyz.jugueteria.main.Main`.

Lógica Destacada
- **Manejo de Transacciones**: Se asegura que las operaciones complejas (como registrar una venta que afecta el stock) mantengan la integridad de los datos.
- **Dry (Don't Repeat Yourself)**: Los métodos internos de mapeo (`mapearProducto`, etc.) previenen la duplicación de código en la lectura de `ResultSet`.
