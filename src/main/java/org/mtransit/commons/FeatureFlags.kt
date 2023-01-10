package org.mtransit.commons

@Suppress("unused", "SimplifyBooleanWithConstants")
object FeatureFlags {
    // @formatter:off

    const val F_PRE_FILLED_DB = false
    // const val F_PRE_FILLED_DB = true // WIP

    const val F_TRANSITION = false
    // const val F_TRANSITION = true // WIP

    const val F_NAVIGATION = false
    // const val F_NAVIGATION = true // WIP

    // const val F_SCHEDULE_INFINITE = false
    const val F_SCHEDULE_INFINITE = true // WIP

    const val F_ACCESSIBILITY = false
    // const val F_ACCESSIBILITY = true // WIP

    const val F_ACCESSIBILITY_PRODUCER = false
    // const val F_ACCESSIBILITY_PRODUCER = true // WIP // data sources

    const val F_ACCESSIBILITY_CONSUMER = false
    // const val F_ACCESSIBILITY_CONSUMER = true // WIP // UI

    // @formatter:on
}