package com.alibaba.hanabi

import cn.hutool.aop.ProxyUtil
import cn.hutool.aop.aspects.SimpleAspect
import cn.hutool.core.util.IdUtil
import io.vertx.core.Promise
import io.vertx.core.Verticle
import io.vertx.core.Vertx
import io.vertx.core.spi.VerticleFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.DefaultListableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.stereotype.Component
import java.util.concurrent.Callable

/**
 * @author 龙也
 * @date 2022/2/14 12:08 PM
 */
@Component
class HanabiVerticleFactory : VerticleFactory, ApplicationContextAware {

    private lateinit var applicationContext: AbstractApplicationContext

    /**
     * @return  The prefix for the factory, e.g. "java", or "js".
     */
    override fun prefix(): String {
        return VertxConstants.VERTICLE_PREFIX
    }

    /**
     * Create a verticle instance. If this method is likely to be slow (e.g. Ruby or JS verticles which might have to
     * start up a language engine) then make sure it is run on a worker thread by [Vertx.executeBlocking].
     *
     * @param verticleName  The verticle name
     * @param classLoader  The class loader
     * @param promise the promise to complete with the result
     */
    override fun createVerticle(
        verticleName: String?,
        classLoader: ClassLoader?,
        promise: Promise<Callable<Verticle>>?
    ) {
        val clazz = VerticleFactory.removePrefix(verticleName)
        val verticle = HanabiVerticleHolder.INACTIVE_VERTICLES.rowMap()[clazz]?.values?.iterator()?.next()
        verticle?.apply {
            promise?.complete(Callable<Verticle> {
                register(this)
                ProxyUtil.proxy(verticle, VerticleAspect::class.java)
            })
        }
    }

    private fun register(verticle: Verticle) {
        val beanFactory = applicationContext.beanFactory as DefaultListableBeanFactory
        val builder = BeanDefinitionBuilder.rootBeanDefinition(verticle.javaClass)
        val beanName = "${verticle.javaClass.simpleName}\$${IdUtil.fastSimpleUUID()}"
        beanFactory.registerBeanDefinition(beanName, builder.beanDefinition)
        CustomizedBeanHolder.register(beanName, verticle)
    }

    class VerticleAspect : SimpleAspect()

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext as AbstractApplicationContext
    }
}