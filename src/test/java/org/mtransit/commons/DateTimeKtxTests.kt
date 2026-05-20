package org.mtransit.commons

import kotlinx.datetime.DatePeriod
import kotlin.test.Test

class DateTimeKtxTests {

    @Test
    fun test_DatePeriod_weeks() {
        DatePeriod(days = 0)
            .weeks.let { result ->
                assert(result == 0)
            }
        DatePeriod(days = 6)
            .weeks.let { result ->
                assert(result == 0)
            }
        DatePeriod(days = 7)
            .weeks.let { result ->
                assert(result == 1)
            }
        DatePeriod(days = 8)
            .weeks.let { result ->
                assert(result == 0)
            }
        DatePeriod(months = 1)
            .weeks.let { result ->
                assert(result == 0)
            }
        DatePeriod(months = 1, days = 7)
            .weeks.let { result ->
                assert(result == 0)
            }
    }
}
