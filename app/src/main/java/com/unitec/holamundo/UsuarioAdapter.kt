package com.unitec.holamundo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class UsuarioAdapter(private val onClick:(Usuario)->Unit)
    :ListAdapter<Usuario,UsuarioAdapter.UsuarioViewHolder>(UsuarioCallback) {
        /*
         La siguiente es la clas UsuarioViewHolder  en ingles holder es contenedor, y debe su nombre
         a que va a contener la ADAPTACION DEL modelo Usuario y su layout usuario_item
         */
        class UsuarioViewHolder(itemView: View, val onClick: (Usuario) -> Unit ):
         RecyclerView.ViewHolder(itemView) {
             //Aqui siguen lo atributos que SIEMPRE deben ser los mismos tipos que de layout usuario_item
             private val usuarioNombre: TextView=itemView.findViewById(R.id.txtNombre)
            private  val usuarioEmail:TextView=itemView.findViewById(R.id.txtEmail)
            private  val usuarioNickname :TextView =itemView.findViewById(R.id.txtNickname)
            //declaramos el modelo donde se van a ADAPTAR esos items
            private  var usuarioActual:Usuario?=null
            //A continucion vamos a inicializar el segundo argumento del constructor (el OnClick)
            //usndo un bloque inicialziado rpoque se trata de un evento
            init {
                itemView.setOnClickListener {
                    usuarioActual?.let{
                        onClick(it)
                    }
                }
            }//Llave de cierre del bloque inicializador
            //Aqui enlazamos a el usaurio actual
            fun  enlazar(usuario:Usuario){
                usuarioActual=usuario
               //Deespues iniciamos los valores
               usuarioNombre.text=usuario.nombre
               usuarioEmail.text=usuario.email
               usuarioNickname.text=usuario.nickname

            }
         }//Llave de cierre de la clase internal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        //Borre el codiguito de este metodo o funcion
        val view =LayoutInflater.from(parent.context)
            .inflate(R.layout.usuario_item, parent, false)
        return UsuarioViewHolder(view, onClick)

    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
      val usuario=getItem(position)
        holder.enlazar(usuario)
    }


}//Llave de la clase externa

//Implementamos la classe UsuarioCallback
object  UsuarioCallback:DiffUtil.ItemCallback<Usuario>(){
    override fun areItemsTheSame(oldItem: Usuario, newItem: Usuario): Boolean {
        return oldItem==newItem
    }

    override fun areContentsTheSame(oldItem: Usuario, newItem: Usuario): Boolean {
        return oldItem.email==newItem.email
    }

}
