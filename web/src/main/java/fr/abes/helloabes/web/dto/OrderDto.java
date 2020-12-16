package fr.abes.helloabes.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("fournisseur")
    private SupplierDto supplier;

    @JsonProperty("products")
    private List<ProductDto> products = new ArrayList<>();

}
