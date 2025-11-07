package mx.edu.utng.bggg.meditationtracker.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO: Define TODAS las operaciones con la base de datos
 */
@Dao
interface MeditationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: MeditationSession): Long

    @Query("SELECT * FROM meditation_sessions ORDER BY date DESC")
    fun getAllSessions(): Flow<List<MeditationSession>>

    @Query("""
        SELECT * FROM meditation_sessions
        WHERE date >= :startDate
        ORDER BY date DESC
    """)
    fun getSessionsSince(startDate: Long): Flow<List<MeditationSession>>

    @Query("SELECT * FROM meditation_sessions WHERE type = :type ORDER BY date DESC")
    fun getSessionsByType(type: SessionType): Flow<List<MeditationSession>>

    @Query("""
        SELECT type, SUM(durationMinutes) as total
        FROM meditation_sessions
        WHERE date >= :startDate
        GROUP BY type
    """)
    fun getTotalMinutesByType(startDate: Long): Flow<List<TypeDuration>>

    @Query("""
        SELECT date, SUM(durationMinutes) as totalMinutes
        FROM meditation_sessions
        WHERE date >= :startDate
        GROUP BY DATE(date / 1000, 'unixepoch')
        ORDER BY date ASC
    """)
    fun getDailyMinutes(startDate: Long): Flow<List<DailyMinutes>>

    @Delete
    suspend fun deleteSession(session: MeditationSession)

    @Update
    suspend fun updateSession(session: MeditationSession)
}

/**
 * Clases auxiliares para queries específicas
 */
data class TypeDuration(
    val type: SessionType,
    val total: Int
)

data class DailyMinutes(
    val date: Long,
    val totalMinutes: Int
)
