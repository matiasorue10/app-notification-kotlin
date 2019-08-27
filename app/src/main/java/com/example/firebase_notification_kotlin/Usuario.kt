package com.example.firebase_notification_kotlin

import com.google.gson.annotations.Expose

data class Usuario(
    var id: Int = 0,
    @Expose(serialize = true, deserialize = true) val usuario: String,
    @Expose(serialize = true, deserialize = true) val nombre: String,
    @Expose(serialize = true, deserialize = true) val apellido: String,
    @Expose(serialize = true, deserialize = true) val origen: Char,
    @Expose(serialize = true, deserialize = true) val token: String?
) {

}