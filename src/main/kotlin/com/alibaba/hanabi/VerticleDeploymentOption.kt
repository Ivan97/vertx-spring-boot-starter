package com.alibaba.hanabi

import java.util.concurrent.TimeUnit

/**
 * @author 龙也
 * @date 2022/2/14 1:38 PM
 */

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class VerticleDeploymentOption(
    val ha: Boolean = false,
    val instances: Int = 1,
    val maxWorkerExecuteTime: Long = 60_000_000_000L,
    val maxWorkerExecuteTimeUnit: TimeUnit = TimeUnit.NANOSECONDS,
    val worker: Boolean = false,
    val workerPoolName: String = "",
    val workerPoolSize: Int = 20
)
