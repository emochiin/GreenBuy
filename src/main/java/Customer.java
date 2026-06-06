import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer {
    private String name;
    private List<Product> shoppingCart;
    private List<Product> shoppingHistory;

    public Customer(String name){
        this.name = name;
        this.shoppingHistory = new ArrayList<>();
        this.shoppingCart = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Product> getShoppingHistory() {
        return Collections.unmodifiableList(shoppingHistory);
    }

    public List<Product> getShoppingCart() {
        return Collections.unmodifiableList(shoppingCart);
    }
    public void addProductToShoppingCart(Product product){
        shoppingCart.add(product);
    }
    public void deleteProductFromShoppingCart(Product product){
        shoppingCart.remove(product);
    }
    public void buyShoppingCart(){
        shoppingHistory.addAll(shoppingCart);
        shoppingCart.clear();
    }

}