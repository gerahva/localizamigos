package com.unitec.holamundo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_detalles.*

class DetallesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, getString(R.string.miToken))


        setContentView(R.layout.activity_detalles)

        var usuario=    intent.extras?.getSerializable("usuario") as Usuario

        Toast.makeText(this, "registros ${usuario.localizacion?.size}", Toast.LENGTH_LONG).show()



        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {

// Aqui dentro debemos de agregar ls marcadores correspondientes a las 10 ultimas
   // ubicaciones de cada usuario seleccionado.



            }
        }
    }
}