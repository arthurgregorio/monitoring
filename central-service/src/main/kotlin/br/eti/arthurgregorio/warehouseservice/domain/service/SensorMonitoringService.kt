package br.eti.arthurgregorio.warehouseservice.domain.service

import br.eti.arthurgregorio.warehouseservice.domain.model.SensorData
import br.eti.arthurgregorio.warehouseservice.domain.model.SensorData.Type.HUMIDITY
import br.eti.arthurgregorio.warehouseservice.domain.model.SensorData.Type.TEMPERATURE
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.math.BigDecimal

private val logger = KotlinLogging.logger {}

@Service
class SensorMonitoringService {

    fun storeAndMonitor(data: SensorData) {

        logger.info { "Storing data for sensor [${data.sensorId}] with value [${data.value}]" }

        when (data.getType()) {
            TEMPERATURE -> {
                if (data.value > BigDecimal(TEMPERATURE_THRESHOLD)) {
                    logger.warn { "Temperature sensor [${data.sensorId}] is above the threshold [${TEMPERATURE_THRESHOLD}], current [${data.value}]" }
                }
            }

            HUMIDITY -> {
                if (data.value > BigDecimal(HUMIDITY_THRESHOLD)) {
                    logger.warn { "Humidity sensor [${data.sensorId}] is above the threshold [${HUMIDITY_THRESHOLD}], current [${data.value}]" }
                }
            }
        }
    }

    companion object {
        private const val TEMPERATURE_THRESHOLD = 35
        private const val HUMIDITY_THRESHOLD = 50
    }
}