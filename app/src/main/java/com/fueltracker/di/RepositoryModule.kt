package com.fueltracker.di

import com.fueltracker.domain.repository.FuelTrackerRepository
import com.fueltracker.data.repository.FuelTrackerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindFuelTrackerRepository(
        fuelTrackerRepositoryImpl: FuelTrackerRepositoryImpl
    ): FuelTrackerRepository
}