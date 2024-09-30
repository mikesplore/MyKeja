package com.mike.hms.model.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mike.hms.model.houseModel.HouseDao
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.model.review.ReviewDao
import com.mike.hms.model.review.ReviewEntity
import com.mike.hms.model.tenantModel.TenantDao
import com.mike.hms.model.tenantModel.TenantEntity

@Database(
    entities = [
        TenantEntity::class,
        HouseEntity::class,
        ReviewEntity::class
    ],
    version = 2, // Incremented version number
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HMSDatabase : RoomDatabase() {
    abstract fun tenantDao(): TenantDao
    abstract fun houseDao(): HouseDao
    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile
        private var INSTANCE: HMSDatabase? = null

        fun getDatabase(context: Context): HMSDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HMSDatabase::class.java,
                    "HmsDatabase"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

