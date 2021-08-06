package com.unitec.holamundo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        Toast.makeText(this, "registros: ${usuario.localizacion?.size}", Toast.LENGTH_LONG).show()

     var indiceMaximo=usuario.localizacion?.size

        var ultimosCinco=ArrayList<Localizacion>()
      //Vamos a iterar  o recorrer ese arreglo para que nos de los ultimos 5 registros
      usuario.localizacion?.forEachIndexed { index, localizacion ->
          if(index>=indiceMaximo?.minus(5)!!){
              ultimosCinco.add(localizacion)
             //Imprimimos en el Logcat ese elemento
             Log.i("xx","Ultimos cinco ${localizacion.fecha.toString()} ")
          }

      }
    //Aqui en esta parte del codigo lo unico ue tienen que hacer es mostrar
// los marcadores con las ultimas 5 localizaciones del usuario seleccionado
 // del arreglo ultimosCinco.




        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {

// Aqui dentro debemos de agregar ls marcadores correspondientes a las 10 ultimas
   // ubicaciones de cada usuario seleccionado.



            }
        }
    }
}