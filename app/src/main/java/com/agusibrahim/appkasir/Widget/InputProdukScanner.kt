package com.agusibrahim.appkasir.Widget

import com.agusibrahim.appkasir.Model.Produk
import android.view.LayoutInflater
import com.agusibrahim.appkasir.R
import com.agusibrahim.appkasir.MainActivity
import com.agusibrahim.appkasir.Adapter.BelanjaanDataAdapter
import android.content.ContentValues
import android.content.Context
import android.text.TextWatcher
import android.text.Editable
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.DecoratedBarcodeView.TorchListener
import com.agusibrahim.appkasir.Fragment.BelanjaFragment
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.google.zxing.ResultPoint
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import java.util.ArrayList

class InputProdukScanner(var ctx: Context) {
    var barcodeView: DecoratedBarcodeView? = null
    var imm: InputMethodManager = ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    private var produk_terindentifikasi: Produk? = null
    var dlg: AlertDialog.Builder = AlertDialog.Builder(ctx)
    var lampufles = false

    fun setupScanner() {
        barcodeView!!.setStatusText("Arahkan ke barcode")
        val formatList = ArrayList<BarcodeFormat>()
        formatList.add(BarcodeFormat.EAN_13)
        //barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formatList, null, null));
        //new DecoderFactory();
        // Toggle flashlight saat viewfinder barcode disentuh
        barcodeView!!.setOnClickListener { if (lampufles) barcodeView!!.setTorchOff() else barcodeView!!.setTorchOn() }
        barcodeView!!.setTorchListener(object : TorchListener {
            override fun onTorchOn() {
                lampufles = true
            }

            override fun onTorchOff() {
                lampufles = false
            }
        })
    }

    fun shoping() {
        val v = LayoutInflater.from(ctx).inflate(R.layout.shopingscanner, null)
        val namaproduk = v.findViewById<View>(R.id.scanNamaProduk) as TextView
        val hargaproduk = v.findViewById<View>(R.id.scanHargaProduk) as TextView
        val snProduk = v.findViewById<View>(R.id.scanSN) as TextView
        val tanpakonf = v.findViewById<View>(R.id.autoaddcheck) as CheckBox
        barcodeView = v.findViewById<View>(R.id.scannershop) as DecoratedBarcodeView
        setupScanner()
        dlg.setView(v)
        dlg.setTitle("Pemindai Barcode")
        dlg.setCancelable(false)
        dlg.setNegativeButton("Selesai") { p1, p2 -> barcodeView!!.pause() }
        // ini hanya buat nampilin tombol "Tambahkan" pada dialog
        // onclick sengaja ga di isi
        // Meng-override onClick (lihat okBtn) agar saat di klik dialog kaga ngilang
        dlg.setPositiveButton("Tambahkan", null)
        val dialog = dlg.show()
        // tombol positive (Tambahkan)
        val okBtn = dialog.getButton(AlertDialog.BUTTON1)
        okBtn.isEnabled = false
        tanpakonf.setOnCheckedChangeListener { p1, checked ->
            okBtn.isEnabled = !checked
        }
        // Override onClick positiveButton pada dialog, 
        okBtn.setOnClickListener {
            MainActivity.Companion.dataBalanjaan?.tambah(produk_terindentifikasi!!, -1)
            // Update totalJumalah di BottomSheet
            BelanjaFragment.totaljum?.text = "Rp. " + BelanjaanDataAdapter.PRICE_FORMATTER.format(
                BelanjaanDataAdapter.total
            )
        }
        barcodeView!!.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                produk_terindentifikasi = Produk.Companion.getBySN(ctx, result.text)
                // Jika produk kedaftar di Database produk
                // Kalo ngga ya diem
                if (produk_terindentifikasi != null) {
                    namaproduk.text = produk_terindentifikasi?.nama
                    hargaproduk.text =
                        "Rp. " + BelanjaanDataAdapter.PRICE_FORMATTER.format(produk_terindentifikasi?.harga)
                    snProduk.text = result.text
                    namaproduk.visibility = View.VISIBLE
                    hargaproduk.visibility = View.VISIBLE
                    // Jika mode otomatis (tanpa konfirm) di cek
                    if (tanpakonf.isChecked) {
                        okBtn.isEnabled = false
                        MainActivity.Companion.dataBalanjaan?.tambah(produk_terindentifikasi!!, -1)
                        // Update totalJumalah di BottomSheet
                        BelanjaFragment.Companion.totaljum?.text = "Rp. " + BelanjaanDataAdapter.PRICE_FORMATTER.format(
                            BelanjaanDataAdapter.total
                        )
                        // Pause dulu kamera jika sudah berhasil mengidentifikasi produk
                        // setelah 2dtk baru di resume
                        // ini utk menghindari scan beruntun, dlm waktu 2dtk jauhkan barcode dari kamera atau aplikasi akan mengupdate status Quantity-nya
                        barcodeView!!.pause()
                        val handler = Handler()
                        handler.postDelayed({ barcodeView!!.resume() }, 2000)
                    } else {
                        okBtn.isEnabled = true
                    }
                }
            }

            override fun possibleResultPoints(p1: List<ResultPoint>) {}
        })
        barcodeView!!.resume()
    }

    fun tambahkanProduk() {
        val v = LayoutInflater.from(ctx).inflate(R.layout.inputproduk_scanner, null)
        val namaproduk = v.findViewById<View>(R.id.new_namaproduk) as EditText
        val hargaproduk = v.findViewById<View>(R.id.new_hargaproduk) as EditText
        val stokproduk = v.findViewById<View>(R.id.new_stok) as EditText
        val tambahkanbtn = v.findViewById<View>(R.id.tambhakanbtn) as Button
        val cancelbtn = v.findViewById<View>(R.id.cancelbtn) as Button
        val selesaibtn = v.findViewById<View>(R.id.selesaibtn) as Button
        namaproduk.isEnabled = false
        hargaproduk.isEnabled = false
        stokproduk.isEnabled = false
        val callback: BarcodeCallback = object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                if (result.text != null) {
                    namaproduk.isEnabled = true
                    hargaproduk.isEnabled = true
                    stokproduk.isEnabled = true
                    namaproduk.requestFocus()
                    produk_terindentifikasi = Produk.Companion.getBySN(ctx, result.text)
                    if (produk_terindentifikasi != null) {
                        tambahkanbtn.text = "Perbarui"
                        namaproduk.setText(produk_terindentifikasi?.nama)
                        hargaproduk.setText("" + produk_terindentifikasi?.harga)
                        stokproduk.setText("" + produk_terindentifikasi?.stok)
                    } else {
                        tambahkanbtn.text = "Tambahkan"
                    }
                    imm.showSoftInput(namaproduk, InputMethodManager.SHOW_IMPLICIT)
                    barcodeView!!.setStatusText(result.text)
                }
            }

            override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
        }
        barcodeView = v.findViewById<View>(R.id.scanner) as DecoratedBarcodeView
        setupScanner()
        dlg.setView(v)
        dlg.setCancelable(false)
        dlg.setTitle("Tambahkan Produk")
        dlg.setOnCancelListener { barcodeView!!.pause() }
        val dialog = dlg.create()
        cancelbtn.setOnClickListener {
            barcodeView!!.pause()
            dialog.dismiss()
        }
        tambahkanbtn.setOnClickListener {
            val data = ContentValues()
            data.put("nama", namaproduk.text.toString())
            data.put("sn", barcodeView!!.statusView.text.toString())
            data.put("harga", hargaproduk.text.toString().toLong())
            data.put("stok", stokproduk.text.toString().toInt())
            if (produk_terindentifikasi == null) MainActivity.Companion.dataproduk?.tambah(data) else MainActivity.dataproduk?.perbarui(
                produk_terindentifikasi!!,
                data
            )
            imm.hideSoftInputFromWindow(namaproduk.windowToken, 0)
            namaproduk.setText("")
            hargaproduk.setText("")
            stokproduk.setText("")
            namaproduk.isEnabled = false
            hargaproduk.isEnabled = false
            stokproduk.isEnabled = false
            barcodeView!!.setStatusText("Arahkan ke barcode")
        }
        dialog.show()
        tambahkanbtn.isEnabled = false
        val watcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p1: CharSequence, p2: Int, p3: Int, p4: Int) {}
            override fun onTextChanged(p1: CharSequence, p2: Int, p3: Int, p4: Int) {
                val s = stokproduk.text.toString()
                var stok = 0
                if (s.length > 0) {
                    stok = s.toInt()
                }
                if (namaproduk.text.length > 3 && stok > 0 && hargaproduk.text.length > 2) tambahkanbtn.isEnabled =
                    true else tambahkanbtn.isEnabled = false
            }

            override fun afterTextChanged(p1: Editable) {}
        }
        namaproduk.addTextChangedListener(watcher)
        hargaproduk.addTextChangedListener(watcher)
        stokproduk.addTextChangedListener(watcher)
        barcodeView!!.decodeContinuous(callback)
        barcodeView!!.resume()
    }
}