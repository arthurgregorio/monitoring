package br.eti.arthurgregorio.warehouseservice.application.handlers

import br.eti.arthurgregorio.warehouseservice.domain.model.SensorData
import br.eti.arthurgregorio.warehouseservice.domain.services.SensorService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.integration.annotation.MessageEndpoint
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.messaging.Message

private val logger = KotlinLogging.logger {}

@MessageEndpoint
class SensorDataHandler(
    private val sensorService: SensorService
) {

    @ServiceActivator(inputChannel = "sensorDataChannel")
    fun handle(message: Message<SensorData>) {

        val payload = message.payload

        logger.debug { "Received data from sensor [${payload.sensorId}] to be stored" }

        sensorService.storeData(payload)
    }
}