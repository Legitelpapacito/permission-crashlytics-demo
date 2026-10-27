# Demo de permisos y Crashlytics para la hoja UVG

Implementación de una sola función: contar contactos. El equipo confirmó `READ_CONTACTS` y la acción «Contar contactos». Nombre de la app, integrantes, identificador registrado y evidencias del panel: **[COMPLETAR POR EL EQUIPO]**. El paquete `dev.example.permissioncrashdemo` es un identificador técnico de ejemplo; no afirma que exista en Firebase.

Se leyó el PDF completo de cuatro páginas. Sus ejemplos son orientación de la actividad. Las decisiones de implementación y las ramas responden a la solicitud del usuario; no se ha registrado ninguna app ni accedido al proyecto de Firebase del equipo.

Para abrir el código sin historial, extraer `PermissionCrashDemo-main.zip` y abrir la carpeta resultante en Android Studio. Para conservar las tres ramas y sus commits, colocar `PermissionCrashDemo.bundle` en una carpeta de trabajo y ejecutar:

```powershell
git clone -b main ./PermissionCrashDemo.bundle ./PermissionCrashDemo
Set-Location ./PermissionCrashDemo
git switch crash-demo
git switch fix
git switch main
```

El bundle es un repositorio Git portátil, sin credenciales ni carpeta de compilación. El ZIP contiene únicamente el estado seguro de main. `AGENTS.md` se encuentra en la raíz del proyecto; la copia en outputs facilita leerlo por separado.

## 1.1 Reglas listas para copiar

El archivo `AGENTS.md` de la raíz contiene exactamente estas ocho reglas:

1. Usar nombres ingleses; clases PascalCase y miembros camelCase. (Kotlin coding conventions)
2. Seguir UI → ViewModel → Repository; solo adaptadores de permisos y composición usan APIs Android en UI. (Guide to app architecture)
3. Parametrizar permisos de runtime; cubrir concesión, justificación, rechazo y bloqueo con Ajustes opcionales. (Request runtime permissions)
4. Importar Firebase únicamente en FirebaseCrashReporter; depender de CrashReporter. (Guide to app architecture; Customize crash reports)
5. Capturar errores de datos en Repository, reportarlos no fatales y propagar cancelaciones. (Customize crash reports; Guide to app architecture)
6. Verificar permiso en cada operación y tolerar revocaciones durante el acceso. (Request runtime permissions)
7. Kotlin: cuatro espacios, sin imports comodín; máximo 120 columnas, 180 líneas/archivo y 60/función. (Kotlin coding conventions; límites del equipo)
8. Hacer un commit por paso y verificarlo; conservar instrucciones breves. (Gemini best practices; Codex AGENTS.md)

Los límites 120/180/60 son decisiones verificables del equipo, no cifras impuestas por Kotlin. Los identificadores, comentarios y mensajes técnicos del código están en inglés; los recursos visibles y esta explicación están en español. Lectura: [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html).

La regla 2 precisa la prohibición inicial «la UI no usa ninguna API Android». La solicitud del permiso pertenece al ciclo de vida de la Activity: `PermissionGate` es el adaptador de plataforma autorizado. La pantalla no consulta contactos; ViewModel no conserva una Activity ni un Context. La composición construye las dependencias. Lectura: [Guide to app architecture](https://developer.android.com/topic/architecture).

`AGENTS.md` proporciona contexto persistente a Codex; la documentación describe su descubrimiento al iniciar una ejecución y la combinación de reglas aplicables. No se presupone que el archivo se relea del disco en cada mensaje: después de editarlo, iniciar una ejecución nueva asegura su incorporación. Mantenerlo breve reduce el contexto repetido. Lectura: [Custom instructions with AGENTS.md](https://learn.chatgpt.com/docs/agent-configuration/agents-md).

## 1.2 Consulta propuesta y pasos pequeños

**Consulta lista para copiar:**

> En este proyecto nuevo de Android Studio con Kotlin y Compose, sigue AGENTS.md. Agrega la función «Contar contactos»: al tocar el botón, solicita READ_CONTACTS con un componente reutilizable que reciba cualquier permiso individual de runtime. Usa Activity Result API y contempla permiso ya concedido, justificación cancelable, rechazo y bloqueo con Ajustes opcionales; no confundas la primera solicitud con rechazo permanente. Mantén UI → ViewModel → Repository, consulta ContactsContract únicamente desde la fuente de datos del Repository y expón StateFlow. Captura errores de datos en Repository y repórtalos mediante CrashReporter, sin imports de Firebase fuera de FirebaseCrashReporter. Deja Firebase desactivado hasta colocar el google-services.json real. Trabaja por pasos con un commit por paso: estructura, datos, permisos/UI, integración y pruebas. Entrega el diagrama Mermaid, las verificaciones realizadas y una explicación que podamos defender en cinco minutos. Mantén el fallo intencional exclusivamente en crash-demo y corrígelo en fix.

Es una consulta propuesta para la hoja, no una afirmación de que el equipo la envió previamente. Para ejecutarla de manera incremental se pueden enviar las cinco solicitudes de implementación una a una, adjuntando los archivos afectados y el resultado esperado. Lecturas: [Gemini best practices](https://developer.android.com/studio/gemini/best-practices) y [Codex prompting](https://learn.chatgpt.com/docs/prompting).

| Paso / commit | Cambio y razón |
| --- | --- |
| 1. `chore: define concise project rules and Android scaffold` | Reglas, Manifest, recursos y Gradle inicial. |
| 2. `feat: add repository boundary and observable view model` | Fuente de contactos, Repository, resultados recuperables y StateFlow. |
| 3. `feat: add reusable runtime permission workflow and Compose screen` | Adaptador parametrizado, diálogos y botón. |
| 4. `build: add opt-in Crashlytics integration and Gradle wrapper` | Implementación aislada y activación explícita de Firebase. |
| 5. `test: cover permission denial revocation and recovery boundaries` | Pruebas de autorización, revocación y cancelación; revisión de reglas. |
| 6. `test: assert cancellation contract across coroutine stack recovery` | Comprobar el contrato de cancelación sin exigir identidad del objeto de excepción. |
| 7. Documentación y verificación | Diagramas, guía y resultados reales de compilación. |
| 8. Solo `crash-demo` | Omitir el adaptador y quitar guardia/captura del Repository. |
| 9. Solo `fix`, hija de `crash-demo` | Restaurar ambas defensas y subir versión. |
| 10. Evidencias finales de ramas | Registrar sus compilaciones y pruebas sin incorporar el fallo en main. |

El historial real se consulta con `git log --all --graph --oneline`. La identidad técnica de los commits no es un integrante del equipo. Las pruebas finales integradas complementan la revisión incremental de cada paso.

## 2.1 Permiso, tipo y acción

**Permiso y tipo:** `android.permission.READ_CONTACTS`; protección `dangerous`, solicitado en tiempo de ejecución desde Android 6/API 23. `minSdk=23`. **Acción:** el usuario toca «Contar contactos». No se solicita al iniciar la app. `INTERNET` también está declarado para Firebase, pero es un permiso de instalación y no es el permiso de la demo. Lecturas: [Permissions on Android](https://developer.android.com/guide/topics/permissions/overview) y [Manifest.permission.READ_CONTACTS](https://developer.android.com/reference/android/Manifest.permission#READ_CONTACTS).

La operación consulta directamente `ContactsContract.Contacts.CONTENT_URI` con una proyección que solo pide `_ID`, toma `cursor.count` y cierra el cursor con `use`. Devuelve 0 si no hay contactos. No enumera ni registra nombres o teléfonos. La lectura del proveedor requiere `READ_CONTACTS`; un selector del sistema que otorgase acceso a una URI concreta sería otro flujo. Lectura: [Contacts Provider](https://developer.android.com/identity/providers/contacts-provider).

### Qué hace cada archivo

Las rutas siguientes parten de `app/src/main/java/dev/example/permissioncrashdemo/`, salvo indicación distinta.

| Archivo | Responsabilidad que puede explicar cualquier integrante |
| --- | --- |
| `MainActivity.kt` y `di/DemoContainer.kt` | Punto de entrada y composición: conectar implementaciones e iniciar Compose. |
| `ui/DemoRoute.kt` | Observar el estado con ciclo de vida y conectar resultados de permiso con ViewModel. |
| `ui/DemoScreen.kt` | Dibujar botón y mensajes; recibe estado y callbacks. |
| `permission/PermissionGate.kt` | Único adaptador que comprueba, solicita y recibe permisos, y abre Ajustes. No consulta contactos. |
| `permission/PermissionPolicy.kt` | Decidir entre permiso existente, justificación, solicitud o bloqueo después del callback. |
| `permission/PermissionDialogs.kt` | Justificación y Ajustes, ambos cancelables. |
| `ui/DemoViewModel.kt` | Convertir resultados del Repository en estado observable; evitar consultas simultáneas. |
| `data/ContactsRepository.kt` | Revalidar permiso, ejecutar en IO, capturar y reportar errores; devolver resultados tipados. |
| `data/AndroidContactsDataSource.kt` | Encapsular `ContextCompat` y `ContentResolver`, usando Application Context. |
| `reporting/CrashReporter.kt` | Contrato de reporte sin Firebase. |
| `src/firebase/kotlin/.../FirebaseCrashReporter.kt` | Traducir el contrato a `setCustomKey`, `log` y `recordException`. |
| `src/local/kotlin/.../CrashReporterFactory.kt` | Proveer una implementación sin envíos para trabajar localmente. |

Se usa `viewModelScope` para cancelar al destruir el ViewModel y `Dispatchers.IO` para no bloquear la pantalla. `CancellationException` se propaga; una cancelación normal no se reporta como error. `StateFlow` mantiene el estado y Compose lo representa. El ViewModel sobrevive a la rotación; los diálogos y una solicitud en curso usan estado guardable. Si se destruye el proceso, no se debe asumir que todavía existe un permiso concedido.

### Alcance del componente genérico

`PermissionGate(permission: String, ...)` no contiene `READ_CONTACTS` ni nombres de otros permisos. Se le puede pasar otro permiso individual declarado en Manifest y una justificación específica. Esto no elimina requisitos especiales de una funcionalidad: ubicación en segundo plano, permisos múltiples o diferencias de versión necesitan su propia coordinación. Los permisos especiales que requieren otra pantalla de configuración no se convierten en permisos de runtime por usar este componente.

Antes de solicitar, se comprueba el permiso actual y la justificación. Solo después de un callback denegado se usa `shouldShowRequestPermissionRationale=false` para tratar la solicitud como bloqueada y ofrecer Ajustes. Es una clasificación práctica: una política de dispositivo también puede bloquearla; no prueba por sí misma una decisión personal de «no preguntar otra vez». No se insiste automáticamente y al volver de Ajustes se exige otro toque. Lectura: [Request runtime permissions, Workflow y Handle permission denial](https://developer.android.com/training/permissions/requesting).

## 2.2 Diagramas de secuencia

Abrir [permisos.mmd](permisos.mmd) para el flujo de `main`/`fix`, y [crash-fix.mmd](crash-fix.mmd) para el contraste entre el fatal deliberado y su corrección verde. Se pueden pegar en un editor Mermaid. Los participantes son Usuario, UI, Sistema Android, ViewModel y Repository.

El primer diagrama recorre los tres resultados, incluye permiso ya concedido, justificación cancelable y nueva comprobación antes del acceso. Las notas de reporte representan llamadas internas a `CrashReporter` sin añadir otra columna. La rama verde cubre la captura en Repository.

El segundo marca todos los incumplimientos deliberados del cambio de `crash-demo`: **❌ X reglas 3 y 6** al omitir `PermissionGate` desde el botón; **❌ X reglas 5 y 6** al quitar verificación y captura en Repository. El resto de la arquitectura sigue siendo UI → ViewModel → Repository. No es necesario debilitar las reglas para aceptar un error introducido expresamente para la práctica.

## 2.3 Revisión y regla precisada

**Texto listo para copiar:** «Precisamos la regla “la UI no usa APIs Android” así: “Seguir UI → ViewModel → Repository; solo adaptadores de permisos y composición usan APIs Android en UI”. La precisión permite integrar Activity Result con el ciclo de vida, sin permitir que la pantalla lea contactos. En main y fix no encontramos incumplimientos tras revisar imports, dependencias y el recorrido de cada resultado; verificamos mecánicamente ocho reglas, límites de Kotlin e imports Firebase. Las pruebas unitarias cubren permisos, revocación y recuperación; la validación del diálogo real y del panel requiere ejecutarlos en un dispositivo con la configuración del equipo.»

Esta precisión se hizo al definir las reglas; no se presenta como un incumplimiento accidental observado después. Si la hoja exige registrar solamente reglas reescritas después de un fallo, se puede responder: «No hubo incumplimientos involuntarios del AGENTS.md final; se verificó como se describe arriba». Los incumplimientos intencionales de `crash-demo` aparecen con X en su diagrama y desaparecen en `fix`.

`tools/check_rules.py` revisa indentación, imports, límites físicos e independencia del adaptador. No demuestra semántica, nombres ingleses ni todos los casos de ciclo de vida: esos puntos necesitan revisión humana y las pruebas manuales de abajo. Los cuerpos de función con llaves se cuentan desde `fun` hasta su cierre; los cuerpos de expresión requieren revisión adicional.

## 3.1 Activar Firebase y observar el fatal

### Configuración preparada

La raíz declara los plugins Google Services `4.5.0` y Crashlytics `3.0.8`, además de AGP `8.13.2`, Kotlin/Compose compiler `2.3.21`. El módulo aplica los dos primeros solo con `firebaseEnabled=true`. Entonces agrega Firebase BoM `35.1.0` y `firebase-crashlytics`, y compila la implementación Firebase. No usa los antiguos módulos `-ktx`. La guía inicial de Firebase contiene esos valores de sus plugins y BoM; su compatibilidad final se verifica al sincronizar el proyecto real. Lecturas: [Get started with Crashlytics](https://firebase.google.com/docs/crashlytics/android/get-started), [AGP 8.13](https://developer.android.com/build/releases/agp-8-13-0-release-notes) y [Compose compiler con Kotlin 2](https://developer.android.com/jetpack/androidx/releases/compose-kotlin).

1. En el proyecto Firebase existente del equipo, registrar una app Android adicional con su `applicationId`: **[COMPLETAR POR EL EQUIPO]**. El namespace del código puede permanecer igual.
2. Descargar su archivo real y colocarlo exactamente en **`app/google-services.json`**. Está excluido de Git. No se creó ningún archivo con credenciales inventadas.
3. En `gradle.properties`, escribir `firebaseEnabled=true` y `demoApplicationId=` con el ID registrado. Cambiar `app_name` en `app/src/main/res/values/strings.xml` por el nombre del equipo. Sin ese archivo real, activar Firebase detiene la configuración con un mensaje claro.
4. Abrir Android Studio con su JDK integrado (JDK 17 o 21 compatible), sincronizar y ejecutar en un dispositivo o emulador con proveedor de contactos. La interfaz indica si está en modo local o Firebase.

No hace falta Analytics para los tres métodos explícitos de reporte utilizados. Si el equipo quiere breadcrumbs automáticos, deberá configurar Analytics por separado.

### Reproducción deliberada en crash-demo

`main` es segura (`versionCode=1`). `crash-demo` (`versionCode=2`, `versionName=1.0-crash`) omite `PermissionGate` y elimina del Repository la guardia y el `try/catch`. `fix` (`versionCode=3`, `versionName=1.1-fix`) restaura ambas defensas. El mismo ID y firma debug permiten actualizar la instalación; para volver a main después de una versión mayor se necesita desinstalar o usar un dispositivo separado.

```powershell
git switch crash-demo
./gradlew.bat :app:installDebug -PfirebaseEnabled=true
```

Si el ID no está guardado en `gradle.properties`, agregar `-PdemoApplicationId=` con el ID real en los comandos de compilación.

En Ajustes del dispositivo → Aplicaciones → app de demo → Permisos → Contactos, revocar el permiso. Android puede terminar el proceso al revocarlo; ese cierre del sistema no es el fatal buscado. **Reabrir la app y tocar «Contar contactos».** La consulta directa al proveedor, sin permiso, provoca la `SecurityException` que queda sin capturar. Se eligió la consulta protegida del proveedor de contactos; contar una lista en memoria o abrir un selector externo no comprueba este mismo fallo. Lectura: [Retrieve contacts y READ_CONTACTS](https://developer.android.com/identity/providers/contacts-provider/retrieve-names).

Después del fatal, abrir otra vez la app sin pulsar inmediatamente el botón. Con internet y Firebase activo, el SDK puede enviar el reporte pendiente. Identificar el issue y su stack trace en Crashlytics. No asumir que el primer frame es de nuestra app: buscar el frame de la llamada a `contentResolver.query`.

**Para copiar después de la ejecución:**

- Excepción observada: **[COMPLETAR POR EL EQUIPO]**; hipótesis del código: `java.lang.SecurityException`.
- Archivo y línea del panel: **[COMPLETAR POR EL EQUIPO]**. El lugar previsto es `AndroidContactsDataSource.kt`, en `contentResolver.query`; la línea real debe copiarse del reporte de esa versión.
- Causa comprobada: **[COMPLETAR POR EL EQUIPO]**; hipótesis: consultar contactos sin `READ_CONTACTS`, omitiendo comprobación y captura.
- Issue URL, versión, dispositivo, fecha y captura: **[COMPLETAR POR EL EQUIPO]**.

El miembro que prepara el fallo puede guardar la explicación mientras los demás diagnostican usando únicamente el panel, como pide la hoja. El repositorio no hace que ese secreto sea inaccesible; la dinámica depende del equipo.

### Force a test crash

Para validar la conexión por separado, en una copia de trabajo de `crash-demo` se puede agregar temporalmente dentro de `setContent` este botón Compose y ejecutar sin el debugger adjunto:

```kotlin
androidx.compose.material3.Button(
    onClick = { throw RuntimeException("Test Crash") },
) {
    androidx.compose.material3.Text("Provocar cierre de prueba")
}
```

Tocar el botón, dejar que cierre la app y **reiniciarla** para enviar el fatal. Esta excepción no se captura: valida la ruta fatal y no sustituye el fallo de permisos. Retirar el botón antes de terminar; nunca incorporarlo a `main`. La guía de inicio indica revisar conectividad y logs si el panel no muestra el reporte después de unos cinco minutos. No se garantiza un tiempo fijo de llegada. Lectura: [Force a test crash](https://firebase.google.com/docs/crashlytics/android/get-started#force-test-crash).

## 3.2 Corrección y error no fatal

```powershell
git switch fix
./gradlew.bat :app:installDebug -PfirebaseEnabled=true
```

La corrección restaura el botón mediante `PermissionGate` y captura excepciones en **Repository**, junto al acceso protegido. Verifica el permiso antes de consultar; si ya falta, lanza una `SecurityException` local dentro del `try`. Si se revoca después de comprobarlo, captura la excepción que lance el proveedor. En ambos casos devuelve `PermissionRequired`. Otros errores de datos devuelven `Unavailable`; las cancelaciones se vuelven a lanzar. ViewModel traduce esos resultados en mensajes y la app sigue abierta.

Esta capa conoce la operación y puede recuperarla sin acoplar la UI al proveedor. Un rechazo normal en el diálogo no invoca Repository ni produce un no fatal; se registra el fallo cuando una operación alcanza Repository sin autorización válida o el proveedor falla.

`CrashReporter` recibe la excepción y atributos estables (`operation=count_contacts`, `permission=READ_CONTACTS`, `permission_granted_at_check=true/false`). `FirebaseCrashReporter` llama a `setCustomKey`, agrega un `log` y usa `recordException`. Las claves describen el estado en la comprobación, no garantizan que siguiera vigente al consultar. No se registran los datos de los contactos. Los no fatales se agrupan para enviarse al próximo inicio de la app. Lectura: [Customize your Crashlytics crash reports](https://firebase.google.com/docs/crashlytics/android/customize-crash-reports).

### Cómo verificar la recuperación sin conservar el fallo en la UI

Las pruebas unitarias usan una fuente falsa que revoca el permiso entre comprobación y consulta, y verifican el resultado recuperable y el reporte con la misma excepción. Para registrar un no fatal real sin añadir una ruta insegura permanente, usar `RepositoryRecoveryTest` con Firebase activo, después de instalar `fix` y revocar el permiso:

```powershell
./gradlew.bat :app:connectedDebugAndroidTest -PfirebaseEnabled=true `
  -Pandroid.testInstrumentationRunnerArguments.class=dev.example.permissioncrashdemo.RepositoryRecoveryTest
```

La prueba confirma que el permiso esté denegado, llama a Repository y exige `PermissionRequired`; el reporter seleccionado es el real si Firebase está activado. Este caso verifica la guardia previa. La carrera exacta con el proveedor se cubre de forma determinista mediante la fuente falsa, porque revocar desde Ajustes puede matar el proceso.

Después de la prueba, relanzar la app para enviar el no fatal y revisar la clasificación, versión y claves en el panel. Resultado y captura: **[COMPLETAR POR EL EQUIPO]**. El fatal de la versión anterior seguirá en su historial: la corrección no lo borra ni necesariamente crea un issue no fatal con el mismo identificador.

## 3.3 Flujo del equipo en seis pasos

1. **Priorizar — [INTEGRANTE].** Revisar tipo, usuarios/sesiones afectados, frecuencia, versión e impacto de la función. Asignar severidad y responsable; atender primero cierres de flujos críticos.
2. **Registrar — [INTEGRANTE].** Crear un ticket en **[COMPLETAR POR EL EQUIPO: tablero]**, enlazar el issue de Crashlytics y adjuntar stack trace, versión, dispositivo, permiso y claves. Registrar avance y decisiones allí; dejar en el issue una nota con el enlace.
3. **Reproducir — [INTEGRANTE].** Instalar la versión afectada en un dispositivo de prueba y repetir los pasos con el mismo estado de permisos. Anotar resultado esperado/real y condiciones necesarias en el ticket.
4. **Corregir — [INTEGRANTE].** Crear rama y PR, aplicar la captura en Repository y agregar una prueba de regresión. Enlazar commits y revisión al ticket, y mantener actualizado su estado.
5. **Verificar una versión nueva — [INTEGRANTE].** Incrementar versionCode, distribuir la corrección, repetir el caso y comprobar que la app siga viva. Revisar no fatales, claves y ausencia de nuevos fatales en esa versión durante **[COMPLETAR POR EL EQUIPO: período]**; conservar evidencias. Probar recuperación no sustituye revisar el comportamiento general.
6. **Cerrar y vigilar regresión — [INTEGRANTE].** Cerrar ticket e issue tras cumplir los criterios y registrar la versión corregida. Si Crashlytics lo reabre como regressed, reasignar, contrastar la versión nueva o antigua y volver a reproducir; no cerrarlo automáticamente.

Una regresión de Crashlytics no equivale a cualquier reporte tardío: después del cierre, el algoritmo considera si la versión del nuevo reporte ya era conocida por Crashlytics al cerrar. Una versión antigua que nunca había reportado también puede provocar reapertura. Revisar la versión antes de concluir que el fix falló. Lectura: [What is a regressed issue?](https://firebase.google.com/docs/crashlytics/troubleshooting#regressed-issues).

## Verificación del equipo y exposición de cinco minutos

Los resultados ejecutados en este entorno se entregan en `Verificacion.md`. Las evidencias del dispositivo y Firebase solo se completan después de correr la demo real.

| Caso manual | Resultado que se debe observar |
| --- | --- |
| Instalación limpia, primer toque | Diálogo de permiso; nunca clasificar como bloqueo antes del callback. |
| Conceder | Se muestra el total; cero también es válido. |
| Volver a tocar con permiso | Consulta sin repetir el diálogo. |
| Rechazar | Mensaje específico y justificación cancelable; no consulta contactos. |
| Rechazar repetidamente / no volver a preguntar | Guía opcional a Ajustes; no insistencia automática. |
| Cancelar cualquier diálogo | Pantalla usable, sin consulta protegida. |
| Conceder en Ajustes y regresar | El siguiente toque comprueba el estado actual y cuenta. |
| Revocar en Ajustes, reiniciar y tocar en main/fix | Vuelve al flujo de permiso; no cae la app. |
| Rotar con diálogo/solicitud | No duplicar solicitudes ni consultas; conservar resultado útil. |
| Rama crash-demo con permiso revocado | Cierre provocado por consulta protegida; reabrir y buscar fatal. |
| Rama fix, prueba RepositoryRecoveryTest | Resultado recuperable; reabrir y buscar no fatal con claves. |

Para volver a probar el primer diálogo se puede reinstalar en el dispositivo de prueba; se pierden los datos y permisos de esa instalación. Preferir un emulador dedicado.

**Guion de defensa:** 0:00–0:35, objetivo y permiso; 0:35–1:40, recorrer las tres rutas del diagrama; 1:40–2:25, explicar UI → ViewModel → Repository y CrashReporter; 2:25–3:20, mostrar el fatal ya cargado en el panel y su stack; 3:20–4:20, mostrar fix y no fatal previamente enviado; 4:20–5:00, explicar responsable, versión corregida y regresión. Generar los reportes antes de exponer para no depender del tiempo de carga del panel.

Cada integrante debe poder responder: ¿por qué `false` de rationale no basta antes de pedir?, ¿por qué una comprobación previa no elimina la carrera?, ¿qué diferencia hay entre un rechazo normal y una excepción no fatal?, ¿por qué el ViewModel no conoce Firebase?, ¿qué evidencia permite cerrar el ticket?

## Resumen listo para copiar según el PDF

- **1.1:** Copiar las ocho reglas numeradas del inicio con sus lecturas. Precisar la excepción del adaptador de plataforma y reconocer que los límites de tamaño son del equipo.
- **1.2:** Copiar la consulta propuesta y dividirla en los pasos del historial.
- **2.1:** `READ_CONTACTS`, permiso dangerous de tiempo de ejecución; el usuario toca «Contar contactos».
- **2.2:** Adjuntar `permisos.mmd`; incluir `crash-fix.mmd` con sus X rojas y corrección verde.
- **2.3:** La pantalla no accede a contactos. Se precisó la regla del adaptador; el AGENTS.md final se revisó mediante imports, límites y pruebas. Los casos de dispositivo y panel se verifican con el equipo.
- **3.1:** Firebase queda preparado y desactivado hasta recibir el JSON real. Hipótesis del fallo en crash-demo: `SecurityException` al consultar contactos sin permiso. Excepción, archivo/línea y causa observados: **[COMPLETAR POR EL EQUIPO]**.
- **3.2:** En fix, Repository captura la excepción y usa CrashReporter para registrarla no fatal con claves y log; devuelve un estado recuperable y la app continúa.
- **3.3:** Priorizar, registrar ticket, reproducir, corregir con prueba, verificar versión nueva y cerrar/vigilar regresiones; responsable de cada paso: **[INTEGRANTE]**.
