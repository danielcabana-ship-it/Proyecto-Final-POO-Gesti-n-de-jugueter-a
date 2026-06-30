# Análisis del Proyecto: Gestión de Juguetería

---

## **LO QUE ESTÁ BIEN**

### 1. Arquitectura general (MVC + DAO)
- Separación clara en capas: `models`, `dao`, `views`, `database`, `util`.
- Las vistas NO tienen lógica de base de datos; todo el acceso a datos pasa por los DAOs.
- Uso de `CardLayout` para navegación entre vistas sin abrir nuevas ventanas.

### 2. Patrón DAO — bien implementado
- Cada tabla tiene su propio DAO (`ProductoDAO`, `ClienteDAO`, `UsuarioDAO`, `VentaDAO`).
- Cada DAO encapsula completamente el CRUD; las vistas nunca ven `Connection`, `PreparedStatement` o `ResultSet`.
- Método privado `mapearProducto(ResultSet)`/`mapearCliente(ResultSet)` para convertir filas en objetos — buena práctica.
- `VentaDAO.registrarVentaCompleta()` usa transacciones atómicas (`commit`/`rollback`) para garantizar integridad en operaciones multi-tabla.

### 3. Conexión a BD corregida
- `ConexionDB.getConexion()` abre **siempre una conexión nueva** (solución al bug del Singleton que dejaba conexiones muertas).
- Cada DAO usa `try-with-resources` que cierra automáticamente la conexión.

### 4. Seguridad básica
- Contraseñas hasheadas con SHA-256 (nunca texto plano en BD).
- `config.properties` en `.gitignore` — las credenciales no se suben al repositorio.
- Validación de unicidad de username/email antes de registrar.

### 5. UI moderna con FlatLaf
- Tema oscuro consistente en todas las vistas.
- Botones reutilizables mediante métodos helper (`crearBotonPrimario`, `crearBotonPeligro`).
- `Session` para mantener el usuario logueado sin tocar BD.

### 6. Build tooling
- Maven con versiones fijas de plugins (lockeado en `pluginManagement`).
- UTF-8 forzado en properties del POM.
- Schema SQL con seed data listo para ejecutar.

---

## **LO QUE ESTÁ MAL / PUEDE MEJORAR**

### 1. **MALA: El nombre del paquete está mal escrito**
- `xyz.jugueteria` debería ser `xyz.jugueteria` o mejor `com.jugueteria`. **"jugueteria" tiene una 't' de más.**
- Archivos afectados: TODOS los `.java`.

### 2. **MALA: Lógica de negocio en las vistas**
- `ProductosView`, `ClientesView`, `VentasView` contienen validaciones y reglas de negocio mezcladas con UI.
- **Solución:** Crear una capa `service` (ej. `ProductoService`, `VentaService`) que valide, calcule totales, verifique stocks, etc. Las vistas solo llaman al service.

### 3. **MALA: No hay manejo de excepciones propio**
- Todas las excepciones se tragan con `e.printStackTrace()` y `return false/null`.
- **Solución:** Crear excepciones personalizadas (ej. `ProductoNotFoundException`, `StockInsuficienteException`) y una capa que las maneje centralizadamente.

### 4. **MALA: Duplicación de código en vistas**
- `ProductosView`, `ClientesView` y `VentasView` tienen los mismos métodos helper duplicados:
  - `crearLabelBlanco()`
  - `crearTextFieldOscuro()`
  - `crearBotonPrimario()`
  - `crearBotonPeligro()`
- **Solución:** Moverlos a una clase `UIUtils` o un `BasePanel`.

### 5. **MALA: No hay interfaz DAO genérica**
- Los DAOs no implementan una interfaz común. Sería más limpio y testeable:
  ```java
  public interface DAO<T> {
      boolean insertar(T t);
      List<T> listar();
      boolean actualizar(T t);
      boolean eliminar(int id);
  }
  ```
- Luego `ProductoDAO implements DAO<Producto>`, etc. Esto permite cambiar implementación (JDBC, JPA, mock) sin tocar las vistas.

### 6. **REGULAR: Usar `double` para dinero**
- `precio`, `total`, `subtotal` modelados como `double` en lugar de `BigDecimal`.
- `double` tiene errores de precisión con decimales. **Solución:** usar `BigDecimal`.

### 7. **REGULAR: No hay pool de conexiones**
- Cada operación crea y cierra una conexión. Para una app de escritorio con 1 usuario no es crítico, pero un pool (HikariCP) sería más eficiente y profesional.

### 8. **REGULAR: SHA-256 sin sal (salt)**
- `SecurityUtils.hashPassword()` usa SHA-256 directo. Esto es vulnerable a ataques de rainbow tables.
- **Solución:** Usar `bcrypt`, `scrypt` o `PBKDF2` con salt aleatorio.

### 9. **REGULAR: `config.properties` con contraseña en texto plano**
- Aunque está en `.gitignore`, sigue siendo texto plano en el disco. Para producción usar variables de entorno o un keystore.

### 10. **REGULAR: El test es mínimo**
- Solo 1 test que verifica conexión a BD. Faltan tests unitarios para DAOs, modelos, y lógica de negocio.
- La BD tiene que estar disponible para correr los tests (no son unitarios, son de integración).

### 11. **REGULAR: La clase `Session` es mutable y estática**
- `Session.usuarioLogueado` se modifica desde cualquier lugar. Puede causar problemas de concurrencia. Una alternativa sería inyectar el usuario en los constructores de las vistas.

### 12. **LEVE: Nombres mixtos español-inglés**
- Métodos como `getConexion()` (español) vs `login()` (inglés). Elegir un idioma y mantener consistencia.

### 13. **LEVE: Sistema operativo en `Class.forName`**
- `Class.forName("org.mariadb.jdbc.Driver")` ya no es necesario desde JDBC 4.0 (los drivers se cargan automáticamente via SPI). Se puede eliminar.

### 14. **LEVE: El schema.sql usa `DROP DATABASE IF EXISTS`**
- Esto borra toda la base de datos al ejecutarse. Peligroso en producción. Mejor usar migrations (Flyway o Liquibase).

---

## **USO DEL PATRÓN DAO — ANÁLISIS DETALLADO**

### ¿Qué es DAO?
Data Access Object es un patrón que abstrae y encapsula el acceso a la fuente de datos (BD, archivo, API). Su objetivo es **separar la lógica de persistencia de la lógica de negocio**.

### ¿Cómo se aplica aquí?

| Capa | Responsabilidad | Archivos |
|---|---|---|
| **Model** | Representación de datos (POJOs) | `Producto`, `Cliente`, `Usuario`, `Venta`, `DetalleVenta` |
| **DAO** | CRUD contra MariaDB | `ProductoDAO`, `ClienteDAO`, `UsuarioDAO`, `VentaDAO` |
| **Database** | Proveer conexiones | `ConexionDB` |
| **View** | Interfaz de usuario (Swing) | `LoginView`, `MainView`, `ProductosView`, `ClientesView`, `VentasView` |

### Flujo típico:
```
View (clic botón) → crea objeto Model → llama DAO.método() → DAO abre Connection →
ejecuta SQL → mapea ResultSet → devuelve Model → View muestra resultado
```

### Lo que falta para un DAO impecable:
1. **Interfaz genérica** `DAO<T>` como se mencionó arriba.
2. **Método `obtenerPorId`** en todos los DAOs (solo `ProductoDAO` lo tiene).
3. **Manejo de excepciones DAO específicas** en lugar de `return false/null`.
4. **Tests unitarios** para cada DAO usando una BD en memoria (H2) o mocks.

---

## **RESUMEN DE PRIORIDADES**

| Prioridad | Qué hacer |
|---|---|
| 🔴 Alta | Corregir nombre del paquete (`jugueteria` → `jugueteria`) |
| 🔴 Alta | Extraer lógica de negocio de vistas a servicios |
| 🔴 Alta | Crear interfaz DAO genérica |
| 🟡 Media | Mover helpers UI a clase común (`UIUtils`) |
| 🟡 Media | Cambiar `double` a `BigDecimal` para dinero |
| 🟡 Media | Agregar excepciones personalizadas |
| 🟢 Baja | Reemplazar SHA-256 por bcrypt |
| 🟢 Baja | Agregar pool de conexiones (HikariCP) |
| 🟢 Baja | Más tests |
