package com.example.prew.dto

import java.net.URI

sealed class Child<T>
data class NodeChild<T> (val node: Node<T>) : Child<T>()
data class CycleChild<T>(val cycle: T) : Child<T>()
data class PagedChild<T>(val value: T, val location: URI) : Child<T>()