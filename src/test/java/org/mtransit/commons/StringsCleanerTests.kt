package org.mtransit.commons

import java.util.Locale
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

    @Test
    fun test_cleanRouteLongName() {
        "Tunney's Pasture <> Bridlewood".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH), routeType = 3)
        }.let { result ->
            assertEquals("Tunney's Pasture <> Bridlewood", result)
        }
        "Tunney's Pasture <> Bridlewood".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.FRENCH, Locale.ENGLISH), routeType = 3)
        }.let { result ->
            assertEquals("Tunney's Pasture <> Bridlewood", result)
        }
        "Tunney's Pasture <> Bridlewood".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH, Locale.FRENCH), routeType = 3)
        }.let { result ->
            assertEquals("Tunney's Pasture <> Bridlewood", result)
        }
        "Tunney's Pasture <> Bridlewood".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH, Locale.FRENCH), routeType = 3, lowerUCWords = true)
        }.let { result ->
            assertEquals("Tunney'S Pasture <> Bridlewood", result) // too bad
        }

    }
}
