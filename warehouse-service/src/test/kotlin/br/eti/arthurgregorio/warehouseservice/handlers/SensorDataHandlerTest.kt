package br.eti.arthurgregorio.warehouseservice.handlers

import br.eti.arthurgregorio.warehouseservice.TestContainersInitializer
import br.eti.arthurgregorio.warehouseservice.domain.model.SensorData
import io.awspring.cloud.sqs.operations.SqsTemplate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import java.math.BigDecimal
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.stream.Stream
import kotlin.jvm.optionals.getOrNull

@ActiveProfiles("test")
@ContextConfiguration(initializers = [TestContainersInitializer::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SensorDataHandlerTest {

    @Value("\${application.sensors.central-store-queue-name}")
    private lateinit var queueName: String

    @Autowired
    private lateinit var sqsTemplate: SqsTemplate

    @ParameterizedTest
    @MethodSource("sensorsData")
    fun `should receive data and send to queue to store`(data: String, port: Int, expectedData: SensorData) {

        sendDatagramPacket(data, port)

        val received = sqsTemplate.receive(queueName, SensorData::class.java)
            .getOrNull()

        assertThat(received)
            .isNotNull
            .extracting { it?.payload }
            .satisfies({
                assertThat(it?.sensorId).isEqualTo(expectedData.sensorId)
                assertThat(it?.value).isEqualTo(expectedData.value)
            })
    }

    private fun sendDatagramPacket(data: String, port: Int, host: String = "localhost") {

        val address = InetAddress.getByName(host)

        val datagramPacket = DatagramPacket(data.toByteArray(), data.length, address, port)

        val datagramSocket = DatagramSocket()
        datagramSocket.send(datagramPacket)
    }

    companion object {

        @JvmStatic
        fun sensorsData(): Stream<Arguments> = Stream.of(
            Arguments.of("sensor_id=t1; value=20", 3344, SensorData("t1", BigDecimal(20))),
            Arguments.of("sensor_id=h1; value=30", 3355, SensorData("h1", BigDecimal(30)))
        )
    }
}