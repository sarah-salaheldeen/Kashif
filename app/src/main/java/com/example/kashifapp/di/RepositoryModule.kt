package com.example.kashifapp.di

import com.example.kashifapp.auth.data.AuthRepositoryImpl
import com.example.kashifapp.auth.domain.repository.AuthRepository
import com.example.kashifapp.place.data.repository.PlaceRepositoryImpl
import com.example.kashifapp.place.domain.repository.PlaceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPlaceRepository(
        impl: PlaceRepositoryImpl
    ): PlaceRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}