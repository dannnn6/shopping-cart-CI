package lab3;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {
    private ShoppingCart cart;
    private Product notebook;

    @BeforeEach
    void setUp() {
        notebook = new Product("p1", "Тетрадь", 10000, 5);

        cart = new ShoppingCart(Map.of(
                "p1", notebook,
                "p2", new Product("p2", "Ручка", 5000, 10),
                "p3", new Product("p3", "Ластик", 1000, 0)
        ));
    }

    @Test
    void emptyCart() {
        assertEquals(Map.of(), cart.getItems());
        assertEquals(0L, cart.total());
    }

    @Test
    void addNewProduct() {
        cart.add("p1", 2);

        assertEquals(Map.of("p1", 2), cart.getItems());
        assertEquals(5, notebook.getStock());
    }

    @Test
    void addExistingProduct() {
        cart.add("p1", 2);
        cart.add("p1", 3);

        assertEquals(Map.of("p1", 5), cart.getItems());
    }

    @Test
    void addUnknownProduct() {
        assertThrows(
                IllegalArgumentException.class,
                () -> cart.add("unknown", 1)
        );

        assertEquals(Map.of(), cart.getItems());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void addInvalidQuantity(int quantity) {
        assertThrows(
                IllegalArgumentException.class,
                () -> cart.add("p1", quantity)
        );

        assertEquals(Map.of(), cart.getItems());
    }

    @ParameterizedTest
    @CsvSource({"p1, 6", "p3, 1"})
    void addOverStock(String id, int quantity) {
        assertThrows(
                IllegalArgumentException.class,
                () -> cart.add(id, quantity)
        );

        assertEquals(Map.of(), cart.getItems());
    }

    @Test
    void addOverStockAfterPreviousAdd() {
        cart.add("p1", 3);

        assertThrows(
                IllegalArgumentException.class,
                () -> cart.add("p1", 3)
        );

        assertEquals(Map.of("p1", 3), cart.getItems());
    }

    @Test
    void removePart() {
        cart.add("p1", 3);
        cart.remove("p1", 1);

        assertEquals(Map.of("p1", 2), cart.getItems());
    }

    @Test
    void removeAll() {
        cart.add("p1", 3);
        cart.add("p2", 1);
        cart.remove("p1");

        assertEquals(Map.of("p2", 1), cart.getItems());
    }

    @Test
    void removeExactQuantity() {
        cart.add("p1", 3);
        cart.remove("p1", 3);

        assertEquals(Map.of(), cart.getItems());
    }

    @Test
    void removeMissingProduct() {
        assertThrows(
                IllegalArgumentException.class,
                () -> cart.remove("p1")
        );

        assertEquals(Map.of(), cart.getItems());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void removeInvalidQuantity(int quantity) {
        cart.add("p1", 3);

        assertThrows(
                IllegalArgumentException.class,
                () -> cart.remove("p1", quantity)
        );

        assertEquals(Map.of("p1", 3), cart.getItems());
    }

    @Test
    void removeTooMany() {
        cart.add("p1", 2);

        assertThrows(
                IllegalArgumentException.class,
                () -> cart.remove("p1", 3)
        );

        assertEquals(Map.of("p1", 2), cart.getItems());
    }

    @Test
    void totalSeveralProducts() {
        cart.add("p1", 2);
        cart.add("p2", 3);

        assertEquals(35000L, cart.total());
    }

    @Test
    void totalAfterRemove() {
        cart.add("p1", 2);
        cart.add("p2", 3);
        cart.remove("p1", 1);

        assertEquals(25000L, cart.total());
    }

    @Test
    void totalWithPromo() {
        cart.add("p1", 2);
        cart.add("p2", 3);

        assertEquals(31500L, cart.total("VYATSU10"));
        assertEquals(35000L, cart.total());
        assertEquals(Map.of("p1", 2, "p2", 3), cart.getItems());
    }

    @Test
    void promoForEmptyCart() {
        assertEquals(0L, cart.total("VYATSU10"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"WRONG", "", "sale10"})
    void unknownPromo(String code) {
        assertThrows(
                IllegalArgumentException.class,
                () -> cart.total(code)
        );
    }

    @ParameterizedTest
    @CsvSource({"15, 14", "14, 13"})
    void promoRounding(long price, long expected) {
        cart = new ShoppingCart(Map.of(
                "p1", new Product("p1", "Товар", price, 1)
        ));

        cart.add("p1", 1);

        assertEquals(expected, cart.total("VYATSU10"));
    }
}