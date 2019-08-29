package com.example.firebase_notification_kotlin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.mylist.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), CanalInterface {

    private val TAG = "MainActivity"
    private val baseUrl = "http://192.168.1.124"
    private val baseUrl2 = "http://192.168.1.126"
    private var canales = listOf<Canal>()
    lateinit var usuario: Usuario
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val usertoken = FirebaseInstanceId.getInstance().getToken()
        usertoken?.let {
            usuario = Usuario(
                0,
                "olimpiacampeon1902",
                "Matias",
                "Orué",
                'F',
                it
            )
            println("User token: " + usertoken)
        }
        obtenerCanales()

        lista_canales.adapter = adapter

        FirebaseMessaging.getInstance().subscribeToTopic("global")


    }

    override fun onCanalClicked(canal: Canal, view: ImageView) {
        view.setImageResource(R.drawable.ic_star_black)
        subscribeToTopic(canal)
    }

    fun obtenerCanales() {
        val builder = Retrofit.Builder()
                        .baseUrl(baseUrl2)
                        .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder.build()

        val service = retrofit.create(ApiService::class.java)
        val canalCall = service.getCanales()

        canalCall.enqueue(object : Callback<List<Canal>> {
            override fun onResponse(call: Call<List<Canal>>, response: Response<List<Canal>>) {
                val temCanales = response.body()
                temCanales?.let {
                    canales = it
                    mostrarCanales()
                } ?: kotlin.run {
                    Toast.makeText(this@MainActivity, "No se encuentran canales disponibles", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<List<Canal>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error al obtener los canales", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun mostrarCanales() {
        canales.forEach { canal ->
            adapter.add(CanalItem(canal, this))
        }
    }

    fun subscribeToTopic(canal: Canal) {

        FirebaseMessaging.getInstance().subscribeToTopic(canal.nombre)
            .addOnCompleteListener { task ->
                var msg = getString(R.string.msg_subscribed)
                if (!task.isSuccessful) {
                    msg = getString(R.string.msg_subscribe_failed)
                }
                Log.d(TAG, msg)
                println("Suscripto a: " + canal.nombre)
                Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
            }
        registerToChannel(canal.id, usuario.id)
    }

    private fun registerToChannel(canalId: Int, userId: Int) {
        val inscripcion = CanalPorUsuario(userId, canalId)

        val builder = Retrofit.Builder()
            .baseUrl(baseUrl2)
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder.build()

        val service = retrofit.create(ApiService::class.java)
        val canalCall = service.suscribirseCanal(inscripcion)

        canalCall.enqueue(object : Callback<CanalPorUsuarioResponse> {
            override fun onResponse(call: Call<CanalPorUsuarioResponse>, response: Response<CanalPorUsuarioResponse>) {
                Toast.makeText(this@MainActivity, "Registrado al canal", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<CanalPorUsuarioResponse>, t: Throwable) {
                Log.e("Error", "On Failure Canal" + t.localizedMessage)
                Toast.makeText(this@MainActivity, "Error al registrarse a canal", Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun registerToServer(view: View) {

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        val service = retrofit.create(ApiService::class.java)
        try {
            val usuarioCall = service.altaUsuario(usuario)
            println("Usuario Enviado " + usuario.toString())
            usuarioCall.enqueue(object : Callback<UsuarioResponse> {
                override fun onResponse(call: Call<UsuarioResponse>, response: Response<UsuarioResponse>) {
                    val id = response.body()
                    println(response.body().toString())
                    id?.let {
                        usuario.id = it.id
                        Toast.makeText(this@MainActivity, "Correcto! usuario id: ${usuario.id}", Toast.LENGTH_SHORT)
                            .show()

                    } ?: kotlin.run {
                        Toast.makeText(
                            this@MainActivity,
                            "No se ha podido obtener el ID del usuario",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UsuarioResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Algo ha salido mal", Toast.LENGTH_SHORT).show()
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


}

interface CanalInterface {
    fun onCanalClicked(canal: Canal, view: ImageView)
}

class CanalItem(val canal: Canal, val listener: CanalInterface) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.mylist
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.nombre_canal.text = canal.nombre
        viewHolder.itemView.setOnClickListener { listener.onCanalClicked(canal, viewHolder.itemView.suscripto) }
    }

}
