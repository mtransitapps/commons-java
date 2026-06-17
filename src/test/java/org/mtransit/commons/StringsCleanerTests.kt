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
    fun test_cleanStopName() {
        "Station Mont-Royal".let { stopName ->
            StringsCleaner.cleanStopName(stopName, languages = listOf(Locale.FRENCH), routeType = 1) // subway
        }.let { result ->
            assertEquals("Mont-Royal", result)
        }
        "Station Édouard-Montpetit".let { stopName ->
            StringsCleaner.cleanStopName(stopName, languages = listOf(Locale.FRENCH), routeType = 1) // subway
        }.let { result ->
            assertEquals("Édouard-Montpetit", result)
        }
        "Union Station".let { stopName ->
            StringsCleaner.cleanStopName(stopName, languages = listOf(Locale.ENGLISH), routeType = 1) // subway
        }.let { result ->
            assertEquals("Union", result)
        }
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
        "Yonge-University Line".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH), routeType = 1)
        }.let { result ->
            assertEquals("Yonge-University", result)
        }
        "Tenth Line <> Place D'Orléans".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH), routeType = 3)
        }.let { result ->
            assertEquals("Tenth Line <> Place D'Orléans", result)
        }
        "Place D'Orléans <> Tenth Line".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH), routeType = 3)
        }.let { result ->
            assertEquals("Place D'Orléans <> Tenth Line", result)
        }
        "Tenth Line".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH), routeType = 3)
        }.let { result ->
            assertEquals("Tenth", result)
        }
        "Place Orléans Line".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH), routeType = 3)
        }.let { result ->
            assertEquals("Place Orléans", result)
        }
        "Place D'Orléans Line".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH), routeType = 3)
        }.let { result ->
            assertEquals("Place D'Orléans", result)
        }
        "Line 10".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH), routeType = 3)
        }.let { result ->
            assertEquals("10", result)
        }
        " Line 10".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH), routeType = 3)
        }.let { result ->
            assertEquals("10", result)
        }
        "The Line 10".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH), routeType = 3)
        }.let { result ->
            assertEquals("The 10", result)
        }
        "Online 10".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH), routeType = 3)
        }.let { result ->
            assertEquals("Online 10", result)
        }
        "John McCrae H.S <> Half Moon Bay".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.ENGLISH, Locale.FRENCH), routeType = 3, lowerUCWords = true)
        }.let { result ->
            assertEquals("John McCrae HS <> Half Moon Bay", result)
        }
        "Ligne Bleue".let {
            StringsCleaner.cleanRouteLongName(it, languages = listOf(Locale.FRENCH), routeType = 1)
        }.let { result ->
            assertEquals("Bleue", result)
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
