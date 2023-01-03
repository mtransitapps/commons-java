package org.mtransit.commons

object CommonsApp {

    @JvmField
    var isAndroid: Boolean? = null

    @JvmStatic
    fun setup(isAndroidPlatform: Boolean) {
        this.isAndroid = isAndroidPlatform
    }
}