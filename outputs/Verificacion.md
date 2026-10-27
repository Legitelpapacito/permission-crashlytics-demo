# Verificación de la demo

Fecha: 27 de octubre de 2026. Configuración probada: modo local, `firebaseEnabled=false`, AGP 8.13.2, Gradle 8.13, SDK 36, JDK integrado de Android Studio. No se utilizó un google-services.json ni un proyecto Firebase real.

| Comprobación | Resultado |
| --- | --- |
| `:app:assembleDebug` en main | APK generado correctamente. |
| `:app:assembleDebug` en crash-demo | Correcto. La compilación no ejecuta el fallo deliberado. |
| `:app:assembleDebug :app:testDebugUnitTest` en fix | Correcto; 8 pruebas, 0 fallos, 0 errores. |
| `:app:testDebugUnitTest` en main | 8 pruebas, 0 fallos, 0 errores. |
| `:app:assembleDebugAndroidTest` | APK de instrumentación compilado. No se ejecutó en dispositivo. |
| `:app:lintDebug` | Correcto: 0 errores, 11 advertencias. |
| `python tools/check_rules.py` | 21 archivos Kotlin revisados, 8 reglas, controles mecánicos correctos. |
| Parser Mermaid 12.2.0 | Ambos diagramas válidos sintácticamente. No se afirma revisión visual renderizada. |

Las advertencias de Lint son OldTargetApi (1), AndroidGradlePluginVersion (1), GradleDependency (4), NewerVersionAvailable (2), DataExtractionRules (1), MissingApplicationIcon (1) y UseKtx (1). Se conservan versiones compatibles fijadas para la demo; no se ocultaron advertencias mediante una línea base.

Las ocho pruebas cubren: conteo autorizado, denegación antes de consultar, revocación entre comprobación y consulta, proveedor no disponible, propagación de cancelación, primera solicitud frente a bloqueo, justificación y permiso existente. Se corrigió una aserción que exigía identidad de objeto para CancellationException; el contrato exige propagar la cancelación sin reportarla, incluso cuando coroutines recupera la traza mediante una copia.

El formato de la ruta local del SDK se corrigió y se volvió a ejecutar el análisis de Lint para invalidar su resultado anterior. `local.properties` no se entrega ni se versiona: Android Studio debe crear el del equipo.

Las comprobaciones mecánicas no prueban por sí solas el significado de todos los nombres, la arquitectura completa ni los comportamientos de fabricantes. Se revisaron manualmente las dependencias UI → ViewModel → Repository y que únicamente FirebaseCrashReporter importe Firebase. `PermissionGate` conserva un parámetro String y no contiene constantes de un permiso concreto.

Pendiente para el equipo: comprobar los diálogos reales, Ajustes, rotación y recreación en dispositivo; compilar y ejecutar con el JSON real; observar y copiar fatal y no fatal del panel. Esos resultados son **[COMPLETAR POR EL EQUIPO]**. El nombre de la app y su ID registrado también están pendientes.

El cambio funcional de main a crash-demo afecta solamente tres archivos: la conexión del botón, el Repository y el número de versión. En fix se restauran los dos primeros exactamente como en main y se incrementa la versión. No se ejecutaron las pruebas unitarias de recuperación en crash-demo: allí las defensas cuya presencia verifican se quitaron deliberadamente.
