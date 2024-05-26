package br.eti.arthurgregorio.warehouseservice.application.consumers

import br.eti.arthurgregorio.warehouseservice.domain.model.SensorData
import br.eti.arthurgregorio.warehouseservice.domain.service.SensorMonitoringService
import io.awspring.cloud.sqs.annotation.SqsListener
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.stereotype.Component
import java.math.BigDecimal

private val logger = KotlinLogging.logger {}

@Component
class SensorDataStorageConsumer(
    private val sensorMonitoringService: SensorMonitoringService
) {

    @SqsListener("\${application.sensors.central-store-queue-name}")
    fun onMessage(message: SensorData, @Headers headers: MessageHeaders) {
        logger.debug { "Trying to consume message [${headers.id}]" }
        sensorMonitoringService.storeAndMonitor(SensorData("1", BigDecimal.TEN))
    }
}