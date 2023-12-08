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

    // const val F_ACCESSIBILITY_PRODUCER = false
    const val F_ACCESSIBILITY_PRODUCER = true // WIP // data sources
    // const val F_ACCESSIBILITY_CONSUMER = false
    const val F_ACCESSIBILITY_CONSUMER = true // WIP // UI

    const val F_EDGE_TO_EDGE = false
    // const val F_EDGE_TO_EDGE = true // WIP

    // const val F_WORK_MANAGER_DB_DEPLOY = false
    const val F_WORK_MANAGER_DB_DEPLOY = true // WIP

    // const val F_MODULE_AUTO_OPEN = false
    const val F_MODULE_AUTO_OPEN = true // WIP

    const val F_EXPORT_GTFS_ID_HASH_INT = false
    // const val F_EXPORT_GTFS_ID_HASH_INT = true // WIP

    const val F_USE_GTFS_ID_HASH_INT = false
    // const val F_USE_GTFS_ID_HASH_INT = true // WIP

    // @formatter:on
}