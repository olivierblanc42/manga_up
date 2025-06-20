describe('Page d\'accueil Manga', () => {
    beforeEach(() => {
        cy.visit('http://localhost:4200/');
        cy.viewport(1024, 768)


    });

    it('Test de la presence du lien account et redirection quand n\'est pas connecté', () => {
        cy.intercept('GET', '/api/auth/check', {
            statusCode: 403,
            body: { message: 'Forbidden' },
        }).as('getForbidden')

        cy.get('nav').first().should('exist'); 
        cy.get('nav').first().find('.first-nav').find('a[href="/account"]')
            .should('exist')
            .click();
        cy.url().should('include', '/login');

        cy.url().should('include', '/login');
        cy.get('h1').should('contain', 'Authentification Utilisateur'); 
        cy.get("xng-breadcrumb").find('.xng-breadcrumb-link').click();
        cy.visit('http://localhost:4200/');

    });
    it('redirection dans la bar de navigation secondaire', () => {
        cy.get('nav').find('.secondary-nav'); 
        cy.get('.secondary-nav').find('a[href="/mangas"]')
            .should('exist')
            .click();
           cy.url().should('include', '/mangas');
        cy.get("xng-breadcrumb").find('.xng-breadcrumb-link').click();

        cy.get('.secondary-nav').find('a[href="/genres"]')
            .should('exist')
            .click();
        cy.url().should('include', '/genres');
        cy.get("xng-breadcrumb").find('.xng-breadcrumb-link').click();

        cy.get('.secondary-nav').find('a[href="/categories"]')
            .should('exist')
            .click();
        cy.url().should('include', '/categories');

        cy.get("xng-breadcrumb").find('.xng-breadcrumb-link').click();
        cy.get('.secondary-nav').find('a[href="/auteurs"]')
            .should('exist')
            .click();
        cy.url().should('include', '/auteurs');
        cy.get("xng-breadcrumb").find('.xng-breadcrumb-link').click();
    });



    it('affiche le bandeau principal (swiper)', () => {
        cy.get('swiper-container').first().should('exist');
        cy.get('swiper-container').first().find('swiper-slide').should('have.length.at.least', 1);
    });

    it('affiche la section Nouveauté Mangas', () => {
        cy.contains('h2', 'Nouveauté Mangas').should('be.visible');
        cy.get('.swiper-manga').first().find('swiper-slide').should('have.length.at.least', 1);
        cy.get('.section__grid--home ui-card').should('have.length.at.least', 1);
    });

    it('chaque carte manga affiche un titre et un bouton voir plus', () => {
        cy.get('.section__grid--home ui-card').each(($card) => {
            cy.get('.card-manga__title', { timeout: 10000 }).should('exist')
            cy.wrap($card).find('.btn-card').should('contain', 'voir plus');
        });
    });

    it('affiche la section Genres', () => {
        cy.contains('h2', 'Genres').should('be.visible');
        cy.get('.swiper-genre').find('swiper-slide').should('have.length.at.least', 1);
        cy.get('.section__grid--home ui-card').should('exist');
        // Test d'une carte genre
        cy.get('.section__grid--home').eq(2) 
            .find('ui-card').first()
            .find('.card-genre__title')
            .should('exist');    });

    it('affiche la section Manga du moment', () => {
        cy.contains('h2', 'Manga du moment').should('be.visible');
        cy.get('.single-card').should('exist');
        cy.get('.single-card__infos').should('exist');
        cy.get('.single-card__summary').should('exist');
        cy.get('.btn-card').contains('Voir plus').should('exist');
    });

    it('affiche les infos principales', () => {
        cy.get('.infos').should('exist');
        cy.get('.info__text span').should('exist');
    });

    it('les images de manga ont un attribut alt', () => {
        cy.get('img').each(($img) => {
            cy.wrap($img).should('have.attr', 'alt');
        });
    });


    it('le lien pour la page manga fonctionne',()=>{
        cy.get('.manga-link').should('exist');
        cy.get('.manga-link').click();
        cy.visit("http://localhost:4200/mangas")
        cy.contains('h1', 'Mangas').should('be.visible');
        cy.get('.section__grid ui-card').should('exist');
        cy.get('.section__grid ui-card').each(($card) => {
            cy.get('.card-manga__title', { timeout: 10000 }).should('exist')
            cy.wrap($card).find('.btn-card').should('contain', 'voir plus');
        });
        cy.get("ui-card").eq(1).find('.btn-card').click();
        cy.get("xng-breadcrumb").find('.xng-breadcrumb-link').click();
    })



    it('Test link to the genre page', () => {
        cy.get('.genre-link').should('exist');
        cy.get('.genre-link').click();
        cy.visit("http://localhost:4200/genres")
        cy.contains('h1', 'Genres', { timeout: 10000 }).should('be.visible');
        cy.get('.section__grid ui-card').should('exist');
        cy.get('.section__grid ui-card').each(($card) => {
            cy.get('.card-genre__title', { timeout: 10000 }).should('exist')
            cy.wrap($card).find('.btn-card').should('contain', 'voir plus');
        });
        cy.get("ui-card").eq(1).find('.btn-card').click();
        cy.get("xng-breadcrumb").find('.xng-breadcrumb-link').click();
    })

});