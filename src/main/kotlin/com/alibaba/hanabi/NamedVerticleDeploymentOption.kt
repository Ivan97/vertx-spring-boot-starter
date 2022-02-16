package com.alibaba.hanabi

import com.alibaba.hanabi.VertxConstants.Companion.DEFAULT_DEPLOYMENT_OPTIONS
import java.lang.annotation.Inherited


/**
 * @author 龙也
 * @date 2022/2/14 1:41 PM
 */

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Inherited
annotation class NamedVerticleDeploymentOption(
    val value: String = DEFAULT_DEPLOYMENT_OPTIONS
)
