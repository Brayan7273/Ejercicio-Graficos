package mx.edu.utng.bggg.meditationtracker.data.repository

import mx.edu.utng.bggg.meditationtracker.data.database.*
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

/**
 * Repositorio: Única fuente de verdad para los datos
 *
 * Ventajas de usar un repositorio:
 * 1. Abstracción: La UI no sabe de dónde vienen los datos (BD, API, caché)
 * 2. Testing: Fácil de probar con datos falsos
 * 3. Mantenibilidad: Si cambias la BD, solo cambias aquí
 *
 * Analogía: Como Amazon, tú pides productos sin saber de qué bodega vienen
 */
class MeditationRepository(private val dao: MeditationDao) {

    /** Obtener todas las sesiones **/
    val allSessions: Flow<List<MeditationSession>> = dao.getAllSessions()

    /** Insertar una nueva sesión **/
    suspend fun insertSession(session: MeditationSession): Long {
        return dao.insertSession(session)
    }

    /** Obtener sesiones de la última semana **/
    fun getWeeklySessions(): Flow<List<MeditationSession>> {
        val weekAgo = getDateDaysAgo(7)
        return dao.getSessionsSince(weekAgo)
    }

    /** Obtener sesiones del último mes **/
    fun getMonthlySessions(): Flow<List<MeditationSession>> {
        val monthAgo = getDateDaysAgo(30)
        return dao.getSessionsSince(monthAgo)
    }

    /** Datos para gráfico de pastel **/
    fun getPieChartData(days: Int = 30): Flow<List<TypeDuration>> {
        val startDate = getDateDaysAgo(days)
        return dao.getTotalMinutesByType(startDate)
    }

    /** Datos para gráfico de barras **/
    fun getBarChartData(days: Int = 7): Flow<List<DailyMinutes>> {
        val startDate = getDateDaysAgo(days)
        return dao.getDailyMinutes(startDate)
    }

    /** Eliminar una sesión **/
    suspend fun deleteSession(session: MeditationSession) {
        dao.deleteSession(session)
    }

    /** Actualizar una sesión **/
    suspend fun updateSession(session: MeditationSession) {
        dao.updateSession(session)
    }

    /** Obtener sesiones por tipo **/
    fun getSessionsByType(type: SessionType): Flow<List<MeditationSession>> {
        return dao.getSessionsByType(type)
    }

    /** Calcular estadísticas generales **/
    suspend fun getStatistics(sessions: List<MeditationSession>): SessionStatistics {
        return SessionStatistics(
            totalSessions = sessions.size,
            totalMinutes = sessions.sumOf { it.durationMinutes },
            averageMinutes = if (sessions.isNotEmpty())
                sessions.sumOf { it.durationMinutes } / sessions.size
            else 0,
            averageMood = if (sessions.isNotEmpty())
                sessions.sumOf { it.mood }.toFloat() / sessions.size
            else 0f,
            mostFrequentType = sessions
                .groupBy { it.type }
                .maxByOrNull { it.value.size }
                ?.key ?: SessionType.MEDITATION
        )
    }

    /** Función auxiliar: obtener fecha hace X días **/
    private fun getDateDaysAgo(days: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -days)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}

/**
 * Data class para estadísticas generales
 */
data class SessionStatistics(
    val totalSessions: Int,
    val totalMinutes: Int,
    val averageMinutes: Int,
    val averageMood: Float,
    val mostFrequentType: SessionType
)
