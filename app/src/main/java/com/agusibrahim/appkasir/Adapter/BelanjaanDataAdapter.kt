package com.agusibrahim.appkasir.Adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.agusibrahim.appkasir.Model.Belanjaan
import com.agusibrahim.appkasir.Model.Produk
import de.codecrafters.tableview.TableDataAdapter
import java.text.NumberFormat

class BelanjaanDataAdapter(ctx: Context?)
    : TableDataAdapter<Belanjaan>(ctx, ArrayList<Belanjaan>()) {
    override fun getCellView(row: Int, column: Int, p3: ViewGroup): View {
        val belanjaan = getRowData(row)
        val produk = belanjaan!!.produk
        var render: View? = null
        when (column) {
            0 -> render = renderString(produk.nama)
            1 -> render = renderString("Rp. " + PRICE_FORMATTER.format(produk.harga))
            2 -> render = renderString("" + belanjaan.quantity)
        }
        return render!!
    }

    // Fungsi get Belanjaan melalui SN
    private fun getBelBySN(sn: String): Belanjaan? {
        var pp: Belanjaan? = null
        for (bb in data) {
            if (bb!!.produk.sn == sn) {
                pp = bb
                break
            }
        }
        return pp
    }

    private fun updateTotal() {
        var jm: Long = 0
        for (bb in data) {
            jm += bb!!.produk.harga * bb.quantity
        }
        total = jm
    }

    fun tambah(prod: Produk, quantity: Int) {
        var qty = quantity
        val bel = getBelBySN(prod.sn)
        // jika produk sudah ada dalam keranjang
        // maka tambahkan quantity
        if (bel != null) {
            var prodquantity = bel.quantity + 1
            if (qty != -1) prodquantity = qty
            data[data.indexOf(bel)] = Belanjaan(prod, prodquantity)
        } else {
            // jika tidak ada dalam keranjang
            // maka masukan ke keranjang
            if (qty == -1) qty = 1
            data.add(Belanjaan(prod, qty))
        }
        // update total belanja
        updateTotal()
        notifyDataSetChanged()
    }

    /*fun hapus(produk: Produk) {
        data.remove(produk)
        // update total belanja
        updateTotal()
        notifyDataSetChanged()
    }*/

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
        @JvmField
		var total: Long = 0
    }
}