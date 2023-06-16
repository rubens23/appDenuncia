package com.example.appdenunciacliente.framework.di

import com.example.appdenunciacliente.framework.data.repositories.FIrebaseStorageRepositoryImpl
import com.example.appdenunciacliente.framework.data.repositories.FirebaseRepository
import com.example.appdenunciacliente.framework.data.repositories.FirebaseRepositoryImpl
import com.example.appdenunciacliente.framework.data.repositories.FirebaseStorageRepository
import com.example.appdenunciacliente.framework.managers.LoginManager
import com.example.appdenunciacliente.framework.managers.LoginManagerImpl
import com.example.appdenunciacliente.framework.utils.CalendarTimeDateUtilitary
import com.example.appdenunciacliente.framework.utils.CalendarTimeDateUtilitaryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class HiltModule {



    @Provides
    fun providesLoginManager(): LoginManager {
        return LoginManagerImpl()
    }

    @Provides
    fun providesCalendarTimeDateUtilitary(): CalendarTimeDateUtilitary{
        return CalendarTimeDateUtilitaryImpl()
    }

    @Provides
    fun providesRealTimeDatabaseRepository(calendarTimeDateUtilitary: CalendarTimeDateUtilitary): FirebaseRepository {
        return FirebaseRepositoryImpl(calendarTimeDateUtilitary)
    }

    @Provides
    fun providesFirebaseStorageRepository(): FirebaseStorageRepository {
        return FIrebaseStorageRepositoryImpl()
    }
}