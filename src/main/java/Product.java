import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Product {
    private int id;
    private String name;
    private Category category;
    private double price;
    private double co2Value;

    public Product(
            @JsonProperty("id") int id,
            @JsonProperty("name") String name,
            @JsonProperty("category") Category category,
            @JsonProperty("price") double price,
            @JsonProperty("co2Value") double co2Value
    ) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.co2Value = co2Value;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public double getCo2Value() {
        return co2Value;
    }

    @Override
    public boolean equals(Object obj){
        if(this==obj) return true;
        if(!(obj instanceof Product other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
