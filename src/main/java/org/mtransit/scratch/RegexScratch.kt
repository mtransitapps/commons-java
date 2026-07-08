package org.mtransit.scratch

import org.intellij.lang.annotations.Language

@Suppress("JoinDeclarationAndAssignment", "CanBeVal", "UNUSED_VALUE", "KotlinRedundantDiagnosticSuppress", "UNNECESSARY_SAFE_CALL", "AssignedValueIsNeverRead")
internal object RegexScratch {
    @JvmStatic
    fun main(args: Array<String>) {
        @Language("RegExp")
        var regex: String
        regex = ""
        println("regex: '$regex'.")
        val pattern = Regex(regex, RegexOption.IGNORE_CASE)

        var string: String
        string = ""
        println("string: '$string'.")
        println("=======")

        pattern.findAll(string).forEach { matchResult ->
            println("-------")
            println("- Found group: '${matchResult.value}' (count: '${matchResult.groups.size}'):")
            matchResult.groupValues.forEachIndexed { g, groupValue ->
                println("  - group[$g]: '$groupValue'.")
            }
            println("-------")
        }

        println("=======")
        var replaceAll: String
        replaceAll = ""
        println("replaceAll: '$replaceAll'.")
        val result = pattern.replace(string, replaceAll)
        var expectedResult: String?
        expectedResult = null
        println("-> '$result'.")
        expectedResult?.let {
            println("-> Success? ${(expectedResult == result).toString().uppercase()}")
        }
        println("=======")
    }
}