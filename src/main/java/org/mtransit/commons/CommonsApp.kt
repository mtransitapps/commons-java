package org.mtransit.commons

object CommonsApp {

    @JvmStatic
    fun setup(isAndroidPlatform: Boolean) {
        RegexUtils.isAndroid = isAndroidPlatform
    }
}