package com.example.testretrofitapp.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [CurrentWeatherDbModel::class,
                DailyWeatherDbModel::class,
                HourlyWeatherDbModel::class],
    version = 3
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun dailyWeatherDao(): DailyWeatherDao
    abstract fun hourlyWeatherDao():HourlyWeatherDao

    companion object {

        private const val DB_NAME = "weather_db"
        private var INSTANCE: AppDataBase? = null
        private val LOCK = Any()

        //База данных у нас синглтон.
        fun getInstance(application: Application): AppDataBase {
            //Проверяем если INSTANCE не null то возвращаем
            INSTANCE?.let {
                return it
            }
            // Если у нас несколько потоков то нужен блок синхронизации для случая многопоточности.
            synchronized(LOCK) {
                // Первый зашедший поток или вернет INSTANCE если он не null.
                INSTANCE?.let {
                    return it
                }
                // Или создаст экземпляр баззы данных
                val db = Room.databaseBuilder(
                    application,
                    AppDataBase::class.java,
                    DB_NAME
                ).allowMainThreadQueries()
                    .build()
                //После этой строчки последующие getInstance будет возвращать созданный экземпляр баззы данных
                INSTANCE = db
                return db
            }
        }
    }
}