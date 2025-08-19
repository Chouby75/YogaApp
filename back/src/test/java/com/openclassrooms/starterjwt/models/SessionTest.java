package com.openclassrooms.starterjwt.models;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;

class SessionTest {

    @Test
    void testEqualsAndHashCode() {
        // Crée deux sessions avec le même ID, mais le reste des données peut être différent
        Session session1 = Session.builder()
                .id(1L)
                .name("Yoga Matin")
                .date(new Date())
                .description("Une session de yoga relaxante.")
                .teacher(new Teacher()) // Pas besoin d'un vrai objet, un mock suffit
                .users(new ArrayList<>()) // Idem
                .build();

        Session session2 = Session.builder()
                .id(1L)
                .name("Yoga du Soir") // Nom différent
                .date(new Date()) // Date différente
                .description("Une session de yoga intense.")
                .teacher(new Teacher())
                .users(new ArrayList<>())
                .build();

        // Crée une troisième session avec un ID différent
        Session session3 = Session.builder()
                .id(2L)
                .name("Méditation")
                .date(new Date())
                .description("Une session de méditation.")
                .teacher(new Teacher())
                .users(new ArrayList<>())
                .build();

        // 1. On vérifie l'égalité : les sessions avec le même ID doivent être égales
        assertEquals(session1, session2, "Les sessions avec le même ID doivent être égales.");
        
        // 2. On vérifie l'inégalité : les sessions avec des ID différents ne doivent pas être égales
        assertNotEquals(session1, session3, "Les sessions avec des IDs différents ne doivent pas être égales.");
        
        // 3. On vérifie que le hashCode est le même pour les objets égaux
        assertEquals(session1.hashCode(), session2.hashCode(), "Les objets égaux doivent avoir le même hashCode.");

        // 4. On vérifie les cas d'égalité avec null ou d'autres types d'objets
        assertNotEquals(null, session1, "Une session ne doit pas être égale à null.");
        assertNotEquals(session1, new Object(), "Une session ne doit pas être égale à un objet de classe différente.");
    }
}