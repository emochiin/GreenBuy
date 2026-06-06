import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class FileService {

    private final String catalogPath;
    private final String userPath;

    public FileService() {
        this("src/main/resources/catalog.json", "src/main/resources/user.json");
    }

    public FileService(String catalogPath) {
        this(catalogPath, "src/main/resources/user.json");
    }

    public FileService(String catalogPath, String userPath) {
        this.catalogPath = catalogPath;
        this.userPath = userPath;
    }

    public List<Product> readJSON() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(
                    new File(catalogPath),
                    new TypeReference<>() {}
            );
        } catch (IOException e) {
            e.printStackTrace();
            IO.println("Fehler beim Lesen. Leere Liste wird als Fallback zurückgegeben.");
            return new LinkedList<>();
        }
    }

    public void writeCatalogToJSON(List<Product> products) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(catalogPath), products);
        } catch (IOException e) {
            e.printStackTrace();
            IO.println("Fehler beim Speichern in den Katalog.");
        }
    }

    public void writeUserListToJSON(List<Customer> customers) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(userPath), customers);
        } catch (IOException e) {
            e.printStackTrace();
            IO.println("Fehler beim Speichern der Benutzerliste.");
        }
    }

    public List<Product> readCatalogFromCSV() {
        String csvPath = catalogPath.replace(".json", ".csv");
        List<Product> products = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            br.readLine(); // header überspringen
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                Category category = Category.valueOf(parts[2].trim());
                double price = Double.parseDouble(parts[3].trim());
                double co2Value = Double.parseDouble(parts[4].trim());
                products.add(new Product(id, name, category, price, co2Value));
            }
        } catch (IOException e) {
            e.printStackTrace();
            IO.println("Fehler beim Lesen der Katalog-CSV. Leere Liste wird zurückgegeben.");
        }
        return products;
    }

    public void writeCatalogToCSV(List<Product> products) {
        String csvPath = catalogPath.replace(".json", ".csv");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvPath))) {
            bw.write("id,name,category,price,co2Value");
            bw.newLine();
            for (Product p : products) {
                bw.write(p.getId() + "," + escapeCsv(p.getName()) + "," +
                        p.getCategory() + "," + p.getPrice() + "," + p.getCo2Value());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            IO.println("Fehler beim Speichern des Katalogs als CSV.");
        }
    }

    private String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
