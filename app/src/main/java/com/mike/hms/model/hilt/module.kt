package com.mike.hms.model.hilt

import android.content.Context
import androidx.room.Room
import com.mike.hms.model.houseModel.HouseRepository
import com.mike.hms.model.review.ReviewRepository
import com.mike.hms.model.roomDatabase.HMSDatabase
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
            "hms_database" // Name of your database
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
}
