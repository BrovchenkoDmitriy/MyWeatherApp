package com.example.myweatherapp.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(
    entities = [CurrentWeatherDbModel::class,
        DailyWeatherDbModel::class,
        HourlyWeatherDbModel::class],
    version = 4,
    exportSchema = true
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
                // Первый зашедший поток или вернёт INSTANCE если он не null.
                INSTANCE?.let {
                    return it
                }
                // Или создаст экземпляр базы данных
                val db = Room.databaseBuilder(
                    application,
                    AppDataBase::class.java,
                    DB_NAME
                )//.allowMainThreadQueries()  //Отключает проверку запроса основного потока для Room.
                    .addMigrations(MIGRATION_3_4)
                    .build()
                //После этой строчки последующие getInstance будет возвращать созданный экземпляр базы данных
                INSTANCE = db
                return db
            }
        }
    }
}

val MIGRATION_3_4: Migration = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE current_weather ADD COLUMN lat DOUBLE DEFAULT 0.0 NOT NULL")
        database.execSQL("ALTER TABLE current_weather ADD COLUMN lon DOUBLE DEFAULT 0.0 NOT NULL")
    }
}