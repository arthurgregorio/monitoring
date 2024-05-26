package br.eti.arthurgregorio.warehouseservice.infrastructure.config

import br.eti.arthurgregorio.warehouseservice.domain.model.SensorData
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.dsl.StandardIntegrationFlow
import org.springframework.integration.dsl.integrationFlow
import org.springframework.integration.ip.dsl.Udp
import java.math.BigDecimal

@Configuration(proxyBeanMethods = false)
class SensorConfiguration(
    @Value("\${application.sensors.humidity.port}")
    private val humiditySensorPort: Int,
    @Value("\${application.sensors.temperature.port}")
    private val temperatureSensorPort: Int

) {

    @Bean
    fun configureTemperatureSensor(): StandardIntegrationFlow =
        integrationFlow(Udp.inboundAdapter(temperatureSensorPort)) {
            transform<String> { transformData(it) }
            channel("sensorDataChannel")
        }

    @Bean
    fun configureHumiditySensor(): StandardIntegrationFlow =
        integrationFlow(Udp.inboundAdapter(humiditySensorPort)) {
            transform<String> { transformData(it) }
            channel("sensorDataChannel")
        }

    private fun transformData(input: String): SensorData {

        val map = input
            .split(";")
            .associate {
                val (key, value) = it.split("=")
                key.trim() to value.trim()
            }

        val sensorId = map.getOrDefault("sensor_id", "no_id")
        val value = map.getOrDefault("value", "0")

        return SensorData(sensorId, BigDecimal(value))
    }
}