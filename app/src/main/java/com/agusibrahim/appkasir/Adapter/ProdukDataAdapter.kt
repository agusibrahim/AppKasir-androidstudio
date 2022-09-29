package com.agusibrahim.appkasir.Adapter

import android.content.ContentValues
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.agusibrahim.appkasir.DBHelper
import com.agusibrahim.appkasir.Model.Produk
import de.codecrafters.tableview.TableDataAdapter
import java.text.NumberFormat
import java.util.ArrayList

class ProdukDataAdapter(ctx: Context?, prod: ArrayList<Produk>) :
    TableDataAdapter<Any?>(ctx, prod as List<Any?>?) {
    override fun getCellView(row: Int, column: Int, p3: ViewGroup): View {
        val produk = getRowData(row) as Produk?
        var render: View? = null
        when (column) {
            0 -> render = renderString(produk!!.nama)
            1 -> render = renderString(
                "Rp. " + PRICE_FORMATTER.format(
                    produk!!.harga
                )
            )
            2 -> render = renderString("" + produk!!.stok)
        }
        return render!!
    }

    private fun getpos(p: Produk): Int {
        var pos = -1
        for (pp in data) {
            val ppp = pp as Produk?
            if (ppp!!.sn == p.sn) {
                pos = data.indexOf(pp)
                break
            }
        }
        return pos
    }

    fun tambah(`val`: ContentValues) {
        data.add(
            Produk(
                `val`.getAsString("nama"),
                `val`.getAsString("sn"),
                `val`.getAsLong("harga"),
                `val`.getAsInteger("stok")
            )
        )
        DBHelper(context).tambah(`val`)
        notifyDataSetChanged()
    }

    fun hapus(produk: Produk) {
        data.remove(produk)
        DBHelper(context).delete(produk.sn)
        notifyDataSetChanged()
    }

    fun perbarui(produk: Produk, newdata: ContentValues) {
        val idx = getpos(produk) //getData().indexOf(produk);
        if (idx < 0) {
            return
        }
        data[idx] = Produk(
            newdata.getAsString("nama"),
            newdata.getAsString("sn"),
            newdata.getAsLong("harga"),
            newdata.getAsInteger("stok")
        )
        DBHelper(context).update(newdata, produk.sn)
        notifyDataSetChanged()
    }

    private fun renderString(value: String): View {
        val textView = TextView(context)
        textView.text = value
        textView.setPadding(20, 30, 20, 30)
        textView.textSize = 14f
        return textView
    }

    companion object {
        @JvmField
		val PRICE_FORMATTER: NumberFormat = NumberFormat.getNumberInstance()
    }
}