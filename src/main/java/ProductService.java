import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductService {

    public List<Product> filterProductsByPrice(List<Product> catalog, double price) {
        return catalog.stream()
                .filter(product -> product.getPrice() <= price)
                .toList();
    }

    public List<Product> filterProductsByCategory(List<Product> catalog, Category category) {
        return catalog.stream()
                .filter(product -> product.getCategory() == category)
                .toList();
    }

    public List<Product> sortProductsByName(List<Product> catalog) {
        return catalog.stream()
                .sorted(Comparator.comparing(Product::getName))
                .toList();
    }

    public double calculateSavedCo2Value(Customer customer) {
        return customer.getShoppingHistory().stream()
                .mapToDouble(Product::getCo2Value)
                .sum();
    }

    public Map<Category, List<Product>> groupProductsByCategory(List<Product> catalog) {
        return catalog.stream().collect(Collectors.groupingBy(Product::getCategory));
    }

    public void addNewProduct(Product product, List<Product> catalog){
        catalog.add(product);
    }
}