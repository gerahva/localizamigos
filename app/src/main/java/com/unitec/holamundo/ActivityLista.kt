package com.unitec.holamundo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class ActivityLista : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        GlobalScope.launch(Dispatchers.IO){
            //REcuerda que este disptachers IO es para conectarse en una tarea asincronica al back end
            var retrofit= Retrofit.Builder()
                .baseUrl("https://localiza-amigos2.herokuapp.com/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build()

            //Invocamos els ervicios
            var servicioUsuarios=retrofit.create(ServicioUsuario::class.java)
            var hacerRequest=servicioUsuarios.buscarLocalizados()
            var usuarios=hacerRequest.execute().body()!!


            launch(Dispatchers.Main){
                val usuarioAdapter=UsuarioAdapter{usuario ->adapterOnClick(usuario)  }

                //INvocamos el recuclerView
                val recyclerView: RecyclerView =findViewById(R.id.recycler_view)
                //Inyectamos el recycler
                recyclerView.adapter=usuarioAdapter

                usuarioAdapter.submitList(usuarios)
            }
        }


    }
    //Finalmente agregamos el metodo adapterOnClick, el cual aciva el onClick de cada
    fun adapterOnClick(usuario:Usuario) {
        //Aqui tendriamos que inyectarle ora vista para que nos lleve a esa vista
        var intent= Intent(this, DetallesActivity::class.java)
        intent.putExtra("usuario",usuario)
        startActivity(intent)
        Toast.makeText(this, usuario.email, Toast.LENGTH_LONG).show()
    }
}