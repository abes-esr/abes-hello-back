package fr.abes.helloabes.web.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;
   
}
