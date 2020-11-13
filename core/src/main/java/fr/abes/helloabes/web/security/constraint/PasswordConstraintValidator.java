package fr.abes.helloabes.web.security.constraint;

import org.passay.*;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Cette classe représente un processus de validation des mots de passe
 * afin de vérifier si le mot de passe respècte les règles de sécurité.
 * @since 0.0.1
 */
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    /** Contient les messages de contrainte pour chaque règle */
    private MessageResolver resolver;

    /**
     * Cette fonction permet de charger en mémoire les messages d'exigence à l'initilisaion.
     * @param constraintAnnotation
     * @exception RuntimeException si le fichier n'existe pas ou ne peut pas être chargé.
     */
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            URL resource = classLoader.getResource("web/security/constraint/passwordConstraintMessage.properties");
            if (resource == null) {
                throw new FileNotFoundException("Le fichier contenant les messages de contrainte des mots de passe n'existe pas");
            } else {
                Properties props = new Properties();
                props.load(new FileInputStream(classLoader.getResource("web/security/constraint/passwordConstraintMessage.properties").getFile()));
                this.resolver = new PropertiesMessageResolver(props);
            }

        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException("Erreur dans le chargement du fichier contenant les messages de contrainte des mots de passe", ex);
        }
    }

    /**
     * Verifie si le mot de passe respecte les règles de sécurité.
     * @param password Mot de passe à tester.
     * @param context
     * @return  Vrai si le mot de passe respècte les règles de sécurité, Faux sinon.
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password == null) {
            return false;
        }

        PasswordValidator validator = new PasswordValidator(resolver,Arrays.asList(
                // au moins 8 caractere et maximum 100.
                new LengthRule(8,100),
                // au moins un caractere majuscule
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                // au moins un caractere minuscule
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                // au moins un chiffre
                new CharacterRule(EnglishCharacterData.Digit, 1),
                // au moins un caractere spécial
                new CharacterRule(EnglishCharacterData.Special, 1),
                // Pas d'espace blanc
                new WhitespaceRule()
        ));

        RuleResult result = validator.validate(new PasswordData(password));

        if (result.isValid()) {
            return true;
        }

        List<String> messages = validator.getMessages(result);
        String messageTemplate = messages.stream().collect(Collectors.joining(","));
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}
