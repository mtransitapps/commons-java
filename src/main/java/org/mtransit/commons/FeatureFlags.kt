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

    // const val F_WORK_MANAGER_DB_DEPLOY = false
    const val F_WORK_MANAGER_DB_DEPLOY = true // WIP
    // const val F_PROVIDER_DEPLOY_SYNC_GTFS_ONLY = false
    const val F_PROVIDER_DEPLOY_SYNC_GTFS_ONLY = true // WIP

    // const val F_MODULE_AUTO_OPEN = false
    const val F_MODULE_AUTO_OPEN = true // WIP

    // const val F_USE_ROUTE_TYPE_FILTER = false
    // TODO @Deprecated("remove this flag", ReplaceWith("true"))
    const val F_USE_ROUTE_TYPE_FILTER = true // WIP

    // const val F_AVOID_DATA_CHANGED = false
    const val F_AVOID_DATA_CHANGED = true // WIP

    const val F_EXPORT_SERVICE_ID_INTS = false
    // const val F_EXPORT_SERVICE_ID_INTS = true // WIP // only marginal gains in APK size because of Android optimizations

    const val F_EXPORT_STRINGS = false
    // const val F_EXPORT_STRINGS = true // WIP // only marginal gains in APK size because of Android optimizations

    const val F_EXPORT_TRIP_ID_INTS = false
    // const val F_EXPORT_TRIP_ID_INTS = true // WIP // only marginal gains in APK size because of Android optimizations

    const val F_EXPORT_TRIP_ID_ARRIVAL = false
    // const val F_EXPORT_TRIP_ID_ARRIVAL = true // WIP

    // @formatter:on
}