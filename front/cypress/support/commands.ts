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
