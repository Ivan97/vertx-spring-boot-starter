package com.alibaba.hanabi

import io.vertx.core.Vertx
import mu.KotlinLogging
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

/**
 * @author 龙也
 * @date 2022/2/14 12:11 PM
 */
@Component
class HanabiVertxPostProcessor : BeanPostProcessor, ApplicationContextAware {

    private val logger = KotlinLogging.logger {}

    private var applicationContext: ApplicationContext? = null

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        if (bean is Vertx) {
            val factory = applicationContext!!.getBean(HanabiVerticleFactory::class.java)
            if (!bean.verticleFactories().contains(factory)) {
                bean.registerVerticleFactory(factory)
                if (logger.isDebugEnabled) {
                    logger.debug { "VerticleFactory registered" }
                }
            }
        }
        return bean
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }
}