package br.eti.arthurgregorio.warehouseservice.application.handlers

import br.eti.arthurgregorio.warehouseservice.application.payloads.SensorData
import br.eti.arthurgregorio.warehouseservice.domain.services.SensorService
import org.springframework.integration.annotation.MessageEndpoint
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.messaging.Message

@MessageEndpoint
class SensorDataHandler(
    private val sensorService: SensorService
) {

    @ServiceActivator(inputChannel = "sensorDataChannel")
    fun handle(message: Message<SensorData>) {
        sensorService.storeData(message.payload)
    }
}