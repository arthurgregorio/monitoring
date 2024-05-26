package br.eti.arthurgregorio.warehouseservice.domain.services

import br.eti.arthurgregorio.warehouseservice.domain.model.SensorData
import br.eti.arthurgregorio.warehouseservice.infrastructure.producers.CentralDataStoreProducer
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class SensorService(
    private val centralDataStoreProducer: CentralDataStoreProducer
) {

    fun storeData(data: SensorData) {
        centralDataStoreProducer.send(data)
    }
}