package fr.abes.helloabes.core.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Users {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private String id;
    private String userName;
    private String password;

    public Users(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
