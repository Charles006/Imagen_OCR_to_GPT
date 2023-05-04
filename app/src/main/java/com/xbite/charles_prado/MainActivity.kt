package com.xbite.charles_prado

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScream()
        setContentView(R.layout.activity_main)
        requestAllPermissions()
    }

    private val multiPermissionCallback =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
             if ( map.entries.size <3){
                 Toast.makeText(this, "Please Accept all the permissions", Toast.LENGTH_SHORT).show()
             }
        }


    private fun requestAllPermissions(){
       multiPermissionCallback.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        )
    }

    private fun fullScream(){
        // Desactivar la barra de título de la ventana
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Establecer la aplicación en modo pantalla completa
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

}