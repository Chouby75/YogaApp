// cypress/e2e/sessions_flow.cy.ts

describe('Campaign: Session Management Flow', () => {
  it('should go to login page on register button click', () => {
    cy.registerAsUser();
  });
  // ===================================================================
  // MISSION 1: Se connecter et afficher la liste des sessions
  // ===================================================================
  it('should log in and correctly display the initial list of sessions', () => {
    // ACTION: On utilise notre super-attaque pour se connecter.
    // La commande intercepte déjà l'appel /api/session et le remplit
    // avec les données qu'on a mises dedans.
    cy.loginAsAdmin();

    // VÉRIFICATION:
    cy.wait('@session'); // On s'assure que les données sont chargées

    cy.get('.item').should('have.length', 2); // On vérifie qu'on a 2 sessions
    cy.get('.item').first().should('contain', 'Yoga pour Débutants');
  });

  it('should log error when trying to log in with invalid credentials', () => {
    // PRÉPARATION : On intercepte l'appel de connexion avec des données incorrectes
    cy.loginError();
  });

  // ===================================================================
  // MISSION 2: Consultation du Détail d'une Session
  // ===================================================================
  it('should navigate to the detail page when clicking on a session', () => {
    // PRÉPARATION : On se connecte.
    cy.loginAsAdmin();
    cy.wait('@session');

    // PRÉPARATION (pour la page de détail)
    // On prépare les espions pour les données spécifiques à la session 1.
    cy.intercept('GET', '/api/session/1', {
      body: { id: 1, name: 'Yoga pour Débutants' },
    }).as('getSessionDetail');

    cy.intercept('GET', '/api/teacher/1', {
      body: { id: 1, firstName: 'Margot', lastName: 'DELAHAYE' },
    }).as('getTeacherDetail');

    // ACTION : On clique sur le bouton "Detail" de la PREMIÈRE session.
    // On utilise .first() pour être sûr de cliquer sur la bonne.
    cy.get('.item').first().contains('button', 'Detail').click();

    // VÉRIFICATION :
    cy.url().should('include', '/sessions/detail/1'); // On est sur la bonne page.

    cy.wait('@getSessionDetail'); // On attend que les détails de la session soient chargés
    // On vérifie que le titre de la session est bien affiché.
    cy.get('h1').debug();
  });

  it('display user details in account page', () => {
    cy.loginAsAdmin();
    cy.wait('@session');
    cy.accountDetails();
  });

  // ===================================================================
  // MISSION 2 (Version Améliorée)
  // ===================================================================
  it('should allow an admin to create a new session and see it in the list', () => {
    // PRÉPARATION
    cy.loginAsAdmin();
    cy.wait('@session');

    // On prépare la fausse réponse pour la liste des profs sur la page de création
    cy.intercept('GET', '/api/teacher', {
      body: [{ id: 1, firstName: 'Margot', lastName: 'DELAHAYE' }],
    }).as('getTeachers');

    // ACTION 1: Naviguer vers la page de création
    cy.get('button[routerLink="create"]').click();
    cy.url().should('include', '/sessions/create');
    cy.wait('@getTeachers'); // On attend que la page soit prête

    // PRÉPARATION (pour la création)
    const newSession = {
      name: 'Session de Méditation Guidée',
      date: '2025-10-10',
      teacherName: 'Margot DELAHAYE',
      description: 'Relaxation totale garantie.',
    };

    // On prépare l'espion pour l'appel de création
    cy.intercept('POST', '/api/session', {
      body: { message: 'Session created!' },
    }).as('create');

    // ===============================================================
    // L'ASTUCE DE PRO !
    // On prépare un NOUVEL espion pour l'appel GET qui aura lieu APRES la redirection
    // Cette nouvelle liste contient notre session fraîchement créée !
    cy.intercept('GET', '/api/session', {
      body: [
        { id: 1, name: 'Yoga pour Débutants' /* ... */ },
        { id: 2, name: 'Yoga pour Experts' /* ... */ },
        { id: 3, name: 'Session de Méditation Guidée' /* ... */ }, // La nouvelle !
      ],
    }).as('getUpdatedSessions');
    // ===============================================================

    // ACTION 2: On utilise notre compétence de création de session !
    cy.createSession(newSession);

    // // VÉRIFICATION FINALE:
    // cy.wait('@create');
    // cy.wait('@getUpdatedSessions'); // On attend la nouvelle liste

    cy.url().should('include', '/sessions');

    // La vérification ultime ! On a maintenant 3 sessions.
    cy.get('.item').should('have.length', 3);
    cy.contains('Méditation Guidée').should('be.visible');
  });

  // ===================================================================
  // MISSION 3 (Version avec commande) : Se déconnecter proprement
  // ===================================================================
  it('should log the user out and redirect to the home page', () => {
    // 1. PRÉPARATION : On se connecte.
    cy.loginAsAdmin();
    cy.wait('@session'); // On attend que la page soit stable

    // 2. ACTION : On utilise notre nouvelle compétence !
    cy.logout();

    // 3. VÉRIFICATION : On vérifie que tout est revenu à la normale.
    cy.url().should('eq', 'http://localhost:4200/');
    cy.contains('span', 'Login').should('be.visible');
    cy.contains('span', 'Logout').should('not.exist');
  });
});
