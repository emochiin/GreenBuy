import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedConstruction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FileServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void readJSONValidFileReturnsCorrectProducts() throws IOException {
        String json = """
                [
                  {"id":1,"name":"Apfel","category":"OBST","price":2.39,"co2Value":0.08},
                  {"id":3,"name":"Milch","category":"MILCHPRODUKTE","price":1.19,"co2Value":0.1}
                ]
                """;
        Path catalogFile = tempDir.resolve("catalog.json");
        Files.writeString(catalogFile, json);

        FileService fileService = new FileService(catalogFile.toString());
        List<Product> result = fileService.readJSON();

        assertEquals(2, result.size());
        assertEquals("Apfel", result.get(0).getName());
        assertEquals(Category.OBST, result.get(0).getCategory());
        assertEquals("Milch", result.get(1).getName());
    }

    @Test
    void readJSONReturnsEmptyList() {
        FileService fileService = new FileService("nonexistent/path/catalog.json");
        List<Product> result = fileService.readJSON();
        assertTrue(result.isEmpty());
    }

    @Test
    void readCorruptJSONReturnsEmptyList() throws IOException {
        Path corruptFile = tempDir.resolve("corrupt.json");
        Files.writeString(corruptFile, "{ dies: ist kein gültiges: JSON !!!");

        FileService fileService = new FileService(corruptFile.toString());
        List<Product> result = fileService.readJSON();
        assertTrue(result.isEmpty());
    }

    @Test
    void readEmptyJSONFileReturnsEmptyList() throws IOException {
        Path emptyFile = tempDir.resolve("empty.json");
        Files.writeString(emptyFile, "");

        FileService fileService = new FileService(emptyFile.toString());
        List<Product> result = fileService.readJSON();
        assertTrue(result.isEmpty());
    }

    @Test
    void readIncompleteJSONReturnsEmptyList() throws IOException {
        String json = "[{\"id\":1,\"name\":\"Apfel\",\"category\":\"OBST\"}]";
        Path badFile = tempDir.resolve("missing_field.json");
        Files.writeString(badFile, json);

        FileService fileService = new FileService(badFile.toString());
        List<Product> result = fileService.readJSON();
        assertNotNull(result);
    }

    @Test
    void readJSONMockedIOExceptionReturnsEmptyList() throws IOException {
        try (MockedConstruction<ObjectMapper> mocked = mockConstruction(ObjectMapper.class,
                (mock, context) -> doThrow(new IOException("Simulierter Lesefehler"))
                        .when(mock).readValue(any(File.class), any(TypeReference.class)))) {

            FileService fileService = new FileService();
            List<Product> result = fileService.readJSON();

            assertTrue(result.isEmpty());
        }
    }

    @Test
    void writeCatalogAndReadJSON() throws IOException {
        List<Product> products = List.of(
                new Product(1, "Apfel", Category.OBST, 2.39, 0.08),
                new Product(3, "Milch", Category.MILCHPRODUKTE, 1.19, 0.1)
        );
        Path outputFile = tempDir.resolve("output.json");
        FileService fileService = new FileService(outputFile.toString());

        fileService.writeCatalogToJSON(products);
        List<Product> result = fileService.readJSON();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(3, result.get(1).getId());
    }

    @Test
    void writeEmptyCatalogToJSONThenReadBackReturnsEmptyList() throws IOException {
        Path outputFile = tempDir.resolve("empty_catalog.json");
        FileService fileService = new FileService(outputFile.toString());

        fileService.writeCatalogToJSON(List.of());
        List<Product> result = fileService.readJSON();

        assertTrue(result.isEmpty());
    }

    @Test
    void writeCatalogToCSVCreatesCorrectContent() throws IOException {
        List<Product> products = List.of(
                new Product(1, "Apfel", Category.OBST, 2.39, 0.08),
                new Product(3, "Milch", Category.MILCHPRODUKTE, 1.19, 0.1)
        );
        Path catalogJson = tempDir.resolve("catalog.json");
        FileService fileService = new FileService(catalogJson.toString());

        fileService.writeCatalogToCSV(products);

        List<String> lines = Files.readAllLines(tempDir.resolve("catalog.csv"));
        assertEquals("id,name,category,price,co2Value", lines.get(0));
        assertEquals("1,Apfel,OBST,2.39,0.08", lines.get(1));
        assertEquals("3,Milch,MILCHPRODUKTE,1.19,0.1", lines.get(2));
    }

    @Test
    void readCatalogFromCSVReturnsCorrectProducts() throws IOException {
        String csv = """
                id,name,category,price,co2Value
                1,Apfel,OBST,2.39,0.08
                3,Milch,MILCHPRODUKTE,1.19,0.1
                """;
        Files.writeString(tempDir.resolve("catalog.csv"), csv);
        FileService fileService = new FileService(tempDir.resolve("catalog.json").toString());

        List<Product> result = fileService.readCatalogFromCSV();

        assertEquals(2, result.size());
        assertEquals(1, result.getFirst().getId());
        assertEquals("Apfel", result.getFirst().getName());
        assertEquals(Category.OBST, result.getFirst().getCategory());
        assertEquals(2.39, result.getFirst().getPrice());
        assertEquals(0.08, result.getFirst().getCo2Value());
    }

    @Test
    void writeThenReadCatalogCSVReturnsOriginalProducts() throws IOException {
        List<Product> products = List.of(
                new Product(1, "Apfel", Category.OBST, 2.39, 0.08),
                new Product(3, "Milch", Category.MILCHPRODUKTE, 1.19, 0.1)
        );
        FileService fileService = new FileService(tempDir.resolve("catalog.json").toString());

        fileService.writeCatalogToCSV(products);
        List<Product> result = fileService.readCatalogFromCSV();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(3, result.get(1).getId());
    }

    @Test
    void readCatalogFromCSVWithMissingFileReturnsEmptyList() {
        FileService fileService = new FileService("nonexistent/catalog.json");
        List<Product> result = fileService.readCatalogFromCSV();
        assertTrue(result.isEmpty());
    }

    @Test
    void writeEmptyCatalogToCSVThenReadReturnsEmptyList() throws IOException {
        FileService fileService = new FileService(tempDir.resolve("catalog.json").toString());

        fileService.writeCatalogToCSV(List.of());
        List<Product> result = fileService.readCatalogFromCSV();

        assertTrue(result.isEmpty());
    }
}
