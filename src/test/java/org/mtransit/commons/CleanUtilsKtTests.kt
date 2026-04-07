package org.mtransit.commons

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CleanUtilsKtTests {

    @BeforeTest
    fun setUp() {
        CommonsApp.setup(false)
    }

    @Test
    fun test_keepOrRemoveVia_keepVia() {
        // Arrange
        val tripHeadsign = "Aaa via Bbb ccc"
        val tripHeading = "Aaa"
        val routeLongName = "Zzz"
        // Act
        val result = CleanUtils.keepOrRemoveVia(tripHeadsign) { string -> string == tripHeading || string == routeLongName }
        // Assert
        assertEquals("via Bbb ccc", result)
    }

    @Test
    fun test_keepOrRemoveVia_doNotKeepVia() {
        // Arrange
        val tripHeadsign = "Aaa via Bbb ccc"
        val tripHeading = "Mmm"
        val routeLongName = "Zzz"
        // Act
        val result = CleanUtils.keepOrRemoveVia(tripHeadsign) { string -> string == tripHeading || string == routeLongName }
        // Assert
        assertEquals("Aaa", result)
    }

    @Test
    fun test_keepOrRemoveVia_noVia() {
        // Arrange
        val tripHeadsign = "Aaa Bbb ccc"
        val tripHeading = "Mmm"
        val routeLongName = "Zzz"
        // Act
        val result = CleanUtils.keepOrRemoveVia(tripHeadsign) { string -> string == tripHeading || string == routeLongName }
        // Assert
        assertEquals(tripHeadsign, result)
    }

    @Test
    fun test_keepOrRemoveVia_onlyVia() {
        // Arrange
        val tripHeadsign = "via Bbb ccc"
        val tripHeading = "Mmm"
        val routeLongName = "Zzz"
        // Act
        val result = CleanUtils.keepOrRemoveVia(tripHeadsign) { string -> string == tripHeading || string == routeLongName }
        // Assert
        assertEquals(tripHeadsign, result)
    }

    @Test
    fun test_keepOrRemoveVia_empty() {
        // Arrange
        val tripHeadsign = Constants.EMPTY
        val tripHeading = "Mmm"
        val routeLongName = "Zzz"
        // Act
        val result = CleanUtils.keepOrRemoveVia(tripHeadsign) { string -> string == tripHeading || string == routeLongName }
        // Assert
        assertEquals(Constants.EMPTY, result)
    }

    @Test
    fun test_cleanSlashes() {
        "Angora / Gascon / Terrebonne / Cégep Terrebonne".let { string ->
            CleanUtils.cleanSlashes(string, true)
        }.let { result ->
            assertEquals("Angora/Gascon/Terrebonne/Cégep Terrebonne", result)
        }
        "Angora/Gascon / Terrebonne/Cégep Terrebonne".let { string ->
            CleanUtils.cleanSlashes(string, true)
        }.let { result ->
            assertEquals("Angora/Gascon/Terrebonne/Cégep Terrebonne", result)
        }
        "Angora/Gascon/Terrebonne/Cégep Terrebonne".let { string ->
            CleanUtils.cleanSlashes(string, true)
        }.let { result ->
            assertEquals("Angora/Gascon/Terrebonne/Cégep Terrebonne", result)
        }
    }
}
