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
import com.mike.hms.model.userModel.CreditCard
import com.mike.hms.model.userModel.UserDao
import com.mike.hms.model.userModel.UserEntity

@Database(
    entities = [
        UserEntity::class,
        HouseEntity::class,
        ReviewEntity::class,
        CreditCard::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HMSDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
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
                    "MyDatabase"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

