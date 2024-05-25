package br.eti.arthurgregorio.warehouseservice.infrastructure.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "application.sensors")
data class SensorProperties(
    val humidity: Map<String, SensorConfigurationData> = mapOf(),
    val temperature: Map<String, SensorConfigurationData> = mapOf()
) {

    fun getByNameOrThrown(name: String) =
        humidity[name] ?: temperature[name] ?: throw IllegalArgumentException("Sensor [$name] not found")
}

data class SensorConfigurationData(
    val port: Int
)