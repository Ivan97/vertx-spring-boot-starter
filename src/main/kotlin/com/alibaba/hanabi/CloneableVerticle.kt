package com.alibaba.hanabi

import cn.hutool.core.clone.Cloneable
import io.vertx.core.*

class CloneableVerticle(
    private val verticle: Verticle
) : Verticle, Cloneable<Verticle> {
    override fun getVertx(): Vertx {
        return verticle.vertx
    }

    override fun init(vertx: Vertx?, context: Context?) {
        verticle.init(vertx, context)
    }

    override fun start(startPromise: Promise<Void>?) {
        verticle.start(startPromise)
    }

    override fun stop(stopPromise: Promise<Void>?) {
        verticle.stop(stopPromise)
    }

    override fun clone(): Verticle {
        return this
    }
}