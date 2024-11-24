package com.mike.hms.model.hilt

import android.content.Context
import androidx.room.Room
import com.mike.hms.model.paymentMethods.CreditCardRepository
import com.mike.hms.model.favorites.FavoriteRepository
import com.mike.hms.model.houseModel.HouseRepository
import com.mike.hms.model.paymentMethods.MpesaRepository
import com.mike.hms.model.paymentMethods.PayPalRepository
import com.mike.hms.model.review.ReviewRepository
import com.mike.hms.model.roomDatabase.HMSDatabase
import com.mike.hms.model.statements.StatementRepository
import com.mike.hms.model.userModel.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HMSDatabase {
        return Room.databaseBuilder(
            context,
            HMSDatabase::class.java,
            "rms_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserRepository(database: HMSDatabase): UserRepository {
        return UserRepository(database.userDao())
    }

    @Provides
    @Singleton
    fun provideHouseRepository(database: HMSDatabase): HouseRepository {
        return HouseRepository(database.houseDao())
    }

    @Provides
    @Singleton
    fun provideReviewRepository(database: HMSDatabase): ReviewRepository {
        return ReviewRepository(database.reviewDao())
    }

    @Provides
    @Singleton
    fun provideCreditCardRepository(database: HMSDatabase): CreditCardRepository {
        return CreditCardRepository(database.creditCardDao())
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(database: HMSDatabase): FavoriteRepository {
        return FavoriteRepository(database.favoriteDao())
    }

    @Provides
    @Singleton
    fun providePayPalRepository(database: HMSDatabase): PayPalRepository {
        return PayPalRepository(database.payPalDao())
    }

    @Provides
    @Singleton
    fun provideMpesaRepository(database: HMSDatabase): MpesaRepository {
        return MpesaRepository(database.mpesaDao())
    }

    @Provides
    @Singleton
    fun providesStatementRepository(database: HMSDatabase): StatementRepository {
        return StatementRepository(database.statementDao())
    }
}
