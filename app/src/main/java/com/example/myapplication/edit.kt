package com.example.myapplication

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.myapplication.databinding.ActivityEditBinding
import org.json.JSONObject

class edit : AppCompatActivity() {
    private lateinit var binding : ActivityEditBinding
    private var jk : String? = "L"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.getStringExtra("id") != null){
            find(intent.getStringExtra("id").toString())
        }

        binding.rgJk.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == binding.rbL.id){
                jk = "L"
            }else {
                jk = "P"
            }
        }
        binding.btEdit.setOnClickListener {
            val nama = binding.etNama.text.toString()
            val alamat = binding.etAlamat.text.toString()
            if (nama.isEmpty()){
                binding.etNama.error = "Kosong"
                binding.etNama.requestFocus()
            }else if (alamat.isEmpty()){
                binding.etNama.error = "Kosong"
                binding.etNama.requestFocus()
            }else{
                update(intent.getStringExtra("id").toString(),nama,alamat, jk.toString())
            }
        }

    }

    fun find (id_crud : String){
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()
        AndroidNetworking.get("http://192.168.100.8/materi/crud/cari.php")
            .addQueryParameter("id_crud", id_crud)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    loading.dismiss()
                    if (response.getInt("success") == 1){
                        val jsonobjcet = response.optJSONObject("data")
                        binding.etNama.setText(jsonobjcet.getString("nama"))
                        binding.etAlamat.setText(jsonobjcet.getString("alamat"))
                        if (jsonobjcet.getString("jenis_kelamin") == "L"){
                            binding.rbL.isChecked = true
                            jk = "L"
                        }else{
                            binding.rbP.isChecked = true
                            jk = "P"

                        }
                    }else{
                        Toast.makeText(this@edit,response.getString("pesan"), Toast.LENGTH_LONG).show()
                    }

                }

                override fun onError(error: ANError) {
                    loading.dismiss()

                    Toast.makeText(this@edit,error.toString(), Toast.LENGTH_LONG).show()
                }
            })
    }

    fun update(id_crud: String, nama: String, alamat: String, jk: String){
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()
        AndroidNetworking.post("http://192.168.100.8/materi/crud/edit.php")
            .addBodyParameter("id_crud", id_crud)
            .addBodyParameter("nama", nama)
            .addBodyParameter("alamat", alamat)
            .addBodyParameter("jenis_kelamin", jk)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    loading.dismiss()

                    if (response.getInt("success") == 1){
                        Toast.makeText(this@edit,response.getString("pesan"),Toast.LENGTH_LONG).show()
                        finish()
                    }else{
                        Toast.makeText(this@edit,response.getString("pesan"),Toast.LENGTH_LONG).show()
                    }

                }

                override fun onError(error: ANError) {
                    loading.dismiss()

                    Toast.makeText(this@edit,error.toString(),Toast.LENGTH_LONG).show()
                }
            })
    }
}