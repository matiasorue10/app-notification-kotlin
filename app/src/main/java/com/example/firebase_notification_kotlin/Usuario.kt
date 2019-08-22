package com.example.firebase_notification_kotlin

import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.annotations.Expose

data class Usuario (var id: Int = 0,
                    @Expose(serialize = true, deserialize = true) val usuario: String,
                    @Expose(serialize = true, deserialize = true) val nombre: String,
                    @Expose(serialize = true, deserialize = true) val apellido: String,
                    @Expose(serialize = true, deserialize = true) val firebase: Boolean = true,
                    @Expose(serialize = true, deserialize = true) val token_firebase: String? = FirebaseInstanceId.getInstance().getToken()) {

}