describe('Test accès compte', () => {
    beforeEach(() => {
        cy.request('POST', 'http://localhost:8080/api/auth/login', {
            username: 'Utilisateur',
            password: 'L7v!x@P4z#Kw2T'
        }).then((resp) => {
            expect(resp.status).to.eq(200);
        });

        cy.visit('http://localhost:4200/');
    });

    it('autorise l’accès à /account après login', () => {
        cy.get('nav').first().should('exist');

        cy.get('nav')
            .first()
            .find('.first-nav')
            .find('a[href="/account"]')
            .should('exist')
            .click();

        cy.url().should('include', '/account');

        cy.get('app-logout').find('button').click();
    });

    afterEach(() => {

        // Nettoyage local
        cy.clearLocalStorage();
        cy.clearCookies();
    });
});
  