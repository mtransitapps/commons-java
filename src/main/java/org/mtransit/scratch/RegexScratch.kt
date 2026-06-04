package org.mtransit.scratch

import java.util.regex.Pattern

@Suppress("JoinDeclarationAndAssignment", "CanBeVal", "UNUSED_VALUE", "KotlinRedundantDiagnosticSuppress")
internal object RegexScratch {
    @JvmStatic
    fun main(args: Array<String>) {
        var regex: String
        regex = ""
        // regex = "(^|\\s*)line (\\w+)"
        regex = "((^|(?<=<>)\\s+)([^~]+)\\s+~\\s+([^~<]+)(\\s+(?=<>)|$))"
        // regex = "((^|<>\\s+)([^~]+)\\s+~\\s+([^~<]+)(\\s+<>|$))"
        println("regex: '$regex'.")
        val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)

        var string: String
        string = ""
        // string = "line ABC (b+)"
        // En-en ~ Fr-fr <>
        string = "Test <> Nepean South ~ Nepean Sud"
        // string = "Nepean South ~ Nepean Sud <> Test"
        // string = "Nepean South ~ Nepean Sud <> Nepean North ~ Nepean Nord"
        // string = "Shuttle-Express Downtown ~ Navette-Express Ctr-Ville <> Shuttle-Express Blair ~ Navette-Express Blair"
        println("string: '$string'.")
        println("=======")

        val matcher = pattern.matcher(string)
        while (matcher.find()) {
            println("-------")
            println("- Found group: '" + matcher.group() + "' (count: '" + matcher.groupCount() + "'):")
            for (g in 0..matcher.groupCount()) {
                println("  - group[" + g + "]: '" + matcher.group(g) + "'.")
            }
            println("-------")
        }

        println("=======")
        var replaceAll: String
        replaceAll = ""
        // replaceAll = "$2"
        replaceAll = "$2$3$5"
        // replaceAll = "$3$5"
        // replaceAll == "Shuttle-Express Downtown <> Shuttle-Express Blair"
        println("replaceAll: '$replaceAll'.")
        println("-> '" + pattern.matcher(string).replaceAll(replaceAll) + "'.")
    }
}