package com.unitec.holamundo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
//Los siguientes imports  son los relacionados a activacion y uso de localziacion por GPS
import com.mapbox.mapboxsdk.location.*
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import kotlinx.android.synthetic.main.activity_mapita.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.lang.Exception
//La siguiente linea les puede
import  java.lang.ref.WeakReference
import java.util.*;



class MapitaActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener{
    //El siguiente atributo sirve para activar el administrador de permisos

    var permissionsManager: PermissionsManager=PermissionsManager(this)

    //Los siguientes atributos indica el tiempo que quieres que este activandose la localizacion
    private  val INTERVALO_DEFECTO_EN_MISISEGUNDOS=1000L
    private val TIEMPO_ESPERA_DEFECTO=5000L

    //La siguiente variable sirve para gestionar el GPS, es decir activa fisicamente el dispositivo
     private var locationEngine: LocationEngine?=null

    //El siguiente objeto se va a estar invocando en tiempo indicado en milisegundos, para efectuar las localizaciones
    private val callback:LocationChangeListeningActivityLocationCallback=
        LocationChangeListeningActivityLocationCallback(this);

    //Lo generamos coomo clase interna:
    inner class LocationChangeListeningActivityLocationCallback internal constructor(activity:MapitaActivity?):
    LocationEngineCallback<LocationEngineResult>{

        private var referenciaDebil:WeakReference<MapitaActivity>?=null
        private var locationEngine:LocationEngine?=null

        override fun onSuccess(result: LocationEngineResult?) {
         //Este metodo es muy imprtante, ya que de ser exitosa la activacion del GPS, de forma automatica estara
          //  conectandose a los datos del GPS, en este caso le pusimos que fuera cada segundo.

            var activity:MapitaActivity=referenciaDebil?.get()!!
            //Si ya se inicializo el mapitaActivity, es decir ssi ya se cargo la app, empezamos la busqueda
            if(activity!=null){
                //Aqui en esta parte tambien vamos a poner mas adelante el servicio REST que pusimo en nuestra
                //ubicacion fake
                //Primero pedimos la ubicacion y a partir de ella generamos a nuestro usuario


                //CReamos la vaarirable de tipo Localizacion
                var loca=Localizacion()
                loca.lat=result?.lastLocation!!.latitude
                loca.lng=result?.lastLocation!!.longitude
                //CReamos el usuario fake
                var usuario=Usuario();
                var arregloLoca=ArrayList<Localizacion>()
                //agregamos la localizacion
                arregloLoca.add(loca)
                //Agregamos el email fake que tiene que ser el que  esribist para tu registro
                usuario.email="rapidclimate@gmail.com"
                usuario.localizacion=arregloLoca;
                //Con esto ya podemos generar nuestro objeto a enviar!!!
                //Para ello debemos crear una corutina de tipo IO para enviar la info
                //y una de tipo MAIN, para recibir.
                GlobalScope.launch(Dispatchers.IO){
                    //Primero construimos el objeto retrofit para poder  trabaa el servicio
                    var retrofit = Retrofit.Builder()
                        .baseUrl("https://localiza-amigos2.herokuapp.com/")
                        .addConverterFactory(JacksonConverterFactory.create())
                        .build()
                    //Ahora si, creamos la variable de nuestro servicio a partir del objeto retorfi
                    var servicioUsuario=retrofit.create(ServicioUsuario::class.java)
                    var hacerEnvio =servicioUsuario.guardarLoca(usuario)
                    //Preparamos el retorno del servicio
                    var estatus=hacerEnvio.execute().body()!!

                    //CReamos de una vez la de respuesta

                    launch(Dispatchers.Main){
                        Toast.makeText(applicationContext, estatus.mensaje, Toast.LENGTH_LONG).show()
                    }

                }

            //Checamos la condicion de que el mapa contenga la referencia a la uiltima
            //localiacion, para que siempre se centre alli
            if(activity.mapboxMap!=null&&result.lastLocation!=null)    {
                //Felizmente mostramos la ubicacion en el mapa!!!, la REAL!!
                activity.mapboxMap.locationComponent.forceLocationUpdate(result.lastLocation)

              }

            }

        }

        override fun onFailure(exception: Exception) {
        //Este metodo de la interface que heredamos se activa en caso de que no se hayan concedido los
            //permisos de uso de GPS
            var activity:MapitaActivity=referenciaDebil?.get()!!
            if(activity!=null){
                Toast.makeText(activity, exception.message, Toast.LENGTH_LONG).show()
            }
        }
        init {
            referenciaDebil= WeakReference(activity)
        }

    }


    private lateinit var mapboxMap: MapboxMap

    //Localizacion


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
            habilitarLocalizacion(it)
        }
    }

    //Implementacion del metodo habilitraLOcalziacion la anotacion que lleva
    //sirvve ara gestionar en la intefaz de usuario la caja de dialogo de permisos
    @SuppressLint("MissingPermission")
    private fun habilitarLocalizacion(estiloMapa:Style){
        //Aqui checamos si el permiso fue otorgado
        if(PermissionsManager.areLocationPermissionsGranted(this)){
            //Aqui se personaliza las opciones de localizacion, esto es mejor que google
            val opcionesLocaPersonalizada=LocationComponentOptions.builder(this)
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(this,R.color.teal_200))
                .build()
            //El siguiente objto activa las opciones l anterior
            val activarOpciones=LocationComponentActivationOptions.builder(this,estiloMapa)
                .locationComponentOptions(opcionesLocaPersonalizada)
                .build()
            //Finalmente obtebemos la instancia del mapa para que lo actualice con los valores
            //anteriores
            mapboxMap.locationComponent.apply {
                //Primero activamos las opciones de localizacion
                activateLocationComponent(activarOpciones)
                //Verificamos que la localizacion este en true
                isLocationComponentEnabled=true
                //POnemos la camara de tracking activada
                cameraMode= CameraMode.TRACKING

                //Activitamos una brujulita, esta me gusta porque no la tiene y nos ayuda a ver el norte
                renderMode=RenderMode.COMPASS

                //Invocamos la localziacion automatica!!
                iniciarLocalizacion()
            }
        }else{
            permissionsManager= PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }
//Finalemente implementamos el metodo iniciarLocalizacion, tambien debe tener la anotacion de permisos concedidos
    @SuppressLint("MissingPermission")
    private fun iniciarLocalizacion(){
    //Aqui le damos ordenes al GPS
        locationEngine=LocationEngineProvider.getBestLocationEngine(this)
    val request =LocationEngineRequest.Builder(INTERVALO_DEFECTO_EN_MISISEGUNDOS)
        .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
        .setMaxWaitTime(TIEMPO_ESPERA_DEFECTO)
        .build()

    //INvocamos el metodo que actualiza con los valores anteriores
    locationEngine?.requestLocationUpdates(request,callback,mainLooper)
    //Invocamos la ultima posicin del gps
    locationEngine?.getLastLocation(callback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        //Aqui un mensajito que vera si niega la cajita de dialogo de los permisos
        Toast.makeText(this, "Necesitas permitir uso de GPS", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if(granted){
            habilitarLocalizacion(mapboxMap.style!!)
        }else{
            Toast.makeText(this, "NO concediste el permiso, imposible localizarte", Toast.LENGTH_LONG)
                .show()
            finish()
        }
    }


}