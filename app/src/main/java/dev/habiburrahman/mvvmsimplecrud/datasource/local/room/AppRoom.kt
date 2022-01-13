package dev.habiburrahman.mvvmsimplecrud.datasource.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.habiburrahman.mvvmsimplecrud.datasource.local.room.tables.StocksTable

@Database(entities = [StocksTable::class], version = 1, exportSchema = false)
abstract class AppRoom: RoomDatabase() {

    abstract fun appDao(): AppDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppRoom? = null

        fun getDatabase(context: Context): AppRoom {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoom::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}