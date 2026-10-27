# Demo de permisos y Crashlytics

Proyecto Android Kotlin con Compose y MVVM. El equipo confirmó `READ_CONTACTS` y el botón «Contar contactos».
Nombre de la app e integrantes: [COMPLETAR POR EL EQUIPO].

La guía completa y las respuestas numeradas están en [outputs/Guia-del-equipo.md](outputs/Guia-del-equipo.md).
Los diagramas editables están en [permisos.mmd](outputs/permisos.mmd) y [crash-fix.mmd](outputs/crash-fix.mmd).
También puedes [ver los diagramas renderizados en GitHub](outputs/Diagramas.md).

## Abrir desde GitHub

```powershell
git clone https://github.com/Legitelpapacito/permission-crashlytics-demo.git
Set-Location permission-crashlytics-demo
```

En Android Studio, seleccionar **Open** y abrir la carpeta `permission-crashlytics-demo` que contiene
`settings.gradle.kts`. Esperar a que termine Gradle Sync, seleccionar `app` y un dispositivo, y pulsar **Run**.
El modo local funciona sin configurar Firebase. Para la demo con el fallo deliberado, cambiar a `crash-demo`;
para la corrección, cambiar a `fix`. La rama `main` conserva el flujo seguro.

Abrir esta carpeta en Android Studio y usar su JDK integrado, SDK 36 y Gradle Wrapper 8.13.
`firebaseEnabled=false` permite compilar sin `google-services.json` y sin enviar reportes.
La configuración para Firebase está preparada y se activa explícitamente según la guía.

```powershell
./gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
python tools/check_rules.py
```

`main` conserva el flujo seguro. `crash-demo` contiene únicamente el fallo deliberado y `fix` lo corrige.
La identidad de los commits `Demo Scaffold <demo-scaffold@example.invalid>` es técnica; no identifica integrantes.
