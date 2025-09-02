package com.example.prew.dto

data class Node<T>(val value: T, val children: MutableList<Node<T>> = mutableListOf())