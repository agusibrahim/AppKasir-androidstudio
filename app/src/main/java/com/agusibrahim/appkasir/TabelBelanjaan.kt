package com.agusibrahim.appkasir

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.agusibrahim.appkasir.Model.Belanjaan
import de.codecrafters.tableview.*
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter
import de.codecrafters.tableview.toolkit.SortStateViewProviders
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders
import java.util.Comparator

class TabelBelanjaan : SortableTableView<Belanjaan?> {
    constructor(ctx: Context?) : super(ctx, null)

    @JvmOverloads
    constructor(
        context: Context?,
        attributes: AttributeSet?,
        styleAttributes: Int = android.R.attr.listViewStyle
    ) : super(context, attributes, styleAttributes) {
        columnCount = 3
        val simpleTableHeaderAdapter =
            SimpleTableHeaderAdapter(context, "Produk", "Harga", "Quantity")
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
        setColumnComparator(0, NamaProdukComparator() as Comparator<Belanjaan?>)
        setColumnComparator(1, HargaProdukComparator() as Comparator<Belanjaan?>)
        setColumnComparator(2, QnProdukComparator() as Comparator<Belanjaan?>)
    }

    private class HargaProdukComparator : Comparator<Belanjaan> {
        override fun compare(prod1: Belanjaan, prod2: Belanjaan): Int {
            if (prod1.produk.harga < prod2.produk.harga) return -1
            return if (prod1.produk.harga > prod2.produk.harga) 1 else 0
        }
    }

    private class QnProdukComparator : Comparator<Belanjaan> {
        override fun compare(prod1: Belanjaan, prod2: Belanjaan): Int {
            if (prod1.quantity < prod2.quantity) return -1
            return if (prod1.quantity > prod2.quantity) 1 else 0
        }
    }

    private class NamaProdukComparator : Comparator<Belanjaan> {
        override fun compare(prod1: Belanjaan, prod2: Belanjaan): Int {
            return prod1.produk.nama.compareTo(prod2.produk.nama)
        }
    }
}