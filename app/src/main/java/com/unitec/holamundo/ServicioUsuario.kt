package com.unitec.holamundo

import retrofit2.Call
import retrofit2.http.GET

/*
Nota: todos los imports son de retroit, por ello ya debes tener tus dependencias
 */
interface ServicioUsuario {

    @GET("api/usuario")
    fun buscarTodos():Call<ArrayList<Usuario>>
}