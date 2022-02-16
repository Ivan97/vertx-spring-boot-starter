package com.alibaba.hanabi

import cn.hutool.extra.spring.SpringUtil
import com.alibaba.hanabi.VertxConstants.Companion.DEFAULT_DEPLOYMENT_OPTIONS
import io.vertx.core.AsyncResult
import io.vertx.core.DeploymentOptions
import io.vertx.core.Verticle
import io.vertx.core.Vertx
import mu.KotlinLogging
import org.springframework.context.SmartLifecycle
import org.springframework.stereotype.Component
import org.springframework.util.ClassUtils
import java.util.*

/**
 * @author 龙也
 * @date 2022/2/14 1:05 PM
 */
@Component
class HanabiVerticleLifecycle(val vertx: Vertx) : SmartLifecycle {

    val logger = KotlinLogging.logger {}

    companion object {
        const val DEFAULT_DEPLOYMENT_OPTION_SUFFIX = "@VerticleDeploymentOption"
    }

    private var running: Boolean = false

    override fun start() {

        SpringUtil.getBeansOfType(Verticle::class.java)
            .forEach { (_, verticle) ->
                HanabiVerticleHolder.INACTIVE_VERTICLES.put(verticle.javaClass.name, "", verticle)
            }

        HanabiVerticleHolder.INACTIVE_VERTICLES.values().forEach { verticle ->
            val verticleClass = verticle.javaClass
            val namedVerticleDeploymentOption = verticleClass.getAnnotation(NamedVerticleDeploymentOption::class.java)
            val verticleDeploymentOption = verticleClass.getAnnotation(VerticleDeploymentOption::class.java)

            val optionName: String
            val deploymentOptions: DeploymentOptions
            if (Objects.nonNull(verticleDeploymentOption)) {
                optionName = verticle.javaClass.simpleName + DEFAULT_DEPLOYMENT_OPTION_SUFFIX
                deploymentOptions = DeploymentOptions()
                    .setHa(verticleDeploymentOption.ha)
                    .setInstances(verticleDeploymentOption.instances)
                    .setMaxWorkerExecuteTime(verticleDeploymentOption.maxWorkerExecuteTime)
                    .setMaxWorkerExecuteTimeUnit(verticleDeploymentOption.maxWorkerExecuteTimeUnit)
                    .setWorker(verticleDeploymentOption.worker)
                    .setWorkerPoolSize(verticleDeploymentOption.workerPoolSize)
                if (verticleDeploymentOption.workerPoolName.isNotBlank()) {
                    deploymentOptions.workerPoolName = verticleDeploymentOption.workerPoolName
                }
            } else {
                optionName = if (Objects.nonNull(namedVerticleDeploymentOption)) {
                    namedVerticleDeploymentOption.value
                } else {
                    DEFAULT_DEPLOYMENT_OPTIONS
                }
                deploymentOptions = SpringUtil.getBean(optionName, DeploymentOptions::class.java)
            }

            vertx.deployVerticle(
                VertxConstants.VERTICLE_PREFIX + ":" + verticleClass.name,
                deploymentOptions
            ) { res: AsyncResult<String?> ->
                if (res.succeeded()) {
                    val className: String = ClassUtils.getUserClass(verticleClass).simpleName
                    logger.info(
                        "deployed verticle [{}] with deploymentOption [{}],id [{}].",
                        className,
                        optionName,
                        res.result()
                    )
                    HanabiVerticleHolder.INACTIVE_VERTICLES.row(verticleClass.name).remove("")
                    HanabiVerticleHolder.ACTIVE_VERTICLES.row(verticleClass.name)[res.result()] = verticle
                } else {
                    logger.error("error with deploy verticle " + verticleClass.name, res.cause())
                }
            }
        }
        this.running = true
    }

    @Synchronized
    override fun stop() {
        this.running = false
    }

    override fun isRunning(): Boolean {
        return this.running
    }
}