package lab3;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private final Map<String, Product> products;
    private final Map<String, Integer> items = new HashMap<>();

    public ShoppingCart(Map<String, Product> products) {
        this.products = Map.copyOf(products);
    }

    public Map<String, Integer> getItems() {
        return Map.copyOf(items);
    }

    public void add(String productId, int quantity) {
        if (!products.containsKey(productId)) {
            throw new IllegalArgumentException("Товар не найден");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Количество должно быть положительным"
            );
        }

        long newQuantity =
                (long) items.getOrDefault(productId, 0) + quantity;

        if (newQuantity > products.get(productId).getStock()) {
            throw new IllegalArgumentException(
                    "Недостаточно товара на складе"
            );
        }

        items.put(productId, (int) newQuantity);
    }

    public void remove(String productId) {
        remove(productId, null);
    }

    public void remove(String productId, Integer quantity) {
        if (!items.containsKey(productId)) {
            throw new IllegalArgumentException("Товара нет в корзине");
        }
        if (quantity == null) {
            items.remove(productId);
            return;
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Количество должно быть положительным"
            );
        }

        int current = items.get(productId);

        if (quantity > current) {
            throw new IllegalArgumentException(
                    "В корзине меньше товара"
            );
        }

        if (quantity == current) {
            items.remove(productId);
        } else {
            items.put(productId, current - quantity);
        }
    }

    public long total() {
        return total(null);
    }

    public long total(String promoCode) {
        long amount = 0;

        for (Map.Entry<String, Integer> item : items.entrySet()) {
            amount += products.get(item.getKey()).getPrice()
                    * item.getValue();
        }

        if (promoCode == null) {
            return amount;
        }
        if (!"VYATSU10".equals(promoCode)) {
            throw new IllegalArgumentException(
                    "Неизвестный промокод"
            );
        }

        return (amount * 90 + 50) / 100;
    }
}