package ronybrosh.rocketlauncher.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import ronybrosh.rocketlauncher.data.db.model.RocketRow

@Dao
interface RocketDao {
    @Query("SELECT * from rocketTable")
    fun getRocketList(): Flowable<List<RocketRow>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRocketList(list: List<RocketRow>)

    @Query("DELETE from rocketTable")
    fun deleteRocketTable()
}