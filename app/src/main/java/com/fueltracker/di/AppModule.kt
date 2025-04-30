package com.fueltracker.di

import android.content.Context
import android.content.SharedPreferences
import com.fueltracker.domain.GetCarDataUseCase
import com.fueltracker.domain.GetUserDataUseCase
import com.fueltracker.domain.SetCarDataUseCase
import com.fueltracker.domain.repository.FuelTrackerRepository
import com.google.firebase.firestore.FirebaseFirestore
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
    fun provideGetCarDataUseCase(fuelTrackerRepository: FuelTrackerRepository): GetCarDataUseCase {
        return GetCarDataUseCase(fuelTrackerRepository)
    }

    @Provides
    @Singleton
    fun provideUserDataUseCase(fuelTrackerRepository: FuelTrackerRepository): GetUserDataUseCase {
        return GetUserDataUseCase(fuelTrackerRepository)
    }

    @Provides
    @Singleton
    fun provideSetCarDataUseCase(fuelTrackerRepository: FuelTrackerRepository): SetCarDataUseCase {
        return SetCarDataUseCase(fuelTrackerRepository)
    }

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("fuel_prefs", Context.MODE_PRIVATE)
    }

//    @Provides
//    fun provideFuelTrackerRepository(
//        firestore: FirebaseFirestore,
//        sharedPrefs: SharedPreferences
//    ): FuelTrackerRepository {
//        return FuelTrackerRepositoryImpl(firestore, sharedPrefs)
//    }
}