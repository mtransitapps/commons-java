package org.mtransit.commons

import kotlinx.datetime.DatePeriod
import kotlin.test.Test
import kotlin.test.assertEquals

class DateTimeKtxTests {

    @Test
    fun test_DatePeriod_weeks() {
        DatePeriod(days = 0)
            .weeks.let { result ->
                assertEquals(0, result)
            }
        DatePeriod(days = 6)
            .weeks.let { result ->
                assertEquals(0, result)
            }
        DatePeriod(days = 7)
            .weeks.let { result ->
                assertEquals(1, result)
            }
        DatePeriod(days = 8)
            .weeks.let { result ->
                assertEquals(0, result)
            }
        DatePeriod(months = 1)
            .weeks.let { result ->
                assertEquals(0, result)
            }
        DatePeriod(months = 1, days = 7)
            .weeks.let { result ->
                assertEquals(0, result)
            }
    }
}
