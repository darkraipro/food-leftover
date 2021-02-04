package com.hungames.cookingsocial.data.di

import android.content.Context
import androidx.room.Room
import com.hungames.cookingsocial.data.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class DataSourceLogin

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ApplicationScope

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class FragmentScope

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context, callback: UserDatabase.Callback): UserDatabase{
        return Room.databaseBuilder(context.applicationContext, UserDatabase::class.java, "userDB").fallbackToDestructiveMigration()
                .addCallback(callback).build()
    }

    @Singleton
    @Provides
    fun provideRegisteredUserDao(db: UserDatabase) = db.userDao()


    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

}