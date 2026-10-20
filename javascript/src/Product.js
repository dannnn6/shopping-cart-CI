export class Product {
  constructor(id, name, price, stock) {
    this.id = id;
    this.name = name;
    this.price = price; // в копейках
    this.stock = stock;

    Object.freeze(this);
  }
}