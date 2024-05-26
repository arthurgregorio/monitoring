package br.eti.arthurgregorio.warehouseservice.infrastructure.config

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@Configuration(proxyBeanMethods = false)
class CommonConfiguration