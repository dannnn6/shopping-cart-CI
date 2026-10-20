import { beforeEach, describe, expect, test } from 'vitest';
import { Product } from '../src/Product.js';
import { ShoppingCart } from '../src/ShoppingCart.js';

describe('ShoppingCart', () => {
  let cart;

  beforeEach(() => {
    const products = new Map([
      ['p1', new Product('p1', 'Тетрадь', 10000, 5)],
      ['p2', new Product('p2', 'Ручка', 5000, 10)],
      ['p3', new Product('p3', 'Ластик', 1000, 0)],
    ]);

    cart = new ShoppingCart(products);
  });

  test('новая корзина пустая', () => {
    expect(cart.getItems().size).toBe(0);
    expect(cart.total()).toBe(0);
  });

  test('добавили 1 товар', () => {
    cart.add('p1', 5);
    expect(cart.total()).toBe(50000);
    expect(cart.getItems().get('p1')).toBe(5);
  });

  test('добавили несуществующий товар', () => {
    expect(() => cart.add('p999'), 1).toThrow('Товар не найден')
  });

  test('добавили неправильное число товара (0)', () => {
    expect(() => cart.add('p1', 0)).toThrow('Количество должно быть целым и положительным')
  });

  test('добавили неправильное число товара (отрицательное)', () => {
    expect(() => cart.add('p1', -1)).toThrow('Количество должно быть целым и положительным')
  });

  test('добавили неправильное число товара (больше, чем есть)', () => {
    expect(() => cart.add('p1', 555)).toThrow('Недостаточно товара на складе')
  });


});