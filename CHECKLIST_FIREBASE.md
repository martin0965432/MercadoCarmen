# ✅ Checklist de Configuración Firebase

Marca cada paso a medida que lo completes:

## 🔥 Firebase Console

### Crear Proyecto
- [ ] Ir a https://console.firebase.google.com/
- [ ] Crear proyecto con nombre: **MercadoCarmen**
- [ ] Desactivar Google Analytics
- [ ] Esperar a que se cree el proyecto

### Registrar App Android
- [ ] Hacer clic en ícono de Android
- [ ] Ingresar nombre de paquete: `com.example.mercadocarmen`
- [ ] Registrar app

### Descargar Archivo de Configuración
- [ ] Descargar `google-services.json`
- [ ] Copiar archivo a: `MercadoCarmen\app\google-services.json`
- [ ] ⚠️ Verificar que esté en `app\` NO en `app\src\`

### Configurar Firestore
- [ ] Ir a "Firestore Database" en menú lateral
- [ ] Crear base de datos
- [ ] Seleccionar **Modo de prueba**
- [ ] Elegir ubicación: `us-central` o `southamerica-east1`
- [ ] Esperar habilitación

### Configurar Storage
- [ ] Ir a "Storage" en menú lateral
- [ ] Comenzar configuración
- [ ] Aceptar modo de prueba
- [ ] Elegir **la misma ubicación** que Firestore
- [ ] Esperar habilitación

## 💻 Android Studio

### Verificación de Archivo
- [ ] Abrir Android Studio
- [ ] Abrir proyecto MercadoCarmen
- [ ] Cambiar vista a "Project"
- [ ] Verificar que `google-services.json` esté en carpeta `app/`

### Compilación
- [ ] File > Sync Project with Gradle Files
- [ ] Esperar sincronización (sin errores)
- [ ] Build > Make Project
- [ ] Verificar que compile correctamente

### Primera Prueba
- [ ] Conectar dispositivo o iniciar emulador
- [ ] Run > Run 'app'
- [ ] App se ejecuta sin errores
- [ ] Menú hamburguesa se abre
- [ ] Opción "Publicaciones" visible

### Prueba de Firebase
- [ ] Abrir "Publicaciones"
- [ ] Presionar botón +
- [ ] Llenar formulario de prueba
- [ ] Seleccionar imagen
- [ ] Guardar artículo
- [ ] Verificar que se guardó

### Verificación en Firebase Console
- [ ] Volver a navegador (Firebase Console)
- [ ] Ir a Firestore Database > Datos
- [ ] Ver colección "articles" creada
- [ ] Ver artículo de prueba
- [ ] Ir a Storage > Files
- [ ] Ver carpeta "article_images"
- [ ] Ver imagen subida

## 🎉 ¡Completado!

Si todos los checkboxes están marcados, tu configuración está PERFECTA.

---

## 🆘 En caso de error, verifica:

**Error más común:** google-services.json no encontrado
→ Solución: Copiar archivo a `app/google-services.json`

**Segundo error:** Package name mismatch
→ Solución: Verificar que en Firebase Console el paquete sea `com.example.mercadocarmen`

**Tercer error:** No se suben imágenes
→ Solución: Verificar que Storage esté habilitado en modo de prueba
