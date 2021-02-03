package com.hungames.cookingsocial.data.di

import android.content.Context
import androidx.room.Room
import com.hungames.cookingsocial.data.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class DataSourceLogin

    @Singleton
    @Provides
    fun provideDB(@ApplicationContext context: Context): UserDatabase{
        return Room.databaseBuilder(context.applicationContext, UserDatabase::class.java, "userDB").build()
    }


}