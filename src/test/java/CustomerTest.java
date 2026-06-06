import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("Anton");
    }


    @Test
    public void addProductToShoppingCart() {
        Product product = new Product(8, "Zahnpasta", Category.SONSTIGES, 0.89, -0.2);
        customer.addProductToShoppingCart(product);
        assertEquals(List.of(product), customer.getShoppingCart());
    }

    @Test
    public void addProductTwiceToShoppingCart() {
        Product product = new Product(8, "Zahnpasta", Category.SONSTIGES, 0.89, -0.2);
        customer.addProductToShoppingCart(product);
        customer.addProductToShoppingCart(product);
        assertEquals(2, customer.getShoppingCart().size());
    }

    @Test
    public void deleteExistingProductFromShoppingCart() {
        Product product = new Product(8, "Zahnpasta", Category.SONSTIGES, 0.89, -0.2);
        customer.addProductToShoppingCart(product);
        customer.deleteProductFromShoppingCart(product);
        assertTrue(customer.getShoppingCart().isEmpty());
    }

    @Test
    public void deleteNotExistingProductFromShoppingCart() {
        Product product = new Product(8, "Zahnpasta", Category.SONSTIGES, 0.89, -0.2);
        customer.deleteProductFromShoppingCart(product);
        assertTrue(customer.getShoppingCart().isEmpty());
    }

    @Test
    public void deleteExistingProductOnceFromShoppingCart() {
        Product product = new Product(8, "Zahnpasta", Category.SONSTIGES, 0.89, -0.2);
        customer.addProductToShoppingCart(product);
        customer.addProductToShoppingCart(product);
        customer.deleteProductFromShoppingCart(product);
        assertEquals(1, customer.getShoppingCart().size());
    }


    @Test
    public void buyShoppingCart() {
        Product product1 = new Product(8, "Zahnpasta", Category.SONSTIGES, 0.89, -0.2);
        Product product2 = new Product(110, "Telefon", Category.SONSTIGES, 100.29, -0.3);
        customer.addProductToShoppingCart(product1);
        customer.addProductToShoppingCart(product2);

        customer.buyShoppingCart();

        assertTrue(customer.getShoppingCart().isEmpty());
        assertEquals(List.of(product1, product2), customer.getShoppingHistory());
    }

    @Test
    public void buyEmptyShoppingCart() {
        customer.buyShoppingCart();
        assertTrue(customer.getShoppingCart().isEmpty());
        assertTrue(customer.getShoppingHistory().isEmpty());
    }

    @Test
    public void buyShoppingCartTwice() {
        Product product1 = new Product(1, "Apfel", Category.OBST, 2.39, 0.08);
        Product product2 = new Product(6, "Banane", Category.OBST, 1.59, 0.09);

        customer.addProductToShoppingCart(product1);
        customer.buyShoppingCart();
        customer.addProductToShoppingCart(product2);
        customer.buyShoppingCart();

        assertEquals(2, customer.getShoppingHistory().size());
        assertTrue(customer.getShoppingCart().isEmpty());
    }

    @Test
    public void getShoppingCartIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () ->
                customer.getShoppingCart().add(new Product(1, "Apfel", Category.OBST, 2.39, 0.08))
        );
    }

    @Test
    public void getShoppingHistoryIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () ->
                customer.getShoppingHistory().add(new Product(1, "Apfel", Category.OBST, 2.39, 0.08))
        );
    }
}
