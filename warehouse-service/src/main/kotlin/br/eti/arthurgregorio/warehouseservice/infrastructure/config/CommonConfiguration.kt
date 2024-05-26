package br.eti.arthurgregorio.warehouseservice.infrastructure.config

import org.springframework.context.annotation.Configuration
import org.springframework.integration.config.EnableIntegration
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@EnableIntegration
@Configuration(proxyBeanMethods = false)
class CommonConfiguration