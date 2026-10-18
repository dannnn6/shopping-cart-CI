import pytest

from src.product import Product
from src.shopping_cart import ShoppingCart


@pytest.fixture
def cart():
    products = {
        "p1": Product("p1", "Тетрадь", 10000, 5),
        "p2": Product("p2", "Ручка", 5000, 10),
        "p3": Product("p3", "Ластик", 1000, 0),
    }
    return ShoppingCart(products)


def test_empty_cart(cart):
    assert cart.items == {}
    assert cart.total() == 0


def test_add_new_product(cart):
    cart.add("p1", 2)

    assert cart.items == {"p1": 2}
    assert cart.products["p1"].stock == 5


def test_add_existing_product(cart):
    cart.add("p1", 2)
    cart.add("p1", 3)

    assert cart.items == {"p1": 5}


def test_add_unknown_product(cart):
    with pytest.raises(ValueError, match="Товар не найден"):
        cart.add("unknown", 1)

    assert cart.items == {}


@pytest.mark.parametrize("quantity", [0, -1, 1.5, True])
def test_add_invalid_quantity(cart, quantity):
    with pytest.raises(ValueError, match="целым и положительным"):
        cart.add("p1", quantity)

    assert cart.items == {}


@pytest.mark.parametrize("product_id, quantity", [("p1", 6), ("p3", 1)])
def test_add_over_stock(cart, product_id, quantity):
    with pytest.raises(ValueError, match="Недостаточно"):
        cart.add(product_id, quantity)

    assert cart.items == {}


def test_add_over_stock_after_previous_add(cart):
    cart.add("p1", 3)

    with pytest.raises(ValueError, match="Недостаточно"):
        cart.add("p1", 3)

    assert cart.items == {"p1": 3}


def test_remove_part(cart):
    cart.add("p1", 3)
    cart.remove("p1", 1)

    assert cart.items == {"p1": 2}


def test_remove_all_without_quantity(cart):
    cart.add("p1", 3)
    cart.add("p2", 1)
    cart.remove("p1")

    assert cart.items == {"p2": 1}


def test_remove_exact_quantity(cart):
    cart.add("p1", 3)
    cart.remove("p1", 3)

    assert cart.items == {}


def test_remove_missing_product(cart):
    with pytest.raises(ValueError, match="Товара нет в корзине"):
        cart.remove("p1")

    assert cart.items == {}


@pytest.mark.parametrize("quantity", [0, -1, 1.5, True])
def test_remove_invalid_quantity(cart, quantity):
    cart.add("p1", 3)

    with pytest.raises(ValueError, match="целым и положительным"):
        cart.remove("p1", quantity)

    assert cart.items == {"p1": 3}


def test_remove_too_many(cart):
    cart.add("p1", 2)

    with pytest.raises(ValueError, match="В корзине меньше товара"):
        cart.remove("p1", 3)

    assert cart.items == {"p1": 2}


def test_total_several_products(cart):
    cart.add("p1", 2)
    cart.add("p2", 3)

    assert cart.total() == 35000


def test_total_after_remove(cart):
    cart.add("p1", 2)
    cart.add("p2", 3)
    cart.remove("p1", 1)

    assert cart.total() == 25000

def test_total_with_promo(cart):
    cart.add("p1", 2)
    cart.add("p2", 3)

    assert cart.total("VYATSU10") == 31500
    assert cart.total() == 35000
    assert cart.items == {"p1": 2, "p2": 3}

def test_promo_for_empty_cart(cart):
    assert cart.total("VYATSU10") == 0

@pytest.mark.parametrize("code", ["WRONG", "", "skidka10"])
def test_unknown_promo(cart, code):
    with pytest.raises(ValueError, match="Неизвестный промокод"):
        cart.total(code)

@pytest.mark.parametrize("price, expected", [(15, 14), (14, 13)])
def test_promo_rounding(price, expected):
    cart = ShoppingCart({"p1": Product("p1", "Товар", price, 1)})
    cart.add("p1", 1)

    assert cart.total("VYATSU10") == expected