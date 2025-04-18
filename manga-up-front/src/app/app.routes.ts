import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { MangasComponent } from './pages/mangas/mangas.component';
import { MangaComponent } from './pages/manga/manga.component';

import { GenresComponent } from './pages/genres/genres.component';
import { GenreComponent } from './pages/genre/genre.component';

import { CategoriesComponent } from './pages/categories/categories.component';
import { CategorieComponent } from './pages/categorie/categorie.component';


import { AuteursComponent } from './pages/auteurs/auteurs.component';
import { AuteurComponent } from './pages/auteur/auteur.component';


// Administration
import { AdminLayoutComponent } from './pages/admin/admin-layout/admin-layout.component';
import { MangasAdminComponent } from './pages/admin/mangas/mangas.component';
import { AuthorsAdminComponent } from './pages/admin/authors/authors.component';
import { CategoriesAdminComponent } from './pages/admin/categories/categories.component';
import { GenresAdminComponent } from './pages/admin/genres/genres.component';



import { PrivacyPolicyComponent } from './pages/privacy-policy/privacy-policy.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
export const routes: Routes = [
    { path: '', component: HomeComponent, data: { breadcrumb: 'Home' } },
    { path: 'mangas', component: MangasComponent },
    { path: 'genres', component: GenresComponent },
    { path: 'genre', component: GenreComponent },
    { path: 'categories', component: CategoriesComponent },
    { path: 'categorie', component: CategorieComponent },

    { path: 'auteurs', component: AuteursComponent },
    { path: 'auteur', component: AuteurComponent },

    { path: 'privacy', component: PrivacyPolicyComponent },
    { path: 'manga/:id', component: MangaComponent },

    {
        path: 'admin', component: AdminLayoutComponent,
        children: [
            { path: 'mangasAdmin', component: MangasAdminComponent },
            { path: 'genresAdmin', component: GenresAdminComponent },
            { path: 'categoriesAdmin', component: CategoriesAdminComponent },
            { path: 'authorsAdmin', component: AuthorsAdminComponent },

        ]
    },


    { path: '404', component: NotFoundComponent },
    { path: '**', redirectTo: '/404' }
];
