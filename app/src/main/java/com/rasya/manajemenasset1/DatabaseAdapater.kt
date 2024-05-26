package com.rasya.manajemenasset1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

private val decimalFormat = DecimalFormat("#,###.##")

class DatabaseAdapater(
    private val assetList: List<Asset>,
    private val deleteAsset: (Asset) -> Unit
) : RecyclerView.Adapter<DatabaseAdapater.AssetViewHolder>() {

    data class Asset(
        val name: String,
        val count: Int,
        val price: Double
    )

    inner class AssetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val assetNameTextView: TextView = itemView.findViewById(R.id.tvAssetName)
        val assetCountTextView: TextView = itemView.findViewById(R.id.tvAssetCount)
        val assetPriceTextView: TextView = itemView.findViewById(R.id.tvAssetPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return AssetViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {
        val asset = assetList[position]
        holder.assetNameTextView.text = asset.name
        holder.assetCountTextView.text = asset.count.toString()
        holder.assetPriceTextView.text = decimalFormat.format(asset.price)

        holder.itemView.setOnLongClickListener {
            deleteAsset(asset)
            true
        }
    }

    override fun getItemCount(): Int {
        return assetList.size
    }
}
