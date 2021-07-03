package com.unitec.holamundo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

/*
Dentro de un proyecto android que usa el patron de Diseno MVP la  MainActivity co
rresponde a la letra P. Es la cala Presenter
Presenter.-Su funcion de esta capa es enlazar los datos de la interfce de usuario y el codigo de la app
Model.- Su funcion  es contener EXCLUSIVAMENTE la logica o codigo de la app.
View.- Su funciones contener EXCLUCIVAMENTE a la interface de usuario (EN android no se usa usa codigo para
construir la Interface de Usuario).

 */
class MainActivity : AppCompatActivity() {
    /*
     El signo de interrogacion se usa muchisimo el kotlin y es de hecho la razon de origen de kotlin
     y sirve para envitar el malevolo NullPointerExeption
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        /**
        Para invocar una componente de la vista usamos el siguiente metodo
       var btnRegistrar=  findViewById<Button>(R.id.registrar)
        btnRegistrar.setOnClickListener {

         }
       */
         //La siguiente linea hace lo mismo pero es mas expresiva
                 registrar.setOnClickListener{
                      //Vamos a crear un objeto de tipo Intent
                     var i=Intent(this, RegistroAcivity::class.java)
                     startActivity(i)
                 }
 //Manejo de evento del MapitaAcivity
        ubicar.setOnClickListener {
          startActivity(Intent(this, MapitaActivity::class.java))
          /*************************************************************
            A continuacion vamos a enviar un usuario fake con su localizacion fake
           para verificar que el back end lo reciba y lo guarde
           NOTA: El usuario debe ya existir en la base de datos.
           *****************************************************************/
             //Primero generamos una localizacion fake: =19.412497,-99.145667
            var loca=Localizacion();
            loca.lat=19.412497
            loca.lng=-99.145667
            //CReamos el usuario fake
            var usuario=Usuario();
            var arrgloLoca=ArrayList<Localizacion>()
            //agregamos la localizacion
            usuario.localizacion=arrgloLoca
            //Agregamos el email fake
            usuario.email="rapidclimate@gmail.com"
            //En la siguiente parte enviamos al back end a este usuario a
            //api/localizaciones


        }

        //Este codigo no se relaciona al proycto es para explicar una lambda
        //Vamos a comprobar que una funcion en los lenguajes funcionales es un
        //tipo de dato y se trata como tal en todo kotlin
             var funcion=    {x:Int->x*2}
        Log.i("JC", "Vamos a invocar nuestra funcion(dato) ${funcion.invoke(5)} y listo")
             var suma=4+5
        var  sumar = {x:Int,y:Int  ->  x+y}

        Log.i("JC", "La variable suma es $suma y eso es todo")
        Log.i("JC", "La expresion no se invoca ${sumar(4,2)} y eso es todo")

        //Una constante en ktlin se declara con la palabra val
        val z=5;
       // Marca error de compilacion. NO se hace porque es constante:  z=10

        //En kotlin existen muchas formas de declarar variables
        var b=5.4F
        var c:Double
        var correcto:Boolean
        var hola:String






    }
}