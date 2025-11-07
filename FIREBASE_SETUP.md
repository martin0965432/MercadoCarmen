# Configuración de Firebase para MercadoCarmen

## 📋 Pasos para Configurar Firebase

### 1. Crear Proyecto en Firebase Console

1. Ve a [Firebase Console](https://console.firebase.google.com/)
2. Haz clic en "Agregar proyecto" o "Add project"
3. Nombre del proyecto: **MercadoCarmen**
4. Acepta los términos y continúa
5. Desactiva Google Analytics (opcional para este proyecto)
6. Espera a que se cree el proyecto

### 2. Agregar una App Android

1. En la página principal del proyecto, haz clic en el ícono de Android
2. Registra tu app con estos datos:
   - **Nombre del paquete**: `com.example.mercadocarmen`
   - **Alias de la app** (opcional): MercadoCarmen
   - **Certificado SHA-1** (opcional por ahora, necesario para Auth más adelante)

3. Haz clic en "Registrar app"

### 3. Descargar google-services.json

1. Descarga el archivo `google-services.json`
2. **IMPORTANTE**: Coloca el archivo en:
   ```
   MercadoCarmen/app/google-services.json
   ```
3. El archivo debe estar en el directorio `app`, NO en `app/src`

### 4. Configurar Firestore Database

1. En el menú lateral de Firebase Console, selecciona **"Firestore Database"**
2. Haz clic en "Crear base de datos" o "Create database"
3. Selecciona **"Modo de prueba"** (Test mode) para desarrollo
4. Elige la ubicación más cercana (por ejemplo: `us-central` o `southamerica-east1`)
5. Haz clic en "Habilitar"

**Reglas de seguridad iniciales** (ya están configuradas en modo test):
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.time < timestamp.date(2025, 12, 31);
    }
  }
}
```

### 5. Configurar Firebase Storage

1. En el menú lateral, selecciona **"Storage"**
2. Haz clic en "Comenzar" o "Get started"
3. Acepta las reglas de seguridad en **modo de prueba**
4. Selecciona la misma ubicación que Firestore
5. Haz clic en "Listo"

**Reglas de seguridad iniciales**:
```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if request.time < timestamp.date(2025, 12, 31);
    }
  }
}
```

### 6. (Opcional) Configurar Authentication

Si más adelante quieres agregar autenticación:

1. En el menú lateral, selecciona **"Authentication"**
2. Haz clic en "Comenzar"
3. Habilita el proveedor **"Correo electrónico/contraseña"**
4. Guarda los cambios

## 🔧 Verificación de la Configuración

Después de colocar el archivo `google-services.json`:

1. Sincroniza Gradle (Sync Now)
2. Compila el proyecto
3. Si ves errores, verifica que:
   - El archivo `google-services.json` esté en `app/`
   - El nombre del paquete coincida: `com.example.mercadocarmen`
   - Firebase Firestore y Storage estén habilitados en la consola

## 📊 Estructura de Datos en Firestore

El proyecto creará esta estructura automáticamente:

```
firestore/
└── articles/
    ├── {articleId}/
    │   ├── id: String
    │   ├── title: String
    │   ├── description: String
    │   ├── price: Number
    │   ├── imageUrl: String
    │   └── dateCreated: Timestamp
```

## 📁 Estructura de Firebase Storage

Las imágenes se guardarán en:

```
storage/
└── article_images/
    └── {timestamp}_{randomId}.jpg
```

## 🔒 Reglas de Producción (Para más adelante)

Cuando estés listo para producción, actualiza las reglas:

**Firestore**:
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /articles/{articleId} {
      allow read: if true;  // Cualquiera puede leer
      allow write: if request.auth != null;  // Solo usuarios autenticados pueden escribir
    }
  }
}
```

**Storage**:
```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /article_images/{imageId} {
      allow read: if true;
      allow write: if request.auth != null
                   && request.resource.size < 5 * 1024 * 1024  // Max 5MB
                   && request.resource.contentType.matches('image/.*');
    }
  }
}
```

## ✅ ¡Listo!

Una vez completados estos pasos, la app estará lista para usar Firebase.
