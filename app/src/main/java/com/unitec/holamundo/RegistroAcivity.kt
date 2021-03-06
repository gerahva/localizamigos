package com.unitec.holamundo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_registro.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class RegistroAcivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        //Aqui vamo a invocar nuestro id del boton registrarse
         registrarme.setOnClickListener {
             //Aqui este bloque vamos a pedirle a nuestros campos de texto sus
             //los contenidos escritos en ellos y con ellos creamos el OBJETO Usuario
              var usuario=Usuario()

            //pOR ESTOS TRES RENGLONES A LA ACTIVITY SE LE DENOMINA pRESENTER:
            //PORQUE PRESENTA AL MODELO SUS ASIGNACIONS OBTENIDAS A PARTIR DE LA UI.
             usuario.nombre=txtNombre.text.toString()
             usuario.email=txtEmail.text.toString()
             usuario.nickname=textoNickname.text.toString()

             //Esto ya lo tenemos que mandar al back end para que guarde!!!!
             //UUUUYYYY que miedo!!!
             //Todavia no vamos a registrar, simplemente vamos a ver si obtenemos el usuario
             //de la base de datos.
             //Corutina.
             GlobalScope.launch(Dispatchers.IO){
                 var retrofit=Retrofit.Builder()
                     .baseUrl("https://localiza-amigos2.herokuapp.com/")
                     .addConverterFactory(JacksonConverterFactory.create())
                     .build()
               //Generamos el servicio de nuestra clase serviciousuario
               var servicioUsuario=retrofit.create(ServicioUsuario::class.java)
               //ahora si, invocamos a nuestro metodo buscarTodos
        var hacerEnvio=servicioUsuario.registrarse(usuario)

         var estatus=      hacerEnvio.execute().body()!!
                 //finalemente nos enlazamos a la interfaz de usuario por medio
                 //de otra corutina ya que debe estar en thread separado
                 launch(Dispatchers.Main) {
                     //Vamos a poner un Toast, el cual es un  mnsaje de corta duracion
                     //simplemente para que nos de y muestre los datos
                 Toast.makeText(applicationContext,estatus.mensaje, Toast.LENGTH_LONG).show()

                 }


             }


         }

    }
}