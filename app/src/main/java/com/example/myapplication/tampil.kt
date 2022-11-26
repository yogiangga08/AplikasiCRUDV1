package com.example.myapplication

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.myapplication.databinding.ActivityTampilBinding
import org.json.JSONObject


class tampil : AppCompatActivity() {
    private lateinit var  binding: ActivityTampilBinding
    var result = ArrayList<model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTampilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTampil.setHasFixedSize(true)
        binding.rvTampil.layoutManager = LinearLayoutManager(this)


    }

    override fun onPostResume() {
        super.onPostResume()
        show()
    }
    fun show (){
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()
        AndroidNetworking.get("http://192.168.100.8/materi/crud/tampil.php")
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    loading.dismiss()
                    result.clear()
                    if (response.getInt("success") == 1){
                        val jsonArray = response.optJSONArray("data")

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.optJSONObject(i)
                            result.add(
                                model(
                                    jsonObject.getString("id_crud"),
                                    jsonObject.getString("nama"),
                                    jsonObject.getString("alamat"),
                                    jsonObject.getString("jenis_kelamin")

                                )
                            )
                        }
                        val adapter = tampil_adapter(this@tampil, result)
                        binding.rvTampil.adapter = adapter
                    }else{

                        Toast.makeText(this@tampil,response.getString("pesan"),Toast.LENGTH_LONG).show()
                    }

                }

                override fun onError(error: ANError) {
                    loading.dismiss()
                    Log.d("tampil", error.toString())
                }
            })
    }
}