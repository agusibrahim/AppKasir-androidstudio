package com.agusibrahim.appkasir.Widget

import com.agusibrahim.appkasir.Model.Produk
import android.widget.TextView
import android.view.LayoutInflater
import com.agusibrahim.appkasir.R
import com.agusibrahim.appkasir.MainActivity
import android.content.ContentValues
import android.content.Context
import android.widget.Toast
import android.text.TextWatcher
import android.text.Editable
import android.view.View
import androidx.appcompat.app.AlertDialog

class ProdukDialog(ctx: Context?, dataset: Produk?) {
    init {
        var positiveTxt = "Tambahkan"
        val dlg = AlertDialog.Builder(
            ctx!!
        )
        val form = LayoutInflater.from(ctx).inflate(R.layout.produkdlg, null)
        val nama = form.findViewById<View>(R.id.namaproduk) as TextView
        val kodeprod = form.findViewById<View>(R.id.kodeproduk) as TextView
        val harga = form.findViewById<View>(R.id.harga) as TextView
        val stok = form.findViewById<View>(R.id.stok) as TextView
        // Jika dataset tidak null yang berarti itu adalah mode PEMBARUAN/EDIT
        // Maka kolom akan di isi, serta ditambabkan tombol Neutral (Hapus)
        if (dataset != null) {
            nama.text = dataset.nama
            kodeprod.text = dataset.sn
            harga.text = "" + dataset.harga
            stok.text = "" + dataset.stok
            positiveTxt = "Perbarui"
            dlg.setNeutralButton("Hapus", null)
        }
        dlg.setView(form)
        dlg.setTitle("$positiveTxt Produk")
        dlg.setPositiveButton(positiveTxt) { p1, p2 ->
            val data = ContentValues()
            data.put("nama", nama.text.toString())
            data.put("sn", kodeprod.text.toString())
            data.put("harga", harga.text.toString().toLong())
            data.put("stok", stok.text.toString().toInt())
            // Jika mode penambahan
            if (dataset == null) {
                MainActivity.dataproduk?.tambah(data)
                // Jika mode EDIT
            } else {
                MainActivity.dataproduk?.perbarui(dataset, data)
            }
        }
        dlg.setNegativeButton("Batal", null)
        val dialog = dlg.create()
        dialog.show()
        // Override tombol Hapus
        val hapusBtn = dialog.getButton(AlertDialog.BUTTON3)
        hapusBtn.setOnClickListener { p1 ->
            Toast.makeText(
                p1.context,
                "Tekan lama untuk menghapus",
                Toast.LENGTH_LONG
            ).show()
        }
        hapusBtn.setOnLongClickListener { p1 ->
            MainActivity.dataproduk?.hapus(dataset!!)
            dialog.dismiss()
            Toast.makeText(p1.context, "Terhapus", Toast.LENGTH_SHORT).show()
            false
        }
        // Dibawah ini adalah fungsi agar Button OK di disable jika data belum terisi atau jumlahnya kurang dari kriteria yang ditentukan
        // Gunakan textWatcher disetiap kolom
        val btn = dialog.getButton(AlertDialog.BUTTON1)
        if (dataset == null) btn.isEnabled = false
        val watcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p1: CharSequence, p2: Int, p3: Int, p4: Int) {}
            override fun onTextChanged(p1: CharSequence, p2: Int, p3: Int, p4: Int) {
                btn.isEnabled = nama.text.length > 3 && kodeprod.text.length > 5 && harga.text.length > 2
            }

            override fun afterTextChanged(p1: Editable) {}
        }
        nama.addTextChangedListener(watcher)
        kodeprod.addTextChangedListener(watcher)
        harga.addTextChangedListener(watcher)
    }
}