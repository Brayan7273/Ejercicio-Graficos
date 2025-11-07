package mx.edu.utng.bggg.meditationtracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

/**
 * Base de datos principal de la aplicación
 */
@Database(
    entities = [MeditationSession::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class MeditationDatabase : RoomDatabase() {

    abstract fun meditationDao(): MeditationDao

    companion object {
        @Volatile
        private var INSTANCE: MeditationDatabase? = null

        fun getDatabase(context: Context): MeditationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MeditationDatabase::class.java,
                    "meditation_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

/**
 * Converters: Para tipos de datos que Room no entiende nativamente
 */
class Converters {

    @TypeConverter
    fun fromSessionType(type: SessionType): String = type.name

    @TypeConverter
    fun toSessionType(value: String): SessionType = SessionType.valueOf(value)
}
