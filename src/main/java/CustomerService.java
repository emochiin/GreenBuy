import java.util.List;

public class CustomerService {

    private final CustomerStore customerStore;

    public CustomerService(CustomerStore customerStore){
        this.customerStore = customerStore;
    }

    public void addCustomer(Customer customer){
        customerStore.getActiveCustomers().add(customer);
    }

    public void deactivateCustomer(Customer customer){
        customerStore.getInactiveCustomers().add(customer);
        customerStore.getActiveCustomers().remove(customer);
    }
}
