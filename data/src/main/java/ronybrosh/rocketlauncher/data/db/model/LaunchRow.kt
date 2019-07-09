package ronybrosh.rocketlauncher.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "launchTable", indices = [Index(
        value = ["rocketId", "flightNumber"],
        unique = true
    )]
)
data class LaunchRow(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "flightNumber") val flightNumber: Int,
    @ColumnInfo(name = "rocketId") val rocketId: String,
    @ColumnInfo(name = "missionName") val missionName: String,
    @ColumnInfo(name = "launchYear") val launchYear: String,
    @ColumnInfo(name = "patchImageUrl") val patchImageUrl: String?,
    @ColumnInfo(name = "launchDateUnix") val launchDateUnix: Long,
    @ColumnInfo(name = "isSuccessful") val isSuccessful: Boolean
)
