package com.epicdevler.ami.minote

fun main(){

//    var, val

//    var = mutable (Modified overtime)
//    val = immutable (Not modified overtime)

    var name = "Jumbo"
    println(name)
    name = "Miracle"
    println(name)

    /*
    * DataTypes
    * String = "Character"
    * Char = 'A',
    * Int = 1124213122342332322343
    * Long = 10394239493
    * Boolean = true/false
    * Short = 2234234
    *
    * Array
    * List
    * MutableList
    * Map
    * HashMap
    * Set
    *
    * */

    var lMax: Long = Long.MAX_VALUE
    var lMin: Long = Long.MIN_VALUE

    println("Max Long Value: $lMax\n\nMin Long Value: $lMin")

    println("\n\nMax Integer Value: ${Int.MAX_VALUE}\n\nMin Integer Value: ${Int.MIN_VALUE}")

    println("\n\nMax Short Value: ${Short.MAX_VALUE}\n\nMin Short Value: {Short.MIN_VALUE}")

    var fruits: MutableList<String> = mutableListOf("Apple", "Banana", "Orange")
    var colors: ArrayList<String> = arrayListOf("Red", "Yellow", "Orange")
    var user: Map<Int, String> = mapOf(
        1 to "Miracle",
        2 to "Philip",
        3 to "Blessed"
    )

    fruits.add("Pawpaw")
    colors.add("Magenta")
    fruits.addAll(fruits)
    colors.add("Magenta")

}

