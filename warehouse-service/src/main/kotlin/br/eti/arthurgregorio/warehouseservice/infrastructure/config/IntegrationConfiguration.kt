package br.eti.arthurgregorio.warehouseservice.infrastructure.config

import org.springframework.context.annotation.Configuration
import org.springframework.integration.config.EnableIntegration

@EnableIntegration
@Configuration(proxyBeanMethods = false)
class IntegrationConfiguration