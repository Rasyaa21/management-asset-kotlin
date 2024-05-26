package com.rasya.manajemenasset1

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rasya.manajemenasset1.databinding.ActivityMainBinding
import com.rasya.manajemenasset1.databinding.PopupInputBinding
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: Database
    private lateinit var assetAdapter: DatabaseAdapater
    private var assetList = mutableListOf<DatabaseAdapater.Asset>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Database(this)
        assetList = db.getAllAssets().mapNotNull { assetString ->
            val parts = assetString.split(",")
            if (parts.size == 3) {
                try {
                    DatabaseAdapater.Asset(parts[0], parts[1].toInt(), parts[2].toDouble())
                } catch (e: NumberFormatException) {
                    null
                }
            } else {
                null
            }
        }.toMutableList()

        assetAdapter = DatabaseAdapater(assetList) { asset ->
            assetList.remove(asset)
            assetAdapter.notifyDataSetChanged()
        }

        binding.rvRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.rvRecyclerView.adapter = assetAdapter

        binding.floatingButton.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        val dialogBinding = PopupInputBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        dialogBinding.btAddAsset.setOnClickListener {
            val assetName = dialogBinding.etAssetName.text.toString()
            val assetCountString = dialogBinding.etAssetCount.text.toString()
            val assetPriceString = dialogBinding.etAssetPrice.text.toString()

            if (assetName.isNotEmpty() && assetCountString.isNotEmpty() && assetPriceString.isNotEmpty()) {
                try {
                    val assetCount = assetCountString.toInt()
                    val assetPrice = assetPriceString.toDouble()
                    val newAsset = DatabaseAdapater.Asset(assetName, assetCount, assetPrice)
                    assetList.add(newAsset)
                    db.addAsset(assetName, assetCount, assetPrice)
                    assetAdapter.notifyDataSetChanged()
                    dialog.dismiss()

                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "masukan total asset / jumlah asset", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "isi semua data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
