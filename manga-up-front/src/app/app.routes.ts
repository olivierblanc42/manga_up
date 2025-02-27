import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { MangasComponent } from './pages/mangas/mangas.component';
import { GenresComponent } from './pages/genres/genres.component';
import { CategoriesComponent } from './pages/categories/categories.component';
import { AuteursComponent } from './pages/auteurs/auteurs.component';
export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'mangas', component: MangasComponent },
    { path: 'genres', component: GenresComponent },
    { path: 'categories', component: CategoriesComponent },
    { path: 'auteurs', component: AuteursComponent }
];
