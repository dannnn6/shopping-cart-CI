export class ShoppingCart {
  #products;
  #items = new Map();

  constructor(products) {
    this.#products = new Map(products);
  }

  getItems() {
    return new Map(this.#items);
  }

  add(productId, quantity) {
    if (!this.#products.has(productId)) {
      throw new Error('Товар не найден');
    }

    if (!Number.isSafeInteger(quantity) || quantity <= 0) {
      throw new Error('Количество должно быть целым и положительным');
    }

    const newQuantity = (this.#items.get(productId) ?? 0) + quantity;

    if (newQuantity > this.#products.get(productId).stock) {
      throw new Error('Недостаточно товара на складе');
    }

    this.#items.set(productId, newQuantity);
  }

  remove(productId, quantity = null) {
    if (!this.#items.has(productId)) {
      throw new Error('Товара нет в корзине');
    }

    if (quantity === null) {
      this.#items.delete(productId);
      return;
    }

    if (!Number.isSafeInteger(quantity) || quantity <= 0) {
      throw new Error('Количество должно быть целым и положительным');
    }

    const current = this.#items.get(productId);

    if (quantity > current) {
      throw new Error('В корзине меньше товара');
    }

    if (quantity === current) {
      this.#items.delete(productId);
    } else {
      this.#items.set(productId, current - quantity);
    }
  }

  total(promoCode = null) {
    let amount = 0;

    for (const [productId, quantity] of this.#items) {
      amount += this.#products.get(productId).price * quantity;
    }

    if (promoCode === null) {
      return amount;
    }

    if (promoCode !== 'SALE10') {
      throw new Error('Неизвестный промокод');
    }

    return Math.floor((amount * 90 + 50) / 100);
  }
}