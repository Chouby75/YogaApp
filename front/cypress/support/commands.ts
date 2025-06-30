// Fichier : cypress/support/commands.ts (La nouvelle version)

Cypress.Commands.add('loginAsAdmin', () => {
  cy.intercept('POST', '/api/auth/login', {
    body: {
      id: 1,
      username: 'userName',
      firstName: 'firstName',
      lastName: 'lastName',
      admin: true,
    },
  });

  // On remplace "fixture" par "body" et on met les données ici
  cy.intercept('GET', '/api/session', {
    body: [
      {
        id: 1,
        name: 'Yoga pour Débutants',
        description: 'Idéal pour commencer.',
        date: new Date().toISOString(), // .toISOString() pour un format standard
        teacher_id: 1,
        users: [],
      },
      {
        id: 2,
        name: 'Yoga pour Experts',
        description: 'Pour les pros du tapis.',
        date: new Date().toISOString(),
        teacher_id: 2,
        users: [],
      },
    ],
  }).as('session');

  cy.visit('/login');

  cy.get('input[formControlName=email]').type('yoga@studio.com');
  cy.get('input[formControlName=password]').type(
    `${'test!1234'}{enter}{enter}`
  );

  cy.url().should('include', '/sessions');
});

Cypress.Commands.add('createSession', (session) => {
  // Cette commande ne fait qu'une chose : remplir le formulaire.
  cy.get('input[formControlName=name]').type(session.name);
  cy.get('input[formControlName=date]').type(session.date);

  cy.get('mat-select[formControlName=teacher_id]').click();

  // On attend que les options soient bien apparues
  cy.get('mat-option').should('be.visible');

  cy.get('mat-option').contains(session.teacherName).click();

  cy.get('textarea[formControlName=description]').type(session.description);

  cy.get('button[type=submit]').click();
});

Cypress.Commands.add('logout', () => {
  cy.contains('span', 'Logout').click();
});

Cypress.Commands.add('participate', () => {
  cy.contains('button', 'Participate').click();
});

Cypress.Commands.add('unParticipate', () => {
  cy.contains('button', 'Do not participate').click();
});

Cypress.Commands.add('loginError', () => {
  cy.visit('/login');

  // 2. On tape un email au mauvais format
  cy.get('input[formControlName=email]').type('un-email-cassé');

  // 3. VÉRIFICATION : On regarde le bouton "Submit"
  // et on vérifie qu'il a bien l'attribut "disabled"
  cy.get('button[type=submit]').should('have.attr', 'disabled');
});

Cypress.Commands.add('registerAsUser', () => {
  // 1. PRÉPARATION : On prépare notre espion pour l'appel d'inscription.
  // On lui dit de simuler une réponse de succès.
  cy.intercept('POST', '/api/auth/register', {
    statusCode: 200, // Le serveur répond "OK"
    body: { message: 'User registered successfully!' },
  }).as('registerAttempt');

  // 2. ACTION : Le robot se rend sur la page d'inscription.
  cy.visit('/register');

  // 3. ACTION : Le robot remplit le formulaire.
  cy.get('input[formControlName=firstName]').type('Naruto');
  cy.get('input[formControlName=lastName]').type('Uzumaki');
  cy.get('input[formControlName=email]').type('naruto.uzumaki@konoha.com');
  cy.get('input[formControlName=password]').type('Rasengan!123{enter}'); // On peut appuyer sur Entrée à la fin

  // 4. VÉRIFICATION :

  // On attend que la tentative d'inscription soit bien terminée.
  cy.wait('@registerAttempt');

  // On vérifie qu'on a bien été redirigé vers la page de login.
  cy.url().should('include', '/login');
});

Cypress.Commands.add('accountDetails', () => {
  // 1. PRÉPARATION : On prépare notre espion pour l'appel de détails du compte.
  cy.intercept('GET', '/api/user/1', {
    body: {
      id: 1,
      email: 'userName@gmail.com',
      firstName: 'firstName',
      lastName: 'lastName',
      admin: true,
    },
  }).as('getAccountDetails');

  // 2. ACTION : Le robot se rend sur la page de détails du compte.
  cy.contains('span', 'Account').click();
  cy.url().should('include', '/me');

  // On vérifie que le nom complet est affiché correctement.
  //   cy.wait('@getAccountDetails');
  cy.contains('p', 'firstName LASTNAME').should('be.visible');
});
