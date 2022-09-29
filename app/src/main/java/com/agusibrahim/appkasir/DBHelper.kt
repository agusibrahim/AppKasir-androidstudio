package com.agusibrahim.appkasir

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase

class DBHelper(ctx: Context?) : SQLiteOpenHelper(ctx, "$namaTable.db", null, 2) {
    private var dbw: SQLiteDatabase = writableDatabase

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table $namaTable (sn TEXT null,nama TEXT null,harga INTEGER null, stok INTEGER null)")
    }

    override fun onUpgrade(db: SQLiteDatabase, p2: Int, p3: Int) {
        db.execSQL("DROP TABLE IF EXISTS $namaTable")
    }

    fun tambah(`val`: ContentValues?) {
        dbw.insert(namaTable, null, `val`)
    }

    fun update(`val`: ContentValues?, sn: String) {
        dbw.update(namaTable, `val`, "sn=$sn", null)
    }

    fun delete(sn: String) {
        dbw.delete(namaTable, "sn=$sn", null)
    }

    fun semuaData(): Cursor {
        return dbw.rawQuery("SELECT * FROM $namaTable", null)
    }

    fun baca(sn: String): Cursor {
        return dbw.rawQuery("SELECT * FROM $namaTable WHERE sn=$sn", null)
    }

    companion object {
        var namaTable = "produk"
    }
}