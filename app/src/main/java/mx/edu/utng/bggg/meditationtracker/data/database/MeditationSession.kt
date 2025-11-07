package mx.edu.utng.bggg.meditationtracker.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa una sesión de meditación
 */
@Entity(tableName = "meditation_sessions")
data class MeditationSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: SessionType,
    val durationMinutes: Int,
    val date: Long = System.currentTimeMillis(), // Timestamp
    val mood: Int, // 1 (muy mal) a 5 (excelente)
    val notes: String = ""
)

/**
 * Tipos de sesiones disponibles
 */
enum class SessionType {
    MEDITATION,      // Meditación guiada
    BREATHING,       // Ejercicios de respiración
    YOGA,            // Práctica de yoga
    JOURNALING,      // Escritura reflexiva
    GRATITUDE        // Práctica de gratitud
}
