package com.example.speechnancial.tools.algoritma.feynman

fun gettingEveryCharIndexInAList() {
    val pattern = listOf("rupiah", "rp")

    val sortedPattern = pattern.sortedBy { it.length }

    println(sortedPattern)

    val longestWord = sortedPattern.last()
    val longestIndex = longestWord.length

    for (i in 0 until longestIndex) {
        for (word in sortedPattern) {
            if (i < word.length) {
                print("${word[i]}")
            } else {
                print(" ") // Print space if the word is shorter
            }
            print(" - ")
        }
        println()
    }
}

fun main() {
    gettingEveryCharIndexInAList()
}