package com.rasya.manajemenasset1

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.text.DisplayContext
import android.content.ContentValues
import androidx.core.content.contentValuesOf
import java.text.DecimalFormat


class Database(context: Context) :SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "assetManagement.db"
        const val DATABASE_VERSION = 1
    }


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE AssetName(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "assetName TEXT ," +
                    "assetCount INTEGER ," +
                    "assetPrice INTEGER" +
                    ")"
        )
    }

    /**
     * Dipanggil ketika database perlu di-upgrade. Metode ini akan menghapus tabel yang ada dan membuatnya kembali.
     *
     * @param db database.
     * @param oldVersion versi lama dari database.
     * @param newVersion versi baru dari database.
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Menjalankan pernyataan SQL untuk menghapus tabel yang ada jika tabel tersebut ada.
        db?.execSQL("DROP TABLE IF EXISTS AssetName")

        // Memanggil onCreate untuk membuat kembali skema database.
        onCreate(db)
    }

    /**
     * Menambahkan aset baru ke dalam database.
     *
     * @param assetName nama aset.
     * @param assetCount jumlah aset.
     * @param assetPrice harga aset.
     * @return ID baris dari baris yang baru dimasukkan, atau -1 jika terjadi kesalahan.
     */
    fun addAsset(assetName: String, assetCount: Int, assetPrice: Double): Long {

        //membuat database bisa diupdate atau bisa ditambahkan diubah atau dihapus
        val db = writableDatabase

        val contentValues = ContentValues().apply {
            put("assetName", assetName)  // Menambahkan nama aset ke dalam ContentValues
            put("assetCount", assetCount)  // Menambahkan jumlah aset ke dalam ContentValues
            put("assetPrice", assetPrice)  // Menambahkan harga aset ke dalam ContentValues
        }

        return db.insert("AssetName", null, contentValues)  // Memasukan data ke dalam tabel sql
    }


    /**
     * Mengambil semua aset dari database.
     *
     * @return daftar semua nama aset dalam database.
     */
    fun getAllAssets(): List<String> {
        val assetList = ArrayList<String>()  // Membuat daftar untuk menyimpan nama aset
        val selectData = "SELECT * FROM AssetName"  // Query untuk memilih semua data dari tabel AssetName
        val db = readableDatabase  // Mendapatkan instance database dalam mode baca
        val cursor = db.rawQuery(selectData, null)  // Menjalankan query dan mendapatkan cursor

        // Memeriksa apakah cursor memiliki baris pertama
        if (cursor.moveToFirst()) {
            do {
                // Mengambil nama aset dari kolom "assetName"
                val assetName = cursor.getString(cursor.getColumnIndexOrThrow("assetName"))
                val assetCount = cursor.getString(cursor.getColumnIndexOrThrow("assetCount"))
                val assetPrice = cursor.getString(cursor.getColumnIndexOrThrow("assetPrice"))

                assetList.add(assetName)
                assetList.add(assetCount)
                assetList.add(assetPrice)

            } while (cursor.moveToNext())  // Pindah ke baris berikutnya
        }

        cursor.close()  // Menutup cursor
        return assetList  // Mengembalikan daftar nama aset
    }


}