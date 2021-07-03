package com.unitec.holamundo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/*
 La siguiente anotacion se debe utilizar cuando vas a enviar este objeto
 al back end y no vas a enviar todos los datos, por ejemplo, cuando nos
 registramos, no enviamos la localizacion.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class Usuario {
    var nombre:String?=null
    var email:String?=null
    var nickname:String?=null
    var localizacion:ArrayList<Localizacion>?=null

}