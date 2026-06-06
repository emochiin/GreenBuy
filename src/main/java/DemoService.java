import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class DemoService {
    FileService fileService = new FileService();
    ProductService productService = new ProductService();
    CustomerStore customerStore = new CustomerStore();
    CustomerService customerService = new CustomerService(customerStore);
    List<Product> catalog = fileService.readJSON();
    Scanner sc = new Scanner(System.in);


    public void menuNavigate(int eingabe) {
        switch (eingabe) {
            case 1 -> showCatalog();
            case 2 -> userSettings();
            case 3 -> catalogSettings();
            case 4 -> exportSettings();
            case 5 -> IO.println("Anwendung beenden...");
        }
    }

    private String validateLetterInput() {
        while (true) {
            try {
                String eingabe = sc.nextLine();
                if (eingabe != null && !eingabe.isBlank() && eingabe.matches("[a-zA-Z]+")) return eingabe.trim();
                else IO.println("Ungültige Eingabe, bitte einen Benutzernamen ohne Zahlen eingeben");
            } catch (InputMismatchException e) {
                IO.println("Bitte nur Buchstaben eingeben.");
                sc.next();
            }
        }
    }

    public int validateInteger(int min, int max) {
        while (true) {
            IO.println("Eingabe: ");
            try {
                int eingabe = sc.nextInt();
                sc.nextLine();
                if (eingabe >= min && eingabe <= max) return eingabe;
                IO.println("Ungültige Eingabe, bitte " + min + "-" + max + " eingeben");
            } catch (InputMismatchException e) {
                IO.println("Bitte eine Zahl eingeben.");
                sc.next();
            }
        }
    }

    private void showCatalog() {
        int counter = 1;
        for (Product product : catalog) {
            IO.println(counter + ". " + product.getName() + " - Kategorie: " + product.getCategory() + " - Preis: " + product.getPrice() + " - Co2 - Ersparnis: " + product.getCo2Value());
            counter++;
        }
    }

    private void userSettings() {
        IO.println("1. Aktive Benutzer anzeigen");
        IO.println("2. Benutzer hinzufügen");
        IO.println("3. Benutzer entfernen");
        IO.println("4. Deaktivierte Benutzer anzeigen");
        IO.println("5. Zurück");
        int eingabe = validateInteger(1, 5);
        switch (eingabe) {
            case 1 -> {
                if (customerStore.getActiveCustomers().isEmpty()) {
                    IO.println("Keine Benutzer aktuell");
                } else {
                    listAllActiveCustomers();
                }
            }
            case 2 -> {
                String userName = findDuplicate();
                customerService.addCustomer(new Customer(userName));
                IO.println("Benutzer hinzugefügt!");

            }
            case 3 -> {
                if (customerStore.getActiveCustomers().isEmpty()) {
                    IO.println("Keine Benutzer aktuell");
                } else {
                    IO.println("Welcher Benutzer soll entfernt werden?");
                    listAllActiveCustomers();
                    int input = validateInteger(1, customerStore.getCustomerCount());
                    customerService.deactivateCustomer(customerStore.getActiveCustomers().get(input - 1));
                }
            }
            case 4 -> {
                if (customerStore.getInactiveCustomers().isEmpty()) {
                    IO.println("Keine Benutzer aktuell");
                } else {
                    listAllDeactivatedCustomers();
                }
            }
            case 5 -> IO.println("Zurück ins Menü...");
        }
    }

    private void listAllActiveCustomers() {
        int counter = 1;
        for (Customer customer : customerStore.getActiveCustomers()) {
            IO.println(counter + ". " + customer.getName());
            counter++;
        }
    }

    private void listAllDeactivatedCustomers() {
        int counter = 1;
        for (Customer customer : customerStore.getInactiveCustomers()) {
            IO.println(counter + ". " + customer.getName());
            counter++;
        }
    }

    private void catalogSettings() {
        IO.println("1. Produkt hinzufügen");
        IO.println("2. Zurück");
        int eingabe = validateInteger(1, 2);
        if (eingabe == 1) {
            IO.println("Produkt hinzufügen:");
            Product newProduct = setNewProductParameters();
            productService.addNewProduct(newProduct, catalog);
            IO.println("Produkt hinzugefügt!");
        } else IO.println("Zurück ins Menü...");
    }

    private Product setNewProductParameters() {
        IO.println("Name des Produkt:");
        int id = catalog.size() + 1;
        String name = validateLetterInput();
        Category category = validateCategory();
        double price = validatePrice();
        double co2Value = validateCo2Value();
        return new Product(id, name, category, price, co2Value);
    }

    private Category validateCategory() {
        IO.println("Alle Kategorien:");
        int counter = 1;
        for (Category category : Category.values()) {
            IO.println(counter + ". " + category);
            counter++;
        }
        int eingabe = validateInteger(1, 6);

        return switch (eingabe) {
            case 1 -> Category.OBST;

            case 2 -> Category.GEMUESE;

            case 3 -> Category.MILCHPRODUKTE;

            case 4 -> Category.FLEISCH;

            case 5 -> Category.BACKWAREN;

            default -> Category.SONSTIGES;
        };
    }

    private double validatePrice() {
        IO.println("- Preis -");
        while (true) {
            IO.println("Eingabe: ");
            try {
                double eingabe = sc.nextDouble();
                if (eingabe > 0) return eingabe;
                IO.println("Ungültige Eingabe, bitte Wert größer als 0 eingeben");
            } catch (InputMismatchException e) {
                IO.println("Bitte eine Zahl eingeben.");
                sc.next();
            }
        }
    }

    private double validateCo2Value() {
        IO.println("Co2-Ausstoß:");
        while (true) {
            IO.println("Eingabe: ");
            try {
                return sc.nextDouble();
            } catch (InputMismatchException e) {
                IO.println("Bitte eine Zahl eingeben.");
                sc.next();
            }
        }
    }

    private void exportSettings() {
        IO.println("Export-Settings:");
        IO.println("1. Katalog exportieren");
        IO.println("2. Benutzerliste exportieren");
        IO.println("3. Zurück");
        int eingabe = validateInteger(1, 3);

        if (eingabe == 1) {
            fileService.writeCatalogToJSON(catalog);
            IO.println("Katalog exportiert als catalog.json");
        } else if (eingabe == 2) {
            fileService.writeUserListToJSON(customerStore.getActiveCustomers());
            IO.println("Userliste exportiert als user.json");
        } else IO.println("Zurück...");
    }

    private List<String> userNameList() {
        return customerStore.getActiveCustomers()
                .stream()
                .map(Customer::getName)
                .toList();
    }

    private String findDuplicate() {
        List<String> userNames= userNameList();
        while (true) {
            IO.println("Eingabe des Benutzernamen");
            String userName = validateLetterInput();
            if (!userNames.contains(userName)) return userName;
            IO.println("Benutzername bereits vorhanden, bitte einen anderen Wählen");
        }
    }
}
