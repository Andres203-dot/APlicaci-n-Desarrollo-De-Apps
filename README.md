# SmartStock: Gestión de Inventario Inteligente

**SmartStock** es una solución móvil integral diseñada para transformar la administración de inventarios en pequeñas y medianas empresas (PyMEs). Más que un simple contador de artículos, es una herramienta de inteligencia de negocios que permite valorizar activos, predecir necesidades de reabastecimiento y profesionalizar la toma de decisiones operativas.

---

##  Introducción
Detrás de cada estantería hay una historia de esfuerzo y crecimiento. **SmartStock** nace para quitarle al emprendedor la carga de la gestión manual y el miedo al desabastecimiento. Con una interfaz moderna, humana y resiliente, la aplicación permite llevar el control total del negocio directamente desde el bolsillo, funcionando de manera fluida incluso sin conexión a internet.

---

##  Problemática
Muchos negocios enfrentan desafíos críticos debido a una gestión de inventario deficiente:
*   **Errores Humanos:** Registros manuales propensos a fallos en el conteo.
*   **Ceguera Financiera:** Desconocimiento del valor monetario real invertido en bodega.
*   **Quiebres de Stock:** Falta de previsión sobre cuándo se agotarán los productos.
*   **Pérdida de Datos:** Información vulnerable a borrados accidentales o falta de respaldo.

---

##  Nuestra Solución
SmartStock cierra la brecha tecnológica mediante:
1.  **Dashboard 360°:** Un panel analítico que muestra la inversión total y el rendimiento individual por producto.
2.  **Predicción de Stock:** Algoritmo que estima días de inventario restantes basado en ventas reales.
3.  **Resiliencia:** Papelera de reciclaje para recuperar datos eliminados por error.
4.  **Profesionalismo:** Reportes en **PDF** y **CSV** listos para auditorías o compartición externa.

---

##  Características Principales
*   **Gestión de Catálogo (CRUD):** Registro completo con SKU, precios, categorías y alertas de stock bajo.
*   **Seguimiento de Movimientos:** Historial detallado de entradas y salidas de mercancía.
*   **Dashboard Financiero:** Visualización en tiempo real del valor del inventario (`Existencias x Precio`).
*   **Gestión de Proveedores:** Directorio visual con fotos de perfil para agilizar el contacto.
*   **Personalización:** Soporte para **Modo Oscuro** y un exclusivo **Tema Blanco y Negro (Monocromo)**.
*   **Exportación Inteligente:** Generación de documentos PDF profesionales con un solo clic.

---

##  Tecnologías Usadas (Tech Stack)
*   **Lenguaje:** [Kotlin](https://kotlinlang.org/)
*   **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) con **Material 3**.
*   **Arquitectura:** **MVVM** (Model-View-ViewModel).
*   **Base de Datos:** **Room** (con soporte KSP) para persistencia local robusta.
*   **Preferencia:** **DataStore** para el guardado de ajustes de usuario.
*   **Asincronía:** **Coroutines** & **Flow** para una UI reactiva y sin bloqueos.
*   **Multimedia:** **Coil** para la carga eficiente de imágenes de proveedores.
*   **Reportes:** Android PDF Document API.

---

##  Estructura del Proyecto
```text
com.smartlens.tcclosparcerosapp
├── data
│   ├── dao         # Interfaces de acceso a Room
│   ├── model       # Entidades (Product, Supplier, StockMovement)
│   ├── repository  # Lógica de datos centralizada
│   └── database    # Configuración de SmartStockDatabase
├── ui
│   ├── product     # Pantallas de Inventario, Detalle y Papelera
│   ├── stats       # Dashboard y Análisis de Datos
│   ├── stock       # Registro de movimientos
│   ├── supplier    # Gestión de Proveedores
│   └── theme       # Definición de colores y estilos M3
└── MainActivity.kt # Punto de entrada y navegación
```

---

##  Instalación
1. Clonar el repositorio.
2. Abrir en **Android Studio Ladybug** o superior.
3. Sincronizar Gradle.
4. Ejecutar en un dispositivo con **Android 8.0 (API 26)** o superior.

> **Nota:** Debido a las actualizaciones de esquema de base de datos, se recomienda realizar un `Clean Project` e instalar la app desde cero si se presentan conflictos con versiones previas.

---
