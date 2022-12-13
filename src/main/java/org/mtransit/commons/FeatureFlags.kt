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

    // const val F_SCHEDULE_DESCENT_ONLY_UI = false
    const val F_SCHEDULE_DESCENT_ONLY_UI = true // WIP
    // const val F_SCHEDULE_DESCENT_ONLY = F_SCHEDULE_DESCENT_ONLY_UI && false
    const val F_SCHEDULE_DESCENT_ONLY = F_SCHEDULE_DESCENT_ONLY_UI && true // WIP

    // const val F_HTML_POI_NAME_UI = false
    const val F_HTML_POI_NAME_UI = true // WIP
    // const val F_HTML_POI_NAME = F_HTML_POI_NAME_UI && false
    const val F_HTML_POI_NAME = F_HTML_POI_NAME_UI && true // WIP

    const val F_SCHEDULE_INFINITE = false
    // const val F_SCHEDULE_INFINITE = true // WIP

    // @formatter:on
}