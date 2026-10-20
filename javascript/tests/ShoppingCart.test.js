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

  test('удаляем товар, которого нет в корзине', () => {
    expect(() => cart.remove('p1')).toThrow('Товара нет в корзине')
  });

  test('удаляем товар с неправильным количеством', () => {
    cart.add('p1', 1);
    expect(() => cart.remove('p1', -1)).toThrow('Количество должно быть целым и положительным')
  });

  test('удаляем больше, чем есть', () => {
    cart.add('p1', 1);
    expect(() => cart.remove('p1', 2)).toThrow('В корзине меньше товара')
  });

  test('удаляем ровно сколько есть, товар становится undefined', () => {
    cart.add('p1', 1);
    cart.remove('p1', 1);
    expect(cart.getItems().get('p1')).toBe(undefined);
  });
});