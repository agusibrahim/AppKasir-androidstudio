package com.agusibrahim.appkasir.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.agusibrahim.appkasir.Adapter.ProdukDataAdapter
import com.agusibrahim.appkasir.MainActivity
import com.agusibrahim.appkasir.Model.Produk
import com.agusibrahim.appkasir.R
import com.agusibrahim.appkasir.TableGue
import com.agusibrahim.appkasir.Widget.ProdukDialog
import com.agusibrahim.appkasir.Widget.InputProdukScanner
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.kennyc.bottomsheet.BottomSheet
import com.kennyc.bottomsheet.BottomSheetListener
import de.codecrafters.tableview.TableDataAdapter
import de.codecrafters.tableview.listeners.*

class ProductFragment : Fragment() {
    var fab_addbtn: FloatingActionButton? = null
    var mycoor: CoordinatorLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.myproduct, container, false)
        fab_addbtn = v.findViewById<View>(R.id.fab_addproduct) as FloatingActionButton
        val tableView: TableGue = v.findViewById<View>(R.id.tableView) as TableGue
        tableView.dataAdapter = MainActivity.dataproduk as TableDataAdapter<Produk?>
        tableView.addDataClickListener(DataClickListener(requireActivity(), mycoor))
        tableView.addDataLongClickListener(DataLongClickListener())
        fab_addbtn!!.setOnClickListener {
            //new ProdukDialog(getActivity(), null);
            //new InputProdukScanner(getActivity()).tambahkanProduk();
            BottomSheet.Builder(activity)
                .setSheet(R.menu.inputmodemenu)
                .setListener(object : BottomSheetListener {
                    override fun onSheetShown(p1: BottomSheet) {

                    }

                    override fun onSheetItemSelected(p1: BottomSheet, p2: MenuItem) {
                        when (p2.itemId) {
                            R.id.tambahsatuan -> ProdukDialog(activity, null)
                            R.id.tambahbanyak -> InputProdukScanner(activity!!).tambahkanProduk()
                        }
                    }

                    override fun onSheetDismissed(p1: BottomSheet, p2: Int) {

                    }
                }).show()
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mycoor = view.findViewById<View>(R.id.myproductCoordinatorLayout1) as CoordinatorLayout
    }

    private class DataClickListener(var activity: FragmentActivity, var mycoor: CoordinatorLayout?) : TableDataClickListener<Produk?> {
        override fun onDataClicked(rowIndex: Int, clickedData: Produk?) {
            BottomSheet.Builder(activity)
                .setSheet(R.menu.popupmenu)
                .setTitle(
                    clickedData?.nama + " - (Rp. " + ProdukDataAdapter.PRICE_FORMATTER.format(
                        clickedData?.nama
                    ) + ")"
                )
                .setListener(object : BottomSheetListener {
                    override fun onSheetShown(p1: BottomSheet) {

                    }

                    override fun onSheetItemSelected(p1: BottomSheet, menu: MenuItem) {
                        when (menu.itemId) {
                            R.id.addtoproduct -> MainActivity.dataBalanjaan?.tambah(
                                clickedData!!,
                                -1
                            )
                            R.id.menuedit -> ProdukDialog(activity, clickedData)
                            R.id.menudelete -> Snackbar.make(
                                mycoor!!,
                                "Tekan Hapus untuk mengkonfirmasi",
                                Snackbar.LENGTH_LONG
                            )
                                .setAction("Hapus") {
                                    MainActivity.dataproduk!!.hapus(clickedData!!)
                                    Toast.makeText(activity, "Terhapus", Toast.LENGTH_SHORT).show()
                                }.show()
                        }
                    }

                    override fun onSheetDismissed(p1: BottomSheet, p2: Int) {

                    }
                })
                .show()
            //Toast.makeText(getActivity(), carString, Toast.LENGTH_SHORT).show();
        }
    }

    private inner class DataLongClickListener : TableDataLongClickListener<Produk?> {
        override fun onDataLongClicked(rowIndex: Int, clickedData: Produk?): Boolean {
            ProdukDialog(activity, clickedData)
            return true
        }
    } /*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mainmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.menu_add:
				new ProdukDialog(MainActivity.this, null);
				break;
		}
		return super.onOptionsItemSelected(item);
	}*/
}