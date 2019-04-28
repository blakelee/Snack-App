package net.blakelee.model

class DummySnackModel : SnackModel {

    override fun getSnacks(): List<Snack> = listOf(
        Snack.Vegetable("French fries"),
        Snack.Vegetable("Veggieburger"),
        Snack.Vegetable("Carrots"),
        Snack.Vegetable("Apple"),
        Snack.Vegetable("Banana"),
        Snack.Vegetable("Milkshake"),
        Snack.NonVegetable("Cheeseburger"),
        Snack.NonVegetable("Hamburger"),
        Snack.NonVegetable("Hot dog")
    ).sortedBy { it.value.toLowerCase() }

    override fun placeOrder(snacks: List<Snack>): Any { return Any() }
}