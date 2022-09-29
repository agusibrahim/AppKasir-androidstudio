package com.agusibrahim.appkasir.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.agusibrahim.appkasir.Adapter.BelanjaanDataAdapter
import com.agusibrahim.appkasir.MainActivity
import com.agusibrahim.appkasir.Model.Belanjaan
import com.agusibrahim.appkasir.R
import com.agusibrahim.appkasir.Widget.EditorDialog
import com.agusibrahim.appkasir.Widget.InputProdukScanner
import com.agusibrahim.appkasir.TabelBelanjaan
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.codecrafters.tableview.TableDataAdapter
import de.codecrafters.tableview.listeners.TableDataClickListener
import de.codecrafters.tableview.listeners.TableDataLongClickListener

class BelanjaFragment : Fragment() {

    companion object{
        var totaljum: TextView? = null
        var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.belanja, container, false)
        val velanjaan: TabelBelanjaan = v.findViewById(R.id.belanjaan)
        velanjaan.dataAdapter = MainActivity.dataBalanjaan as TableDataAdapter<Belanjaan?>
        velanjaan.addDataClickListener(DtaClickListener(requireActivity()))
        velanjaan.addDataLongClickListener(DataLongClickListener())
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fabshop: FloatingActionButton =
            view.findViewById<View>(R.id.fab_shopping) as FloatingActionButton
        totaljum = view.findViewById<View>(R.id.totaljumlah) as TextView
        bottomSheetBehavior =
            BottomSheetBehavior.from(view.findViewById(R.id.bottomSheet))
        fabshop.setOnClickListener { p1 -> InputProdukScanner(p1.context).shoping() }
        if (BelanjaanDataAdapter.total != 0L) {
            totaljum?.text = "Rp. ${BelanjaanDataAdapter.PRICE_FORMATTER.format(
                BelanjaanDataAdapter.total
            )}"
            (bottomSheetBehavior as BottomSheetBehavior<View>).setState(BottomSheetBehavior.STATE_COLLAPSED)
        } else {
            (bottomSheetBehavior as BottomSheetBehavior<View>).setState(BottomSheetBehavior.STATE_HIDDEN)
        }
    }

    private class DtaClickListener(
        var activity: FragmentActivity
    ) : TableDataClickListener<Belanjaan?> {
        override fun onDataClicked(rowIndex: Int, belanjaan: Belanjaan?) {
            EditorDialog(activity, belanjaan!!, totaljum!!)
        }
    }

    private class DataLongClickListener : TableDataLongClickListener<Belanjaan?> {
        override fun onDataLongClicked(rowIndex: Int, belanjaan: Belanjaan?): Boolean {
            //showdlg(belanjaan.getProduk().getNama(), belanjaan.getProduk().getHarga(), belanjaan.getQuantity());
            return true
        }
    }
}