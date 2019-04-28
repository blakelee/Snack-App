package net.blakelee.snackapp

import net.blakelee.model.DummySnackModel
import net.blakelee.model.Snack
import org.junit.Test

class SnackViewModelTest {

    private val model = DummySnackModel()
    private val viewModel = SnackViewModel(model)

    @Test
    fun `Enforce snack added when toggling`() {
        val snack = model.getSnacks().first()
        viewModel.toggleSnack(snack, true)

        assert(viewModel.snacksSelected.size == 1)
    }

    @Test
    fun `Enforce snack removed actually removes something`() {
        val snacks = model.getSnacks()
        val snack = snacks.first()

        // Add all the snacks to the selected snacks
        snacks.forEach {
            viewModel.toggleSnack(it, true)
        }

        viewModel.toggleSnack(snack, false)

        assert(!viewModel.snacksSelected.contains(snack))
    }

    @Test
    fun `Make sure all veggies are showing up`() {
        val veggies = model.getSnacks().filter { it is Snack.Vegetable }

        viewModel.toggleVeggieFilter(true)
        assert(viewModel.filteredSnacks.containsAll(veggies))
    }

    @Test
    fun `Make sure no veggies are showing up`() {
        viewModel.toggleVeggieFilter(false)

        var result = true
        viewModel.filteredSnacks.forEach {
            if (it is Snack.Vegetable) {
                result = false
            }
        }
        assert(result)
    }

    @Test
    fun `Make sure all non-veggies are showing up`() {
        val nonveggies = model.getSnacks().filter { it is Snack.NonVegetable }

        viewModel.toggleNonVeggieFilter(true)
        assert(viewModel.filteredSnacks.containsAll(nonveggies))
    }

    @Test
    fun `Make sure no non-veggies are showing up`() {
        viewModel.toggleNonVeggieFilter(false)

        var result = true
        viewModel.filteredSnacks.forEach {
            if (it is Snack.NonVegetable) {
                result = false
            }
        }
        assert(result)
    }

    @Test
    fun `Make sure newly added vegetable is added`() {
        viewModel.addSnack("Blake was here", true, true, true)
        assert(viewModel.filteredSnacks.contains(Snack.Vegetable("Blake was here")))
    }

    @Test
    fun `Make sure newly added nonvegetable is added`() {
        viewModel.addSnack("Blake was here", false, true, true)
        assert(viewModel.filteredSnacks.contains(Snack.NonVegetable("Blake was here")))
    }

    @Test
    fun `Test our stub place order returns something at all`() {
        val result = viewModel.placeOrder()
        assert(result is Any)
    }

    @Test
    fun `Make sure resetting the snacks actually clears everything`() {
        val snacks = model.getSnacks()
        snacks.forEach {
            viewModel.toggleSnack(it, true)
        }

        viewModel.resetSnacksSelected()

        assert(viewModel.snacksSelected.isEmpty())
    }
}