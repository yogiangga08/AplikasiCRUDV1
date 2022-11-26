package com.example.myapplication

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.myapplication.databinding.ActivityInputBinding
import org.json.JSONObject


class input : AppCompatActivity() {
    private lateinit var binding : ActivityInputBinding
    private var jk : String? = "L"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

       binding.rgJk.setOnCheckedChangeListener { group, checkedId ->
           if (checkedId == binding.rbL.id){
               jk = "L"
           }else {
               jk = "P"
           }
       }
       binding.btSimpan.setOnClickListener {
           val nama = binding.etNama.text.toString()
           val alamat = binding.etAlamat.text.toString()
           if (nama.isEmpty()){
               binding.etNama.error = "Kosong"
               binding.etNama.requestFocus()
           }else if (alamat.isEmpty()){
               binding.etNama.error = "Kosong"
               binding.etNama.requestFocus()
           }else{
               save(nama,alamat, jk.toString())
           }
       }
    }

    fun save(nama: String, alamat: String, jk: String){
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()
        AndroidNetworking.post("http://192.168.100.8/materi/crud/simpan.php")
            .addBodyParameter("nama", nama)
            .addBodyParameter("alamat", alamat)
            .addBodyParameter("jenis_kelamin", jk)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    loading.dismiss()

                    if (response.getInt("success") == 1){
                        Toast.makeText(this@input,response.getString("pesan"),Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@input,response.getString("pesan"),Toast.LENGTH_LONG).show()
                    }

                }

                override fun onError(error: ANError) {
                    loading.dismiss()

                    Toast.makeText(this@input,error.toString(),Toast.LENGTH_LONG).show()
                }
            })
    }
}