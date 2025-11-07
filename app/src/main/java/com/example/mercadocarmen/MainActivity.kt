package com.example.mercadocarmen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Encontrar las vistas del layout
        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val navView: NavigationView = findViewById(R.id.nav_view)

        // 2. Configurar el Toolbar como el ActionBar de la app
        setSupportActionBar(toolbar)

        // ¡Importante! Ocultar el título por defecto que podría poner el toolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 3. Crear el "toggle" (el icono de hamburguesa)
        // Esto conecta el Toolbar (para el clic) y el DrawerLayout (para abrir/cerrar)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        // 4. Añadir el listener al DrawerLayout y sincronizar el icono
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // (Opcional) Manejar clics en los ítems del menú
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_inicio -> {
                    // Lógica para "Inicio"
                }
                R.id.nav_perfil -> {
                    // Lógica para "Mi perfil"
                }
                R.id.nav_publicaciones -> {
                    // Abrir la gestión de artículos
                    val intent = Intent(this, ArticleManagementActivity::class.java)
                    startActivity(intent)
                }
                // ... etc.
            }
            // Cierra el menú después de un clic
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    // (Opcional) Si el usuario presiona "atrás", primero cierra el menú si está abierto
    fun onBackPressedd() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}