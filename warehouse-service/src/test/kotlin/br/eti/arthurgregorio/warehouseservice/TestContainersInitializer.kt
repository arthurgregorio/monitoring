package br.eti.arthurgregorio.warehouseservice

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.BindMode.READ_ONLY
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.SNS
import org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS
import org.testcontainers.lifecycle.Startables
import org.testcontainers.utility.DockerImageName.parse

class TestContainersInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(context: ConfigurableApplicationContext) {

        val properties = mapOf(
            "spring.cloud.aws.endpoint" to localstackContainer.getEndpointOverride(SQS).toString()
        )

        TestPropertyValues.of(properties).applyTo(context.environment)
    }

    companion object {

        private const val LOCALSTACK_IMAGE = "localstack/localstack:3.4"

        private const val REGION = "us-west-2"
        private const val ACCESS_KEY = "localstack"
        private const val ACCESS_KEY_ID = "localstack"
        private const val INIT_SCRIPT = "localstack-init.sh"
        private const val INIT_SCRIPT_LOCATION = "/etc/localstack/init/ready.d/init-aws.sh"

        private val localstackContainer = LocalStackContainer(parse(LOCALSTACK_IMAGE))
            .withServices(SQS, SNS)
            .withEnv("AWS_ACCESS_KEY_ID", ACCESS_KEY_ID)
            .withEnv("AWS_SECRET_ACCESS_KEY", ACCESS_KEY)
            .withEnv("AWS_DEFAULT_REGION", REGION)
            .withClasspathResourceMapping(INIT_SCRIPT, INIT_SCRIPT_LOCATION, READ_ONLY)

        init {
            Startables.deepStart(localstackContainer).join()
        }
    }
}