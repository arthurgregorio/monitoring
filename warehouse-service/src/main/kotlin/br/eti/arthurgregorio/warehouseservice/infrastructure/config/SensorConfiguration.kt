package br.eti.arthurgregorio.warehouseservice.infrastructure.config

import br.eti.arthurgregorio.warehouseservice.infrastructure.config.properties.SensorProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.ip.dsl.Udp

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SensorProperties::class)
class SensorConfiguration(
    private val sensorProperties: SensorProperties
) {

    @Bean
    fun eastCornerSensor(): IntegrationFlow {

        val sensorName = "east-corner"

        val configuration = sensorProperties.getByNameOrThrown(sensorName)

        return IntegrationFlow.from(Udp.inboundAdapter(configuration.port))
            .channel(sensorName)
            .get()
    }
}