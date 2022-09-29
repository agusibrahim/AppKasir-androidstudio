package com.agusibrahim.appkasir

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.agusibrahim.appkasir.Model.Produk
import de.codecrafters.tableview.SortableTableView
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter
import de.codecrafters.tableview.toolkit.SortStateViewProviders
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders
import java.util.Comparator

class TableGue : SortableTableView<Produk?> {
    constructor(ctx: Context?) : super(ctx, null) {}

    @JvmOverloads
    constructor(
        context: Context?,
        attributes: AttributeSet?,
        styleAttributes: Int = android.R.attr.listViewStyle
    ) : super(context, attributes, styleAttributes) {
        columnCount = 3
        val simpleTableHeaderAdapter = SimpleTableHeaderAdapter(context, "Produk", "Harga", "Stok")
        simpleTableHeaderAdapter.setTextColor(
            ContextCompat.getColor(
                context!!,
                R.color.table_header_text
            )
        )
        val rowColorEven: Int = ContextCompat.getColor(context, R.color.table_data_row_even)
        val rowColorOdd: Int = ContextCompat.getColor(context, R.color.table_data_row_odd)
        setDataRowBackgroundProvider(
            TableDataRowBackgroundProviders.alternatingRowColors(
                rowColorEven,
                rowColorOdd
            )
        )
        headerSortStateViewProvider = SortStateViewProviders.brightArrows()
        headerAdapter = simpleTableHeaderAdapter
        setColumnComparator(1, HargaProdukComparator() as Comparator<Produk?>)
        setColumnComparator(0, NamaProdukComparator() as Comparator<Produk?>)
        setColumnComparator(2, StokProdukComparator() as Comparator<Produk?>)
    }

    private class HargaProdukComparator : Comparator<Produk> {
        override fun compare(prod1: Produk, prod2: Produk): Int {
            if (prod1.harga < prod2.harga) return -1
            return if (prod1.harga > prod2.harga) 1 else 0
        }
    }

    private class StokProdukComparator : Comparator<Produk> {
        override fun compare(prod1: Produk, prod2: Produk): Int {
            if (prod1.stok < prod2.stok) return -1
            return if (prod1.stok > prod2.stok) 1 else 0
        }
    }

    private class NamaProdukComparator : Comparator<Produk> {
        override fun compare(prod1: Produk, prod2: Produk): Int {
            return prod1.nama.compareTo(prod2.nama)
        }
    }
}