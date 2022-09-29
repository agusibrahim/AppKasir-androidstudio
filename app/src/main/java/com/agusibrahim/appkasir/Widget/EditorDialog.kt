package com.agusibrahim.appkasir.Widget

import android.widget.TextView
import com.agusibrahim.appkasir.Model.Belanjaan
import android.view.LayoutInflater
import com.agusibrahim.appkasir.R
import com.agusibrahim.appkasir.MainActivity
import com.agusibrahim.appkasir.Adapter.BelanjaanDataAdapter
import android.content.Context
import androidx.appcompat.app.AlertDialog

class EditorDialog(ctx: Context?, bel: Belanjaan, totalbelanja: TextView) {
    init {
        val nama = bel.produk.nama
        val harga = bel.produk.harga
        val `val` = bel.quantity
        val v = LayoutInflater.from(ctx).inflate(R.layout.editproduk, null)
        val num = v.findViewById<NumPik>(R.id.numr)
        val hargaview = v.findViewById<TextView>(R.id.hargaview)
        val totalview = v.findViewById<TextView>(R.id.totalview)
        val dlg = AlertDialog.Builder(
            ctx!!
        )
        dlg.setView(v)
        dlg.setTitle(nama)
        dlg.setPositiveButton("Set") { p1, p2 ->
            MainActivity.Companion.dataBalanjaan?.tambah(bel.produk, num.value)
            totalbelanja.text =
                "Rp. " + BelanjaanDataAdapter.PRICE_FORMATTER.format(BelanjaanDataAdapter.total)
        }
        dlg.setNeutralButton("Hapus Belanjaan", null)
        dlg.show()
        totalview.text = "Rp. " + BelanjaanDataAdapter.PRICE_FORMATTER.format(harga * `val`)
        hargaview.text = "Rp. " + BelanjaanDataAdapter.PRICE_FORMATTER.format(harga)
        num.minValue = 1
        num.maxValue = 200
        num.value = `val`
        num.setOnValueChangedListener { p1, p2, p3 ->
            totalview.text = "Rp. " + BelanjaanDataAdapter.PRICE_FORMATTER.format(harga * num.value)
        }
    }
}