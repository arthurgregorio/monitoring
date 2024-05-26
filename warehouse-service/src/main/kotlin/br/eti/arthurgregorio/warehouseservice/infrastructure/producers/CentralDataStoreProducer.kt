package br.eti.arthurgregorio.warehouseservice.infrastructure.producers

import br.eti.arthurgregorio.warehouseservice.domain.model.SensorData
import io.awspring.cloud.sqs.operations.SqsTemplate
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class CentralDataStoreProducer(
    @Value("\${application.sensors.central-store-queue-name}")
    private val queueName: String,
    private val sqsTemplate: SqsTemplate
) {

    @Async
    fun send(data: SensorData) {
        sqsTemplate.send(queueName, data)
        logger.info { "Data for sensor [${data.sensorId}] sent to central storage" }
    }
}