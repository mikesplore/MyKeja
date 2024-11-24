package com.mike.hms.model.roomDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mike.hms.model.paymentMethods.CreditCardDao
import com.mike.hms.model.paymentMethods.CreditCardEntity
import com.mike.hms.model.favorites.FavoriteDao
import com.mike.hms.model.favorites.FavouriteEntity
import com.mike.hms.model.houseModel.HouseDao
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.model.paymentMethods.MpesaDao
import com.mike.hms.model.paymentMethods.MpesaEntity
import com.mike.hms.model.paymentMethods.PayPalDao
import com.mike.hms.model.paymentMethods.PayPalEntity
import com.mike.hms.model.review.ReviewDao
import com.mike.hms.model.review.ReviewEntity
import com.mike.hms.model.userModel.UserDao
import com.mike.hms.model.userModel.UserEntity

@Database(
    entities = [
        UserEntity::class,
        HouseEntity::class,
        ReviewEntity::class,
        CreditCardEntity::class,
        FavouriteEntity:: class,
        PayPalEntity::class,
        MpesaEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HMSDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun houseDao(): HouseDao
    abstract fun reviewDao(): ReviewDao
    abstract fun creditCardDao(): CreditCardDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun payPalDao(): PayPalDao
    abstract fun mpesaDao(): MpesaDao

}


