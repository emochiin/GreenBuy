import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
    }

    List<Product> testCatalog = new ArrayList<>(List.of(
            new Product(8, "Zahnpasta", Category.SONSTIGES, 0.89, -0.2),
            new Product(1, "Apfel", Category.OBST, 2.39, 0.08),
            new Product(6, "Banane", Category.OBST, 1.59, 0.09),
            new Product(7, "Paprika", Category.GEMUESE, 1.79, -0.03),
            new Product(3, "Milch", Category.MILCHPRODUKTE, 1.19, 0.1)
    ));

    @Test
    public void filterProductsByPrice() {
        List<Product> expected = List.of(
                new Product(8, "Zahnpasta", Category.SONSTIGES, 0.89, -0.2),
                new Product(3, "Milch", Category.MILCHPRODUKTE, 1.19, 0.1)
        );
        assertEquals(expected, productService.filterProductsByPrice(testCatalog, 1.50));
    }

    @Test
    public void filterProductsByPriceWithEmptyList() {
        assertTrue(productService.filterProductsByPrice(List.of(), 5.00).isEmpty());
    }

    @Test
    public void filterProductsByNonContainedPrice() {
        assertTrue(productService.filterProductsByPrice(testCatalog, 0.50).isEmpty());
    }

    @Test
    public void filterProductsByCategory() {
        List<Product> expected = List.of(
                new Product(1, "Apfel", Category.OBST, 2.39, 0.08),
                new Product(6, "Banane", Category.OBST, 1.59, 0.09)
        );
        assertEquals(expected, productService.filterProductsByCategory(testCatalog, Category.OBST));
    }

    @Test
    public void filterProductsByCategoryWithEmptyList() {
        assertTrue(productService.filterProductsByCategory(List.of(), Category.OBST).isEmpty());
    }

    @Test
    public void filterProductsByNonPresentCategory() {
        assertTrue(productService.filterProductsByCategory(testCatalog, Category.FLEISCH).isEmpty());
    }

    @Test
    public void sortProductsByName() {
        List<Product> expected = List.of(
                new Product(1, "Apfel", Category.OBST, 2.39, 0.08),
                new Product(6, "Banane", Category.OBST, 1.59, 0.09),
                new Product(3, "Milch", Category.MILCHPRODUKTE, 1.19, 0.1),
                new Product(7, "Paprika", Category.GEMUESE, 1.79, -0.03),
                new Product(8, "Zahnpasta", Category.SONSTIGES, 0.89, -0.2)
        );
        assertEquals(expected, productService.sortProductsByName(testCatalog));
    }

    @Test
    public void sortProductsByNameEmptyList() {
        assertTrue(productService.sortProductsByName(List.of()).isEmpty());
    }

    @Test
    public void calculateSavedCo2Value() {
        Customer customer = new Customer("Testkunde");
        customer.addProductToShoppingCart(new Product(1, "Apfel", Category.OBST, 2.39, 0.08));
        customer.addProductToShoppingCart(new Product(8, "Zahnpasta", Category.SONSTIGES, 0.89, -0.2));
        customer.buyShoppingCart();

        assertEquals(-0.12, productService.calculateSavedCo2Value(customer), 0.001);
    }

    @Test
    public void calculateSavedCo2ValueEmptyHistory() {
        Customer customer = new Customer("Leerkunde");
        assertEquals(0.0, productService.calculateSavedCo2Value(customer), 0.001);
    }

    @Test
    public void calculateSavedCo2ValueAllNegativeCo2() {
        Customer customer = new Customer("GruenKunde");
        customer.addProductToShoppingCart(new Product(8, "Zahnpasta", Category.SONSTIGES, 0.89, -0.2));
        customer.addProductToShoppingCart(new Product(7, "Paprika", Category.GEMUESE, 1.79, -0.03));
        customer.buyShoppingCart();

        assertEquals(-0.23, productService.calculateSavedCo2Value(customer), 0.001);
    }

    @Test
    public void calculateSavedCo2ValueAllPositiveCo2() {
        Customer customer = new Customer("PosKunde");
        customer.addProductToShoppingCart(new Product(1, "Apfel", Category.OBST, 2.39, 0.08));
        customer.addProductToShoppingCart(new Product(3, "Milch", Category.MILCHPRODUKTE, 1.19, 0.1));
        customer.buyShoppingCart();

        assertEquals(0.18, productService.calculateSavedCo2Value(customer), 0.001);
    }


    @Test
    public void groupProductsByCategory() {
        Map<Category, List<Product>> actual = productService.groupProductsByCategory(testCatalog);

        assertEquals(2, actual.get(Category.OBST).size());
        assertEquals(1, actual.get(Category.GEMUESE).size());
        assertEquals(1, actual.get(Category.MILCHPRODUKTE).size());
        assertEquals(1, actual.get(Category.SONSTIGES).size());
        assertFalse(actual.containsKey(Category.FLEISCH));
    }

    @Test
    public void groupProductsByCategoryEmptyList() {
        assertTrue(productService.groupProductsByCategory(List.of()).isEmpty());
    }

    @Test
    public void groupProductsByCategoryAllSameCategory() {
        List<Product> obstOnly = List.of(
                new Product(1, "Apfel", Category.OBST, 2.39, 0.08),
                new Product(6, "Banane", Category.OBST, 1.59, 0.09)
        );
        Map<Category, List<Product>> result = productService.groupProductsByCategory(obstOnly);
        assertEquals(1, result.size());
        assertEquals(2, result.get(Category.OBST).size());
    }
}
