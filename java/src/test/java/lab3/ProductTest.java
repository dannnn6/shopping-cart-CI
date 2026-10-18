package lab3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductTest {
    @Test
    void productFields() {
        Product product = new Product("p1", "Тетрадь", 10000, 5);

        assertEquals("p1", product.getId());
        assertEquals("Тетрадь", product.getName());
        assertEquals(10000L, product.getPrice());
        assertEquals(5, product.getStock());
    }
}