package br.eti.arthurgregorio.warehouseservice.consumers

import br.eti.arthurgregorio.warehouseservice.TestContainersInitializer
import br.eti.arthurgregorio.warehouseservice.domain.model.SensorData
import br.eti.arthurgregorio.warehouseservice.memoryLogAppender
import br.eti.arthurgregorio.warehouseservice.startMemoryLogAppender
import br.eti.arthurgregorio.warehouseservice.stopMemoryLogAppender
import io.awspring.cloud.sqs.operations.SqsTemplate
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.atMost
import org.awaitility.kotlin.await
import org.awaitility.kotlin.untilAsserted
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import java.math.BigDecimal
import java.time.Duration
import java.time.temporal.ChronoUnit.SECONDS
import java.util.stream.Stream

@ActiveProfiles("test")
@ContextConfiguration(initializers = [TestContainersInitializer::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SensorDataStorageConsumerTest {

    @Value("\${application.sensors.central-store-queue-name}")
    private lateinit var queueName: String

    @Autowired
    private lateinit var sqsTemplate: SqsTemplate

    @ParameterizedTest
    @MethodSource("sensorsDataAboveThreshold")
    fun `should consume data and alarm if above the threshold`(data: SensorData, threshold: BigDecimal) {

        sqsTemplate.send(queueName, data)

        startMemoryLogAppender()

        await atMost Duration.of(20, SECONDS) untilAsserted {

            val storeMessage = "Storing data for sensor [${data.sensorId}] with value [${data.value}]"
            assertThat(memoryLogAppender.countBy(storeMessage)).isOne()

            val alarmMessage = "[${data.sensorId}] is above the threshold [$threshold]"
            assertThat(memoryLogAppender.countBy(alarmMessage)).isOne()
        }

        stopMemoryLogAppender()
    }

    @ParameterizedTest
    @MethodSource("sensorsDataBellowThreshold")
    fun `should just store data if is bellow the threshold`(data: SensorData) {

        sqsTemplate.send(queueName, data)

        startMemoryLogAppender()

        await atMost Duration.of(20, SECONDS) untilAsserted {

            val storeMessage = "Storing data for sensor [${data.sensorId}] with value [${data.value}]"
            assertThat(memoryLogAppender.countBy(storeMessage)).isOne()

            val alarmMessage = "[${data.sensorId}] is above the threshold"
            assertThat(memoryLogAppender.countBy(alarmMessage)).isZero()
        }

        stopMemoryLogAppender()
    }

    companion object {

        @JvmStatic
        fun sensorsDataAboveThreshold(): Stream<Arguments> = Stream.of(
            Arguments.of(SensorData("t1", BigDecimal(36)), BigDecimal(35)),
            Arguments.of(SensorData("h1", BigDecimal(51)), BigDecimal(50))
        )

        @JvmStatic
        fun sensorsDataBellowThreshold(): Stream<Arguments> = Stream.of(
            Arguments.of(SensorData("t1", BigDecimal(37))),
            Arguments.of(SensorData("h1", BigDecimal(49)))
        )
    }
}