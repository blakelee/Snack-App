package net.blakelee.model

sealed class Snack {
    abstract val value: String
    data class Vegetable(override val value: String) : Snack()
    data class NonVegetable(override val value: String) : Snack()
}

interface SnackModel {
    fun getSnacks(): List<Snack>
    fun placeOrder(snacks: List<Snack>): Any // Would likely be a Completable if using RxJava
}