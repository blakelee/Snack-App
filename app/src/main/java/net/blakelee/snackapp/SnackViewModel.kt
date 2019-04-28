package net.blakelee.snackapp

import androidx.lifecycle.ViewModel
import net.blakelee.model.Snack
import net.blakelee.model.SnackModel

class SnackViewModel(private val model: SnackModel) : ViewModel() {

    private val snacks = model.getSnacks().toMutableList()
    val snacksSelected = mutableSetOf<Snack>()
    val filteredSnacks = snacks.toMutableList()

    fun isSnackSelected(snack: Snack) = snacksSelected.contains(snack)

    fun toggleSnack(snack: Snack, checked: Boolean) {
        if (checked) {
            snacksSelected.add(snack)
        } else {
            snacksSelected.remove(snack)
        }
    }

    fun toggleVeggieFilter(checked: Boolean) {
        if (!checked) {
            filteredSnacks.removeAll { it is Snack.Vegetable }
        } else {
            filteredSnacks.addAll(snacks.filter { it is Snack.Vegetable })
            filteredSnacks.sortBy { it.value.toLowerCase() }
        }
    }

    fun toggleNonVeggieFilter(checked: Boolean) {
        if (!checked) {
            filteredSnacks.removeAll { it is Snack.NonVegetable }
        } else {
            filteredSnacks.addAll(snacks.filter { it is Snack.NonVegetable })
            filteredSnacks.sortBy { it.value.toLowerCase() }
        }
    }

    fun addSnack(text: String, isVeggie: Boolean, vChecked: Boolean, nvChecked: Boolean) {
        val snack = if (isVeggie) {
            Snack.Vegetable(text)
        } else {
            Snack.NonVegetable(text)
        }

        snacks.add(snack)

        if (vChecked && snack is Snack.Vegetable || nvChecked && snack is Snack.NonVegetable) {
            filteredSnacks.add(snack)
            filteredSnacks.sortBy { it.value.toLowerCase() }
        }

    }

    fun placeOrder() = model.placeOrder(snacksSelected.toList())

    fun resetSnacksSelected() {
        snacksSelected.clear()
    }
}