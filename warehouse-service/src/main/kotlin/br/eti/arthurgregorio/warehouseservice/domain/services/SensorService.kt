package br.eti.arthurgregorio.warehouseservice.domain.services

import br.eti.arthurgregorio.warehouseservice.application.payloads.SensorData
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class SensorService {

    fun storeData(data: SensorData) {
        logger.info { "Received data [$data]" }
    }
}