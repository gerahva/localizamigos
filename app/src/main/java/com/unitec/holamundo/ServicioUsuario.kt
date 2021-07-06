package com.unitec.holamundo

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/*
Nota: todos los imports son de retroit, por ello ya debes tener tus dependencias
 */
interface ServicioUsuario {

    @GET("api/usuario")
    fun buscarTodos():Call<ArrayList<Usuario>>

    //El siguiente es el de registrarse
    @POST("api/usuario")
    fun registrarse(@Body usuario:Usuario ):Call<Estatus>

    @POST("api/localizaciones")
    fun guardarLoca(@Body usuario: Usuario):Call<Estatus>
}