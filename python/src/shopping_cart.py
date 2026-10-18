from src.product import Product


class ShoppingCart:
    def __init__(self, products: dict[str, Product]):
        self.products = products.copy()
        self.items: dict[str, int] = {}

    def add(self, product_id: str, quantity: int):
        if product_id not in self.products:
            raise ValueError("Товар не найден")
        if type(quantity) is not int or quantity <= 0:
            raise ValueError("Количество должно быть целым и положительным")

        new_quantity = self.items.get(product_id, 0) + quantity
        if new_quantity > self.products[product_id].stock:
            raise ValueError("Недостаточно товара на складе")

        self.items[product_id] = new_quantity

    def remove(self, product_id: str, quantity: int | None = None):
        if product_id not in self.items:
            raise ValueError("Товара нет в корзине")
        if quantity is None:
            del self.items[product_id]
            return
        if type(quantity) is not int or quantity <= 0:
            raise ValueError("Количество должно быть целым и положительным")

        if quantity > self.items[product_id]:
            raise ValueError("В корзине меньше товара")

        if quantity == self.items[product_id]:
            del self.items[product_id]
        else:
            self.items[product_id] -= quantity

    def total(self):
        return sum(
            self.products[product_id].price * quantity
            for product_id, quantity in self.items.items()
        )