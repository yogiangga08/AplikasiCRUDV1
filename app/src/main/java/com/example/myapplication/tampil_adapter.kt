package com.example.myapplication

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.myapplication.databinding.CostumTampilBinding
import org.json.JSONObject


class tampil_adapter(private val context: Context, results: ArrayList<model>) : RecyclerView.Adapter<tampil_adapter.MyViewHolder>() {

    private var Items = ArrayList<model>()

    init {
        this.Items = results

    }

    inner class MyViewHolder(val binding: CostumTampilBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return  MyViewHolder(
            CostumTampilBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val result = Items[position]
        with(holder){
            binding.tvNama.text = result.nama
            binding.tvAlamat.text = result.alamat
            if (result.jenis_kelamin == "L"){
                binding.tvJenisKelamin.text = "Laki-laki"
            }else{
                binding.tvJenisKelamin.text = "Perempuan"

            }
            binding.root.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Pilih Pengaturan")
                val pilihan = arrayOf("Edit", "Delete", "Cancel")
                builder.setItems(pilihan) { dialog, which ->
                    when (which) {
                        0 -> {
                            val a = Intent(context, edit::class.java)
                            a.putExtra("id", result.id_crud)
                            context.startActivity(a)
                        }

                        1 -> {
                            delete(result.id_crud)
                        }

                        2 -> {

                        }
                    }
                }
                val dialog = builder.create()
                dialog.show()
            }
        }


    }

    override fun getItemCount(): Int {
        return Items.size
    }

    fun delete(id_crud: String){
        val loading = ProgressDialog(context)
        loading.setMessage("Memuat data...")
        loading.show()
        AndroidNetworking.post("http://192.168.100.8/materi/crud/hapus.php")
            .addBodyParameter("id_crud", id_crud)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    loading.dismiss()
                    if (response.getInt("success") == 1) {
                        Toast.makeText(context, response.getString("pesan"), Toast.LENGTH_LONG).show()
                        (context as Activity).finish()
                    } else {
                        Toast.makeText(context, response.getString("pesan"), Toast.LENGTH_LONG).show()
                    }

                }

                override fun onError(error: ANError) {
                    loading.dismiss()

                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
                }
            })
    }
}