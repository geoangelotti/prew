package com.example.prew.dto

sealed class Child<T>
data class NodeChild<T> (val node: Node<T>) : Child<T>()
data class CycleChild<T>(val cycle: T) : Child<T>()