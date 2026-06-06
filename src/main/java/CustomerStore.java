import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CustomerStore {
    private final List<Customer> activeCustomers = new LinkedList<>();
    private final List<Customer> inactiveCustomers = new LinkedList<>();


    public List<Customer> getActiveCustomers(){
        return activeCustomers;
    }

    public List<Customer> getInactiveCustomers(){
        return inactiveCustomers;
    }

    public int getCustomerCount(){
        return activeCustomers.size();
    }
}
