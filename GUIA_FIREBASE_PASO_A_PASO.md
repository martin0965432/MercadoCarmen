# 🔥 Guía Paso a Paso: Configuración de Firebase Console

## ⏱️ Tiempo estimado: 15-20 minutos

---

## 📌 PASO 1: Acceder a Firebase Console

### 1.1 Abrir Firebase Console
1. Abre tu navegador (Chrome recomendado)
2. Ve a: **https://console.firebase.google.com/**
3. Inicia sesión con tu cuenta de Google
   - Si no tienes cuenta de Google, créala primero

✅ **Checkpoint:** Deberías ver la página principal de Firebase con un botón grande que dice "Agregar proyecto" o "Add project"

---

## 📌 PASO 2: Crear el Proyecto Firebase

### 2.1 Iniciar creación del proyecto
1. Haz clic en **"Agregar proyecto"** o **"Add project"** (botón grande en el centro)
2. Se abrirá un asistente de 3 pasos

### 2.2 Paso 1 del asistente - Nombre del proyecto
1. En el campo "Nombre del proyecto", escribe: **MercadoCarmen**
2. Notarás que abajo aparece un ID único como `mercadocarmen-xxxxx`
   - Este ID es generado automáticamente
3. Haz clic en **"Continuar"**

### 2.3 Paso 2 del asistente - Google Analytics
1. Verás la opción "Google Analytics para este proyecto"
2. **Desactiva** esta opción (mueve el toggle a la izquierda)
   - No necesitamos Analytics para esta app
3. Haz clic en **"Crear proyecto"**

### 2.4 Esperar creación
1. Firebase comenzará a crear tu proyecto
2. Verás una animación de carga (tarda 20-30 segundos)
3. Cuando termine, haz clic en **"Continuar"**

✅ **Checkpoint:** Deberías estar viendo el Dashboard principal de tu proyecto "MercadoCarmen" con varias tarjetas (Analytics, Authentication, etc.)

---

## 📌 PASO 3: Agregar App Android al Proyecto

### 3.1 Iniciar configuración de Android
1. En el centro de la pantalla, verás una sección que dice "Comienza agregando Firebase a tu app"
2. Haz clic en el ícono de **Android** (robot verde)
   - Si no lo ves, ve a "Configuración del proyecto" (ícono de engranaje arriba) > "General" y busca "Tus apps" > "Agregar app" > Android

### 3.2 Paso 1 - Registrar app
En el formulario que aparece, completa:

1. **Nombre del paquete de Android (OBLIGATORIO):**
   ```
   com.example.mercadocarmen
   ```
   ⚠️ **MUY IMPORTANTE:** Debe ser exactamente este nombre. Cópialo y pégalo.

2. **Alias de la app (opcional):**
   ```
   MercadoCarmen
   ```

3. **Certificado de firma de depuración SHA-1 (opcional):**
   - Déjalo **VACÍO** por ahora
   - Lo podemos agregar después si usamos autenticación

4. Haz clic en **"Registrar app"**

### 3.3 Paso 2 - Descargar google-services.json
Este es el paso MÁS IMPORTANTE:

1. Verás un botón grande que dice **"Descargar google-services.json"**
2. Haz clic en ese botón
3. Se descargará un archivo llamado `google-services.json`

**AHORA PRESTA MUCHA ATENCIÓN:**

4. Abre el **Explorador de Archivos** de Windows
5. Ve a la carpeta de **Descargas**
6. Busca el archivo `google-services.json` que acabas de descargar
7. **CÓPIALO** (Ctrl+C)
8. Navega a tu proyecto:
   ```
   C:\Users\lenovo\AndroidStudioProjects\MercadoCarmen\app\
   ```
9. **PÉGALO** (Ctrl+V) en la carpeta `app`

**VERIFICACIÓN CRÍTICA:**
- El archivo debe estar en: `MercadoCarmen\app\google-services.json`
- **NO** debe estar en: `MercadoCarmen\google-services.json` ❌
- **NO** debe estar en: `MercadoCarmen\app\src\google-services.json` ❌

10. Una vez copiado, regresa a Firebase Console y haz clic en **"Siguiente"**

### 3.4 Paso 3 - Agregar SDK de Firebase
1. Verás instrucciones para agregar dependencias
2. **IGNORA estas instrucciones** - Ya las agregamos en el código
3. Haz clic en **"Siguiente"**

### 3.5 Paso 4 - Finalizar
1. Haz clic en **"Ir a la consola"**

✅ **Checkpoint:** Deberías estar de vuelta en el Dashboard principal del proyecto

---

## 📌 PASO 4: Configurar Firestore Database

### 4.1 Acceder a Firestore
1. En el menú lateral izquierdo, busca la sección **"Compilación"** o **"Build"**
2. Haz clic en **"Firestore Database"**
3. Verás una página con información sobre Cloud Firestore

### 4.2 Crear la base de datos
1. Haz clic en el botón **"Crear base de datos"** o **"Create database"**
2. Se abrirá un modal con dos opciones

### 4.3 Configurar modo de seguridad
Verás dos opciones:
- **Modo de producción** (Production mode)
- **Modo de prueba** (Test mode)

**Selecciona:** ✅ **Modo de prueba** (Test mode)

⚠️ **Nota:** Esto permite lectura/escritura sin autenticación durante 30 días. Perfecto para desarrollo.

Haz clic en **"Siguiente"**

### 4.4 Seleccionar ubicación
1. Verás un dropdown para seleccionar la ubicación de Cloud Firestore
2. **Recomendación para México/Latinoamérica:**
   - Selecciona: **us-central (Iowa)** o **southamerica-east1 (São Paulo)**

3. ⚠️ **IMPORTANTE:** Una vez seleccionada, NO se puede cambiar
4. Haz clic en **"Habilitar"**

### 4.5 Esperar habilitación
1. Firebase configurará Firestore (tarda 30-60 segundos)
2. Cuando termine, verás la interfaz de Firestore con pestañas: "Datos", "Reglas", "Índices", "Uso"

✅ **Checkpoint:** Deberías ver la pestaña "Datos" con el mensaje "No hay datos que mostrar. Comienza agregando una colección"

### 4.6 Verificar reglas de seguridad
1. Haz clic en la pestaña **"Reglas"**
2. Deberías ver algo como esto:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.time < timestamp.date(2025, 2, 1);
    }
  }
}
```

3. Esta fecha se extiende 30 días desde hoy
4. **NO CAMBIES NADA** por ahora
5. Si las reglas son diferentes, cópialas y pégalas

✅ **Firestore está listo!**

---

## 📌 PASO 5: Configurar Firebase Storage

### 5.1 Acceder a Storage
1. En el menú lateral izquierdo, en la sección **"Compilación"** o **"Build"**
2. Haz clic en **"Storage"**
3. Verás una página introductoria sobre Cloud Storage

### 5.2 Comenzar configuración
1. Haz clic en el botón **"Comenzar"** o **"Get started"**
2. Se abrirá un modal sobre reglas de seguridad

### 5.3 Configurar reglas de seguridad
1. Verás un modal explicando las reglas de seguridad
2. Se mostrará el **modo de prueba** por defecto (similar a Firestore)
3. Haz clic en **"Siguiente"**

### 5.4 Seleccionar ubicación de Storage
1. Verás un dropdown para la ubicación
2. **IMPORTANTE:** Selecciona la **MISMA UBICACIÓN** que elegiste para Firestore
   - Si elegiste `us-central` para Firestore → elige `us-central` aquí
   - Si elegiste `southamerica-east1` → elige `southamerica-east1` aquí
3. Haz clic en **"Listo"**

### 5.5 Esperar configuración
1. Firebase configurará Storage (20-30 segundos)
2. Cuando termine, verás la interfaz de Storage con carpetas y archivos

✅ **Checkpoint:** Deberías ver un bucket vacío con el nombre parecido a `mercadocarmen-xxxxx.appspot.com`

### 5.6 Verificar reglas de Storage
1. Haz clic en la pestaña **"Reglas"**
2. Deberías ver algo como:

```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if request.time < timestamp.date(2025, 2, 1);
    }
  }
}
```

3. **NO CAMBIES NADA** por ahora

✅ **Storage está listo!**

---

## 📌 PASO 6: Verificación Final en Firebase Console

Vamos a verificar que todo está correctamente configurado:

### 6.1 Verificar App Registrada
1. Haz clic en el **ícono de engranaje** (⚙️) arriba a la izquierda
2. Selecciona **"Configuración del proyecto"**
3. Baja hasta la sección **"Tus apps"**
4. Deberías ver tu app Android con:
   - Nombre del paquete: `com.example.mercadocarmen`
   - Alias: MercadoCarmen

### 6.2 Verificar Servicios Habilitados
En el menú lateral, verifica que estos servicios estén disponibles:
- ✅ **Firestore Database** - debe estar habilitado
- ✅ **Storage** - debe estar habilitado
- ⚪ **Authentication** - puede estar deshabilitado (lo usaremos después)

### 6.3 Checklist Final en Firebase Console
Marca cada item:
- [ ] Proyecto "MercadoCarmen" creado
- [ ] App Android registrada con paquete `com.example.mercadocarmen`
- [ ] google-services.json descargado
- [ ] Firestore Database habilitado en modo de prueba
- [ ] Storage habilitado en modo de prueba
- [ ] Ambos servicios en la misma ubicación geográfica

---

## 📌 PASO 7: Verificación en Android Studio

Ahora vamos a verificar que el archivo `google-services.json` está en el lugar correcto:

### 7.1 Abrir Android Studio
1. Abre Android Studio
2. Abre el proyecto **MercadoCarmen**

### 7.2 Verificar archivo google-services.json
1. En el panel izquierdo (Project), cambia la vista a **"Project"** (no "Android")
2. Navega a: `MercadoCarmen > app`
3. Deberías ver el archivo `google-services.json` ahí
4. Si no está ahí, **DETENTE** y vuelve al PASO 3.3

### 7.3 Verificar contenido del archivo
1. Haz doble clic en `google-services.json`
2. Verifica que contenga:
   - `"project_id": "mercadocarmen-xxxxx"`
   - `"package_name": "com.example.mercadocarmen"`
   - Múltiples claves y configuraciones

3. ⚠️ **IMPORTANTE:** Si ves errores o el archivo está vacío, descárgalo de nuevo desde Firebase Console

### 7.4 Sincronizar Gradle
1. Ve a **File > Sync Project with Gradle Files**
2. Espera a que termine la sincronización (puede tardar 1-2 minutos)
3. Verifica que no haya errores en la consola de Gradle

**Errores comunes:**
- Si dice "google-services.json not found" → el archivo no está en `app/`
- Si dice "package name mismatch" → el nombre del paquete en Firebase Console es diferente

### 7.5 Primera compilación
1. Ve a **Build > Make Project** (o presiona Ctrl+F9)
2. Espera a que compile
3. Verifica que compile sin errores

✅ **Si compila exitosamente, Firebase está CORRECTAMENTE configurado!**

---

## 📌 PASO 8: Probar la App

### 8.1 Conectar dispositivo o emulador
1. Conecta tu teléfono Android por USB **O**
2. Inicia un emulador de Android

### 8.2 Ejecutar la app
1. Haz clic en el botón **Run** ▶️ (o presiona Shift+F10)
2. Selecciona tu dispositivo
3. Espera a que se instale y ejecute

### 8.3 Prueba inicial
1. La app debería abrir normalmente
2. Abre el menú hamburguesa (☰)
3. Selecciona **"Publicaciones"**
4. Deberías ver la pantalla vacía con el mensaje "No hay artículos publicados"
5. Toca el botón **+** flotante
6. Completa el formulario:
   - Título: "Artículo de prueba"
   - Descripción: "Este es un test"
   - Precio: 100
   - Selecciona una imagen de tu galería
7. Toca **"Guardar"**

### 8.4 Verificar en Firebase Console
1. Vuelve a Firebase Console en el navegador
2. Ve a **Firestore Database > Datos**
3. Deberías ver una colección llamada **"articles"**
4. Expándela y verás tu artículo de prueba

5. Ve a **Storage > Files**
6. Deberías ver una carpeta **"article_images"**
7. Dentro verás tu imagen subida

✅ **Si ves los datos en Firebase Console, TODO ESTÁ FUNCIONANDO PERFECTAMENTE!**

---

## 🎉 ¡CONFIGURACIÓN COMPLETADA!

Tu app MercadoCarmen ahora está completamente integrada con Firebase:
- ✅ Firestore para almacenar artículos
- ✅ Storage para guardar imágenes
- ✅ Sincronización en tiempo real
- ✅ CRUD completo funcionando

---

## 🆘 Solución de Problemas Comunes

### Error: "google-services.json not found"
**Solución:** El archivo no está en la ubicación correcta
- Debe estar en: `app/google-services.json`
- Vuelve al PASO 3.3 y colócalo correctamente

### Error: "Package name mismatch"
**Solución:** El nombre del paquete no coincide
- Verifica en `google-services.json` que diga: `"package_name": "com.example.mercadocarmen"`
- Si es diferente, crea una nueva app en Firebase Console con el nombre correcto

### Error: "Failed to get Firebase Storage"
**Solución:** Storage no está habilitado
- Ve a Firebase Console > Storage
- Habilita el servicio (PASO 5)

### Error al subir imágenes
**Solución:** Verifica las reglas de Storage
- Ve a Storage > Reglas
- Asegúrate de que estén en modo de prueba
- La fecha debe ser futura

### No se ven los artículos en la app
**Solución:** Verifica Firestore
- Ve a Firestore Database > Datos
- Revisa si hay datos en la colección "articles"
- Verifica las reglas de Firestore

---

## 📞 ¿Necesitas Ayuda?

Si tienes algún problema durante el proceso:
1. Revisa esta guía desde el principio
2. Verifica cada checkbox de verificación
3. Consulta la sección de problemas comunes
4. Pídeme ayuda con el error específico que ves

---

**¡Listo para empezar a usar tu app con Firebase!** 🚀
