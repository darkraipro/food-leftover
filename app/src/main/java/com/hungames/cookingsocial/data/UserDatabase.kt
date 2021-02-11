package com.hungames.cookingsocial.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hungames.cookingsocial.data.di.DatabaseModule.ApplicationScope
import com.hungames.cookingsocial.data.model.Dishes
import com.hungames.cookingsocial.data.model.DishesDao
import com.hungames.cookingsocial.data.model.RegisteredUser
import com.hungames.cookingsocial.data.model.RegisteredUserDao
import com.hungames.cookingsocial.util.NutritionType
import com.hungames.cookingsocial.util.TAG_DISH
import com.hungames.cookingsocial.util.TAG_LOGIN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider


@Database(entities = [RegisteredUser::class, Dishes::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): RegisteredUserDao
    abstract fun dishesDao(): DishesDao


    /* To prevent Circular Dependency, since dagger needs a database and dao to use the insert operations and
     since in DatabaseModule dagger builds a database and needs to know how to build a callback and thus it
     can solve the problem by using Provider which provides the database lazily when the code calls the database.
     onCreate will be called, AFTER
     the database has been created.*/
    class Callback @Inject constructor(
        private val database: Provider<UserDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val userDao = database.get().userDao()
            val dishesDao = database.get().dishesDao()
            val password = "123456"
            val passwordHash = password.hashCode().toString()
            applicationScope.launch {
                Timber.tag(TAG_LOGIN).i("DB Callback! DB has been created. Now to insert test user")

                // Race condition between insertUser and getUser during startup ONLY if user tries to login with an account which does not exist, but for
                // testing reason exists, because DB will be lazily instantiated if a DAO is needed
                // and in current implementation only on LoginDataSource the DAO will be instantiated.
                // Normal flow: First register. It will auto login on success and subsequent logins work as intended

                // Add FakeUsers to test POI on MapsFragment.kt

                val tim = RegisteredUser(
                    password = passwordHash,
                    email = "tim.lemon@geng.org",
                    street = "Schemkesweg 42",
                    postal = "47057",
                    city = "Duisburg",
                    country = "Germany",
                    userDescription = "Professional LoL player also passionate cooker."
                )
                val martin = RegisteredUser(
                    password = passwordHash,
                    email = "martin.larsson@g2.org",
                    street = "Grabenstraße 120",
                    postal = "47057",
                    city = "Duisburg",
                    country = "Germany",
                    userDescription = "One of the best ADC in the west offering culinaries from Sweden."
                )
                val tina = RegisteredUser(
                    password = passwordHash,
                    email = "tina.nemesis@geng.org",
                    street = "Oststraße 48",
                    postal = "47057",
                    city = "Duisburg",
                    country = "Germany",
                    userDescription = "The girlfriend of Tim Lemon who offers dishes inspired by various culture."
                )

                userDao.insertUser(
                    RegisteredUser(
                        password = passwordHash,
                        email = "viet.hungdinh@yahoo.de"
                    )
                )
                userDao.insertUser(tim)
                userDao.insertUser(martin)
                userDao.insertUser(tina)

                // Add fake dishes to test
                val userMinimal = userDao.getUser("tim.lemon@geng.org", passwordHash)
                Timber.tag(TAG_DISH).i("About to insert dishes into Dishes with user: $userMinimal")
                Timber.tag(TAG_DISH).i("UID to use: ${userMinimal?.uid}")
                userMinimal?.let {
                    dishesDao.insertDish(
                        Dishes(
                            uid = userMinimal.uid,
                            dishName = "Spaghetti Carbonara",
                            dishDescription = "Delicous home made original Carbonara",
                            dishPrice = 9.90f,
                            nutritionType = NutritionType.ALL,
                            quantity = 2
                        )
                    )
                    dishesDao.insertDish(
                        Dishes(
                            uid = userMinimal.uid,
                            dishName = "Pizza Margharita",
                            dishDescription = "Classic Italian pizza with cheese and tomato sauce from my oven",
                            dishPrice = 5.50f,
                            nutritionType = NutritionType.VEGETARIAN,
                            quantity = 14
                        )
                    )
                }
                Timber.tag(TAG_DISH).i("Inserting dishes complete. Now trying to get them")
                val getDish = dishesDao.getDishes(userMinimal!!.uid)
                Timber.tag(TAG_DISH).i("Got dishes: $getDish")
                Timber.tag(TAG_LOGIN).i("DB Callback insert finished. Try to get the user")
                val res = userDao.getUser("viet.hungdinh@yahoo.de", passwordHash)
                Timber.tag(TAG_LOGIN).i("DB Callback Getting user successful: $res")
            }
        }
    }
}