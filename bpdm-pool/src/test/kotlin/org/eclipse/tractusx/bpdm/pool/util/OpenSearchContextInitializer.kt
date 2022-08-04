package org.eclipse.tractusx.bpdm.pool.util

import com.github.dockerjava.api.model.Ulimit
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy

/**
 * When used on a spring boot test, starts a singleton opensearch container that is shared between all integration tests.
 */
class OpenSearchContextInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    companion object {
        private const val OPENSEARCH_PORT = 9200

        private val openSearchContainer: GenericContainer<*> = GenericContainer("opensearchproject/opensearch:2.1.0")
            .withExposedPorts(OPENSEARCH_PORT)
            .waitingFor(HttpWaitStrategy()
                .forPort(OPENSEARCH_PORT)
                .forStatusCodeMatching { response -> response == 200 || response == 401 }
            )
            // based on sample docker-compose for development from https://opensearch.org/docs/latest/opensearch/install/docker
            .withEnv("cluster.name", "opensearch-cluster")
            .withEnv("node.name", "bpdm-opensearch")
            .withEnv("bootstrap.memory_lock", "true")
            .withEnv("OPENSEARCH_JAVA_OPTS", "-Xms512m -Xmx512m")
            .withEnv("DISABLE_INSTALL_DEMO_CONFIG", "true")
            .withEnv("DISABLE_SECURITY_PLUGIN", "true")
            .withEnv("discovery.type", "single-node")
            .withCreateContainerCmdModifier { cmd ->
                cmd.hostConfig!!.withUlimits(arrayOf(Ulimit("nofile", 65536L, 65536L), Ulimit("memlock", -1L, -1L)))
            }
    }

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        openSearchContainer.start()
        TestPropertyValues.of(
            "bpdm.opensearch.host=${openSearchContainer.host}",
            "bpdm.opensearch.port=${openSearchContainer.getMappedPort(OPENSEARCH_PORT)}",
            "bpdm.opensearch.scheme=http",
            "bpdm.opensearch.enabled=true"
        ).applyTo(applicationContext.environment)
    }
}