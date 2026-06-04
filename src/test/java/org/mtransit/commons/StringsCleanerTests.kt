package org.mtransit.commons

import java.util.Locale
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StringsCleanerTests {

    @BeforeTest
    fun setUp() {
        CommonsApp.setup(false)
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
        "Bell H.S".let { tripHeadsign ->
            StringsCleaner.cleanTripHeadsign(tripHeadsign, languages = listOf(Locale.ENGLISH), routeType = 3, lowerUCWords = true)
        }.let { result ->
            assertEquals("Bell HS", result)
        }
    }

    @Test
    fun test_cleanRouteLongName() {
        "Tenth Line <> Place D'Orléans".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH), routeType = 3)
        }.let { result ->
            assertEquals("Tenth Line <> Place D'Orléans", result)
        }
        "Line 10".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH), routeType = 3)
        }.let { result ->
            assertEquals("10", result)
        }
        "The Line 10".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH), routeType = 3)
        }.let { result ->
            assertEquals("The 10", result)
        }
        "John McCrae H.S <> Half Moon Bay".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH, Locale.FRENCH), routeType = 3, lowerUCWords = true)
        }.let { result ->
            assertEquals("John McCrae HS <> Half Moon Bay", result)
        }
    }

    @Test
    fun test_cleanRouteLongName_Capitalize() {
        "Tunney's Pasture <> Bridlewood".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH), routeType = 3)
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
            assertEquals("Tunney's Pasture <> Bridlewood", result)
        }
        "tunney's pasture <> bridlewood".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH, Locale.FRENCH), routeType = 3, lowerUCWords = true)
        }.let { result ->
            assertEquals("Tunney's Pasture <> Bridlewood", result)
        }
        "tunney's pasture <> bridlewood".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH, Locale.FRENCH), routeType = 3)
        }.let { result ->
            assertEquals("Tunney's Pasture <> Bridlewood", result)
        }
    }
}
