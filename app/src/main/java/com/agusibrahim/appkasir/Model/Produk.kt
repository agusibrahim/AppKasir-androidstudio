package com.agusibrahim.appkasir.Model

import android.annotation.SuppressLint
import com.agusibrahim.appkasir.DBHelper
import android.content.Context
import com.agusibrahim.appkasir.Const
import java.util.ArrayList

class Produk(var nama: String, var sn: String, var harga: Long, var stok: Int) {

    companion object {

        @SuppressLint("Range")
        fun getBySN(ctx: Context?, SN: String): Produk? {
            val cur = DBHelper(ctx).baca(SN)
            if (!cur.moveToFirst()) return null
            if (cur.count < 1) {
                return null
            }
            val nama = cur.getString(cur.getColumnIndex(Const.FIELD_NAMA))
            val sn = cur.getString(cur.getColumnIndex(Const.FIELD_SN))
            val harga = cur.getLong(cur.getColumnIndex(Const.FIELD_HARGA))
            val stok = cur.getInt(cur.getColumnIndex(Const.FIELD_STOK))
            return Produk(nama, sn, harga, stok)
        }

        @SuppressLint("Range")
        fun getInit(ctx: Context?): ArrayList<Produk> {
            val prod = ArrayList<Produk>()
            val cur = DBHelper(ctx).semuaData()
            cur.moveToFirst()
            for (i in 0 until cur.count) {
                cur.moveToPosition(i)
                val nama = cur.getString(cur.getColumnIndex(Const.FIELD_NAMA))
                val sn = cur.getString(cur.getColumnIndex(Const.FIELD_SN))
                val harga = cur.getLong(cur.getColumnIndex(Const.FIELD_HARGA))
                val stok = cur.getInt(cur.getColumnIndex(Const.FIELD_STOK))
                prod.add(Produk(nama, sn, harga, stok))
            }
            return prod
        }
    }
}