package org.mtransit.commons

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CleanUtilTests {

    @Before
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
        Assert.assertEquals("via Bbb ccc", result)
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
        Assert.assertEquals("Aaa", result)
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
        Assert.assertEquals(tripHeadsign, result)
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
        Assert.assertEquals(tripHeadsign, result)
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
        Assert.assertEquals(Constants.EMPTY, result)
    }
}