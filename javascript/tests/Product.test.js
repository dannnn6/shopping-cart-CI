import { expect, test } from 'vitest';
import { Product } from '../src/Product.js';

test('товар хранит переданные значения', () => {
  const product = new Product('tovar1', 'Тетрадь', 10000, 5);

  expect(product.id).toBe('tovar1');
  expect(product.name).toBe('Тетрадь');
  expect(product.price).toBe(10000);
  expect(product.stock).toBe(5);

});