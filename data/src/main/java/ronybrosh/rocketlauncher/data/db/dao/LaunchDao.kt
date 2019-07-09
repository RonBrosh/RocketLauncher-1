package ronybrosh.rocketlauncher.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import ronybrosh.rocketlauncher.data.db.model.LaunchRow

@Dao
interface LaunchDao {
    @Query("SELECT * from launchTable WHERE rocketId == :rocketId ORDER BY flightNumber ASC")
    fun getLaunchList(rocketId: String): Flowable<List<LaunchRow>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLaunchList(list: List<LaunchRow>)

    @Query("DELETE from launchTable")
    fun deleteLaunchTable()
}