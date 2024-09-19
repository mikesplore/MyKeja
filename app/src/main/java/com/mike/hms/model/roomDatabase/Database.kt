package com.mike.hms.model.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mike.hms.model.bedModel.BedDao
import com.mike.hms.model.bedModel.BedEntity
import com.mike.hms.model.houseModel.HouseDao
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.model.roomModel.RoomDao
import com.mike.hms.model.roomModel.RoomEntity
import com.mike.hms.model.tenantModel.TenantDao
import com.mike.hms.model.tenantModel.TenantEntity

@Database
    (
    entities = [
        TenantEntity::class,
        HouseEntity::class,
        RoomEntity::class,
        BedEntity::class
    ], version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HMSDatabase : RoomDatabase() {
    abstract fun tenantDao(): TenantDao
    abstract fun houseDao(): HouseDao
    abstract fun roomDao(): RoomDao
    abstract fun bedDao(): BedDao

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
                .build()
            INSTANCE = instance
            instance
        }
    }
    }
}
