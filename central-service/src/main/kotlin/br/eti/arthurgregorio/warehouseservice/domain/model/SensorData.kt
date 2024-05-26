package br.eti.arthurgregorio.warehouseservice.domain.model

import java.math.BigDecimal

data class SensorData(
    val sensorId: String,
    val value: BigDecimal
) {

    fun getType(): Type =
        if (sensorId.contains("h")) {
            Type.HUMIDITY
        } else if (sensorId.contains("t")) {
            Type.TEMPERATURE
        } else {
            throw IllegalArgumentException("Invalid sensor type")
        }

    enum class Type {
        TEMPERATURE,
        HUMIDITY
    }
}
