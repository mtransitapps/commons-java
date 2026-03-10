@file:Suppress("FunctionName")

package org.mtransit.commons

import org.mtransit.commons.CleanUtils.PLACE_CHAR_ARRONDISSEMENT
import org.mtransit.commons.CleanUtils.PLACE_CHAR_AV
import org.mtransit.commons.CleanUtils.PLACE_CHAR_AVENUE
import org.mtransit.commons.CleanUtils.PLACE_CHAR_BOUL
import org.mtransit.commons.CleanUtils.PLACE_CHAR_BOULEVARD
import org.mtransit.commons.CleanUtils.PLACE_CHAR_CH
import org.mtransit.commons.CleanUtils.PLACE_CHAR_CIVIQUE
import org.mtransit.commons.CleanUtils.PLACE_CHAR_CROISS
import org.mtransit.commons.CleanUtils.PLACE_CHAR_D
import org.mtransit.commons.CleanUtils.PLACE_CHAR_DE
import org.mtransit.commons.CleanUtils.PLACE_CHAR_DES
import org.mtransit.commons.CleanUtils.PLACE_CHAR_DE_L
import org.mtransit.commons.CleanUtils.PLACE_CHAR_DE_LA
import org.mtransit.commons.CleanUtils.PLACE_CHAR_DU
import org.mtransit.commons.CleanUtils.PLACE_CHAR_L
import org.mtransit.commons.CleanUtils.PLACE_CHAR_LA
import org.mtransit.commons.CleanUtils.PLACE_CHAR_LE
import org.mtransit.commons.CleanUtils.PLACE_CHAR_LES
import org.mtransit.commons.CleanUtils.PLACE_CHAR_QUARTIER
import org.mtransit.commons.CleanUtils.PLACE_CHAR_RTE
import org.mtransit.commons.CleanUtils.PLACE_CHAR_RUE
import org.mtransit.commons.CleanUtils.PLACE_CHAR_TSSE

fun makeALL_ST_REGEX() =
    buildString {
        append("((^\\s*|/\\s*)(")
        append(
            listOf(
                PLACE_CHAR_ARRONDISSEMENT,
                PLACE_CHAR_AV,
                PLACE_CHAR_AVENUE,
                PLACE_CHAR_BOUL,
                PLACE_CHAR_BOULEVARD,
                PLACE_CHAR_CH,
                PLACE_CHAR_CIVIQUE,
                PLACE_CHAR_CROISS,
                PLACE_CHAR_QUARTIER,
                PLACE_CHAR_RTE,
                PLACE_CHAR_RUE,
                PLACE_CHAR_TSSE,
            ).joinToString("|")
        )
        append("))")
    }.toRegex(setOf(RegexOption.IGNORE_CASE))

fun makeALL_ST_REGEX_REPLACEMENT() = "$2"

fun makeALL_CHARS_REGEX() =
    buildString {
        append("((^\\s*|/\\s*)(")
        append(
            listOf(
                PLACE_CHAR_DE_L,
                PLACE_CHAR_DE_LA,
                PLACE_CHAR_D,
                PLACE_CHAR_DE,
                PLACE_CHAR_DES,
                PLACE_CHAR_DU,
                PLACE_CHAR_LA,
                PLACE_CHAR_LE,
                PLACE_CHAR_LES,
                PLACE_CHAR_L,
            ).joinToString("|")
        )
        append("))")
    }.toRegex(setOf(RegexOption.IGNORE_CASE))

fun makeALL_CHARS_REGEX_REPLACEMENT() = "$2"

fun makeALL_FACE_A_REGEX() =
    buildString {
        append("((^|\\s)(")
        append(
            listOf(
                "face à ",
                "face au ",
                "face ",
            ).joinToString("|")
        )
        append("))")
    }.toRegex(setOf(RegexOption.IGNORE_CASE))

fun makeALL_FACE_A_REGEX_REPLACEMENT() = "$2"
