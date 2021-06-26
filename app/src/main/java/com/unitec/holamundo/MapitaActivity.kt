package com.unitec.holamundo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_mapita.*


class MapitaActivity : AppCompatActivity(), OnMapReadyCallback {
    //Declaramos una vaiable de tipo mapbox
    private lateinit var mapboxMap: MapboxMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //Aqui vamos a cargar el token publico
      //NOTA: ES MUY IMPORTANTE CARGARLO ANTES  DEL metodos setContentView()
      Mapbox.getInstance(this, getString(R.string.miToken) )
        setContentView(R.layout.activity_mapita)
        //Tenemos que generar el layout e invocar el id que le daremos al mapa...
        mapita.getMapAsync(this)

    }

    override fun onMapReady(mapboxMap: MapboxMap) {
//Invocamos la referencia que declaramos arriba de tipo  mapboxMap
        this.mapboxMap=mapboxMap
        this.mapboxMap.setStyle(Style.TRAFFIC_NIGHT){
            //Aqui dentro despues del bimestral vamos a invocar a un metodo que es el que nos va a estar
            //localziando...
        }





    }
}