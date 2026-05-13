package com.smartlens.tcclosparcerosapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.smartlens.tcclosparcerosapp.data.dao.ProductDao
import com.smartlens.tcclosparcerosapp.data.dao.StockMovementDao
import com.smartlens.tcclosparcerosapp.data.dao.SupplierDao
import com.smartlens.tcclosparcerosapp.data.model.Product
import com.smartlens.tcclosparcerosapp.data.model.StockMovement
import com.smartlens.tcclosparcerosapp.data.model.Supplier

@Database(entities = [Product::class, StockMovement::class, Supplier::class], version = 3, exportSchema = false)
abstract class SmartStockDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun stockMovementDao(): StockMovementDao
    abstract fun supplierDao(): SupplierDao

    companion object {
        @Volatile
        private var INSTANCE: SmartStockDatabase? = null

        fun getDatabase(context: Context): SmartStockDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmartStockDatabase::class.java,
                    "smart_stock_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
