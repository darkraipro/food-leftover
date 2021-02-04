package com.hungames.cookingsocial.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hungames.cookingsocial.data.di.DatabaseModule.ApplicationScope
import com.hungames.cookingsocial.data.model.RegisteredUser
import com.hungames.cookingsocial.data.model.RegisteredUserDao
import com.hungames.cookingsocial.util.TAG_LOGIN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider


@Database(entities = [RegisteredUser::class], version = 1, exportSchema = false)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): RegisteredUserDao


    /* To prevent Circular Dependency, since dagger needs a database and dao to use the insert operations and
     since in DatabaseModule dagger builds a database and needs to know how to build a callback and thus it
     can solve the problem by using Provider which provides the database lazily when the code calls the database.
     onCreate will be called, AFTER
     the database has been created.*/
    class Callback @Inject constructor(private val database: Provider<UserDatabase>, @ApplicationScope private val applicationScope: CoroutineScope): RoomDatabase.Callback(){

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().userDao()

                val password = 123456.hashCode().toString()
                applicationScope.launch {
                    Timber.tag(TAG_LOGIN).i("DB Callback to insert test user")
                    dao.insertUser(RegisteredUser(password = password, email = "viet.hungdinh@yahoo.de"))
                    Timber.tag(TAG_LOGIN).i("DB Callback insert finished. Try to get the user")
                    val res = dao.getUser("viet.hungdinh@yahoo.de", password)
                    Timber.tag(TAG_LOGIN).i("Getting user successful: $res")
                }
        }
    }
}