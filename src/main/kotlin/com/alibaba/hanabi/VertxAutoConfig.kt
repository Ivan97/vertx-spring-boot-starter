package com.alibaba.hanabi

import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.core.file.FileSystem
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * @author 龙也
 * @date 2022/2/16 1:57 PM
 */
@Configuration
@ComponentScan("com.alibaba.hanabi")
class VertxAutoConfig {

    @Bean
    @ConditionalOnMissingBean(Vertx::class)
    fun vertx(): Vertx {
        return Vertx.vertx()
    }

    @Bean
    fun eventBus(vertx: Vertx): EventBus {
        return vertx.eventBus()
    }

    @Bean
    fun fileSystem(vertx: Vertx): FileSystem {
        return vertx.fileSystem()
    }
}