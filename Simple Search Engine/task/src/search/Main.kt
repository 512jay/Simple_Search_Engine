package search

import java.io.File
import java.util.Scanner

fun main(args: Array<String>) {
    // println("Argument passed ${args[0]} and ${args[1]}")
    val fileName = args[1]
    val list = mutableListOf<String>()
    val invertedIndex = mutableMapOf<String, MutableSet<Int>>()
    var index = 0

    File(fileName).forEachLine {
        list.add(index, it)
        val str = it.split(' ')
        for (e in str)
            {
                if (invertedIndex[e.toLowerCase()].isNullOrEmpty()) {
                    val set = mutableSetOf(index)
                    invertedIndex[e.toLowerCase()] = set
                } else {
                    invertedIndex[e.toLowerCase()]?.add(index)
                }
            }
        index++
        }


    val scanner = Scanner(System.`in`)

    while (true) {
        println("=== Menu ===\n1. Find a person\n2. Print all people\n0. Exit")
        val menuItem = scanner.nextLine().toInt()
        println()

        when (menuItem) {
            0 -> {println("Bye!"); return}
            1 -> findAPerson(invertedIndex, list)
            2 -> printAllPeople(list)
            else -> println("Incorrect option! Try again.")
        }
    }
}

fun anySearch(inverted: MutableMap<String, MutableSet<Int>>, list: List<String>){
    val scanner = Scanner(System.`in`)
    println("Enter a name or email to search all suitable people.")
    val queryList = scanner.nextLine().split(" ")
    var matches = 0

    for (query in queryList) {
        matches += inverted[query.toLowerCase()]?.size ?: 0
    }

    if (matches > 0) {
        println("$matches persons found:")
    } else {
        println("No matching people found.")
    }

    for (query in queryList) {
        if (inverted[query.toLowerCase()]?.isNotEmpty() == true) {
            inverted[query.toLowerCase()]?.iterator()?.forEach {
                println(list[it])
            }
        }
    }
}


fun allSearch(inverted: MutableMap<String, MutableSet<Int>>, list: List<String>){
    val scanner = Scanner(System.`in`)
    println("Enter a name or email to search all suitable people.")
    val queryList = scanner.nextLine().split(" ")
    val allMatch = mutableListOf<Set<Int>>()

    for (query in queryList) {
        if (!inverted[query.toLowerCase()].isNullOrEmpty()) {
            inverted[query.toLowerCase()]?.let { allMatch.add(it) }
        }
    }

    if (allMatch.isEmpty()){
        println("No matching people found.")
    } else if (allMatch.size == 1){
        println("${allMatch.size} persons found:")
        for (line in allMatch[0]) {
            println(list[line])
        }
    } else {
        var theSet = allMatch[0]
        for( set in allMatch) {
            theSet  = theSet.intersect(set)
        }

        if (theSet.isEmpty()) {
            println("No matching people found")
        }else {
            println("${theSet.size} persons found:")
            for (line in theSet) {
                println(list[line])
            }
        }
    }
}

fun noneSearch(inverted: MutableMap<String, MutableSet<Int>>, list: List<String>){
    val scanner = Scanner(System.`in`)
    println("Enter a name or email to search all suitable people.")
    val queryList = scanner.nextLine().split(" ")
    val everyItemSet = mutableSetOf<Int>()
    for (i in list.indices) everyItemSet.add(i)

    val setForGivenItems = mutableSetOf<Int>()

    for (query in queryList) {
        if (inverted[query.toLowerCase()]?.isNotEmpty() == true) {
            inverted[query.toLowerCase()]?.iterator()?.forEach {
                setForGivenItems.add(it)
            }
        }
    }

    val noneSet = everyItemSet.subtract(setForGivenItems)

    if (noneSet.isEmpty()) {
        println("No matching people found")
    } else {
        println("${noneSet.size} persons found: ")
        for (i in noneSet) {
            println(list[i])
        }
    }

}

fun findAPerson(inverted: MutableMap<String, MutableSet<Int>>, list: List<String>) {
    val scanner = Scanner(System.`in`)
    println("Select a matching strategy: ALL, ANY, NONE")

    when(scanner.nextLine()) {
        "ALL" -> allSearch(inverted, list)
        "ANY" -> anySearch(inverted, list)
        "NONE" -> noneSearch(inverted, list)
        else -> return
    }
    println()
}

fun printAllPeople(list: MutableList<String>) {
    println("=== List of people ===")
    for (item in list) {
        println(item)
    }
    println()
}

