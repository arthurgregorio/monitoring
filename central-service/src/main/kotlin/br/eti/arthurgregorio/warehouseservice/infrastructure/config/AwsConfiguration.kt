package br.eti.arthurgregorio.warehouseservice.infrastructure.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory
import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.services.sqs.SqsAsyncClient

@Configuration(proxyBeanMethods = false)
class AwsConfiguration(
    private val objectMapper: ObjectMapper
) {

    @Bean
    fun defaultSqsListenerContainerFactory(asyncClient: SqsAsyncClient): SqsMessageListenerContainerFactory<Any> {

        val messageConverter = SqsMessagingMessageConverter()
        messageConverter.setObjectMapper(objectMapper)

        return SqsMessageListenerContainerFactory
            .builder<Any>()
            .configure {
                it.messageConverter(messageConverter)
            }
            .sqsAsyncClient(asyncClient)
            .build()
    }
}