package ronybrosh.rocketlauncher.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ronybrosh.rocketlauncher.data.db.utils.RocketConverters

@Entity(tableName = "rocketTable")
data class RocketRow(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "isActive") val isActive: Boolean,
    @TypeConverters(RocketConverters::class) @ColumnInfo(name = "imageUrlList") val imageUrlList: List<String>,
    @ColumnInfo(name = "enginesCount") val enginesCount: Int
)