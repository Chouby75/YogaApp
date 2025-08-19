package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testConstructors() {
        // Teste le constructeur par défaut (@NoArgsConstructor)
        User user1 = new User();
        assertNotNull(user1, "Le constructeur par défaut doit créer un objet.");

        // Teste le constructeur avec les champs @NonNull (@RequiredArgsConstructor)
        User user2 = new User("email@test.com", "Dupont", "Jean", "password123", true);
        assertNotNull(user2, "Le constructeur requis doit créer un objet.");
        assertEquals("email@test.com", user2.getEmail());

        // Teste le constructeur avec tous les champs (@AllArgsConstructor)
        User user3 = new User(1L, "email2@test.com", "Durand", "Marie", "password456", false, null, null);
        assertNotNull(user3, "Le constructeur complet doit créer un objet.");
        assertEquals(1L, user3.getId());
    }

    @Test
    void testEqualsAndHashCode() {
        
        // Crée deux users avec le même ID, mais le reste doit être non-null
        User user1 = User.builder()
                .id(1L)
                .email("user1@mail.com")
                .lastName("Doe")
                .firstName("John")
                .password("password")
                .admin(false)
                .build();

        User user2 = User.builder()
                .id(1L)
                .email("user2@mail.com") // L'email peut être différent, mais il doit exister
                .lastName("Doe")
                .firstName("Jane")
                .password("password2")
                .admin(true)
                .build();

        // Crée un troisième user avec un ID différent et tous les champs non-null
        User user3 = User.builder()
                .id(2L)
                .email("user3@mail.com")
                .lastName("Smith")
                .firstName("Alex")
                .password("password3")
                .admin(false)
                .build();

        // Vérifie l'égalité des objets avec le même ID
        assertEquals(user1, user2, "Les utilisateurs avec le même ID doivent être égaux.");
        
        // Vérifie que les hashCodes sont identiques pour les objets égaux
        assertEquals(user1.hashCode(), user2.hashCode(), "Les objets égaux doivent avoir le même hashCode.");

        // Vérifie la non-égalité des objets avec des IDs différents
        assertNotEquals(user1, user3, "Les utilisateurs avec des IDs différents ne doivent pas être égaux.");
        assertNotEquals(null, user1, "Un utilisateur ne doit pas être égal à null.");
    }

    @Test
    void testToString() {
        
        // Crée un objet complet pour tester la méthode toString
        User user = User.builder()
                .id(1L)
                .email("user@test.com")
                .lastName("Doe")
                .firstName("John")
                .password("password")
                .admin(false)
                .build();
        
        // On appelle toString et on s'assure qu'il n'y a pas d'exception
        String userString = user.toString();
        assertNotNull(userString, "La méthode toString ne doit pas retourner null.");
        assertTrue(userString.contains("id=1"), "Le toString doit contenir l'ID.");
    }
}