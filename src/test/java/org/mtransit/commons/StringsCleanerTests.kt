package org.mtransit.commons

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StringsCleanerTests {

    @BeforeTest
    fun setUp() {
        CommonsApp.setup(false);
    }

    @Test
    fun test_cleanTripHeadsign() {
        "Terrebonne / Mascouche".let { tripHeadsign ->
            StringsCleaner.cleanTripHeadsign(tripHeadsign, languages = null, routeType = 3)
        }.let { result ->
            assertEquals("Terrebonne/Mascouche", result)
        }
        "Terrebonne/Mascouche".let { tripHeadsign ->
            StringsCleaner.cleanTripHeadsign(tripHeadsign, languages = null, routeType = 3)
        }.let { result ->
            assertEquals("Terrebonne/Mascouche", result)
        }
        "Angora / Gascon / Terrebonne / Cégep Terrebonne".let { tripHeadsign ->
            StringsCleaner.cleanTripHeadsign(tripHeadsign, languages = null, routeType = 3)
        }.let { result ->
            assertEquals("Angora/Gascon/Terrebonne/Cégep Terrebonne", result)
        }
        "Angora/Gascon / Terrebonne/Cégep Terrebonne".let { tripHeadsign ->
            StringsCleaner.cleanTripHeadsign(tripHeadsign, languages = null, routeType = 3)
        }.let { result ->
            assertEquals("Angora/Gascon/Terrebonne/Cégep Terrebonne", result)
        }
        "Angora/Gascon/Terrebonne/Cégep Terrebonne".let { tripHeadsign ->
            StringsCleaner.cleanTripHeadsign(tripHeadsign, languages = null, routeType = 3)
        }.let { result ->
            assertEquals("Angora/Gascon/Terrebonne/Cégep Terrebonne", result)
        }
    }
}
