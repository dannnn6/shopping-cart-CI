from src.product import Product


def test_product_fields():
    product = Product("p1", "Тетрадь", 10000, 5)

    assert product.id == "p1"
    assert product.name == "Тетрадь"
    assert product.price == 10000
    assert product.stock == 5