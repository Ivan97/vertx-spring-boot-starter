package com.alibaba.hanabi

import com.google.common.collect.HashBasedTable
import io.vertx.core.Verticle

/**
 * @author 龙也
 * @date 2022/2/14 1:32 PM
 */
object HanabiVerticleHolder {

    val ACTIVE_VERTICLES = HashBasedTable.create<String, String, Verticle>()
    val INACTIVE_VERTICLES = HashBasedTable.create<String, String, Verticle>()
}