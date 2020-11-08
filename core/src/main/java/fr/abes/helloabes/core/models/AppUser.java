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
public class AppUser {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private String id;
    private String userName;
    private String passWord;

    public AppUser(String userName, String password) {
        this.userName = userName;
        this.passWord = password;
    }

}
