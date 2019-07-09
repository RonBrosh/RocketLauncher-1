package ronybrosh.rocketlauncher.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ronybrosh.rocketlauncher.data.db.dao.LaunchDao
import ronybrosh.rocketlauncher.data.db.dao.RocketDao
import ronybrosh.rocketlauncher.data.db.model.LaunchRow
import ronybrosh.rocketlauncher.data.db.model.RocketRow
import ronybrosh.rocketlauncher.data.db.utils.RocketConverters

@Database(entities = [RocketRow::class, LaunchRow::class], version = 3, exportSchema = false)
@TypeConverters(RocketConverters::class)
abstract class RocketLauncherDatabase : RoomDatabase() {
    abstract fun getRocketDao(): RocketDao
    abstract fun getLaunchDao(): LaunchDao

    companion object {
        private var instance: RocketLauncherDatabase? = null

        @Synchronized
        fun getInstance(context: Context): RocketLauncherDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    RocketLauncherDatabase::class.java, "rocketLauncher.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance as RocketLauncherDatabase
        }
    }
}