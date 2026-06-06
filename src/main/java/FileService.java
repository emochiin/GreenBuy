import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
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
}
