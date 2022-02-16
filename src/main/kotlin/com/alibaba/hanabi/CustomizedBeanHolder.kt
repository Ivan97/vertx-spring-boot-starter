package com.alibaba.hanabi

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import mu.KotlinLogging

/**
 * @author 龙也
 * @date 2022/2/15 2:28 PM
 */
object CustomizedBeanHolder {

    private val logger = KotlinLogging.logger { }
    private val beanHolder: Table<String, Class<*>, Any> = HashBasedTable.create()

    fun getBean(beanName: String): Any {
        return beanHolder.row(beanName).values.first()
    }

    fun register(beanName: String, obj: Any) {
        if (beanHolder.containsRow(beanName)) {
            throw Exception("Duplicated verticle bean $beanName register action")
        } else {
            beanHolder.put(beanName, obj.javaClass, obj)
            if (logger.isDebugEnabled) {
                logger.debug { "Customized verticle bean registered.Type ${obj.javaClass.name} with name $beanName." }
            }
        }
    }

    fun customizedBeanTypes(): Set<Class<*>> {
        return beanHolder.columnKeySet()
    }

    fun getBeanOfType(clazz: Class<*>): Map<String, Any> {
        return beanHolder.column(clazz)
    }
}
