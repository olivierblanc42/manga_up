import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { MangasComponent } from './pages/mangas/mangas.component';
import { MangaComponent } from './pages/manga/manga.component';

import { GenresComponent } from './pages/genres/genres.component';
import { GenreComponent } from './pages/genre/genre.component';

import { CategoriesComponent } from './pages/categories/categories.component';
import { AuteursComponent } from './pages/auteurs/auteurs.component';
import { PrivacyPolicyComponent } from './pages/privacy-policy/privacy-policy.component';
export const routes: Routes = [
    { path: '', component: HomeComponent, data: { breadcrumb: 'Home' } },
    { path: 'mangas', component: MangasComponent },
    { path: 'genres', component: GenresComponent },
    { path: 'genre', component: GenreComponent },
    { path: 'categories', component: CategoriesComponent },
    { path: 'auteurs', component: AuteursComponent },
    { path: 'privacy', component: PrivacyPolicyComponent },
    { path: 'manga', component: MangaComponent },

];
