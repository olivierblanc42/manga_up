import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { MangasComponent } from './pages/mangas/mangas.component';
import { MangaComponent } from './pages/manga/manga.component';
import { GenresComponent } from './pages/genres/genres.component';
import { GenreComponent } from './pages/genre/genre.component';
import { CategoriesComponent } from './pages/categories/categories.component';
import { CategorieComponent } from './pages/categorie/categorie.component';
import { AccountComponent } from './pages/account/account.component';
import { AuteursComponent } from './pages/auteurs/auteurs.component';
import { AuteurComponent } from './pages/auteur/auteur.component';
import { AdminLayoutComponent } from './pages/admin/admin-layout/admin-layout.component';
import { MangasAdminComponent } from './pages/admin/mangas/mangas.component';
import { AuthorsAdminComponent } from './pages/admin/authors/authors.component';
import { CategoriesAdminComponent } from './pages/admin/categories/categories.component';
import { CategoryAdminComponent } from './pages/admin/category/category.component';

import { GenresAdminComponent } from './pages/admin/genres/genres.component';
import { UsersRegisterComponent } from './pages/users-register/users-register.component';
import { PrivacyPolicyComponent } from './pages/privacy-policy/privacy-policy.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { CartComponent } from './pages/cart/cart.component';
import { LoginComponent } from './pages/login/login.component';
import { AuthGuard } from './service/auth.guard';
import { RoleGuard } from './service/role.guard'; // Le guard de rôle
import { ForbiddenComponent } from './pages/forbidden/forbidden.component';
import { SearchComponent } from './pages/search/search.component';
import { GenreAdminComponent } from './pages/admin/genre/genre.component';

export const routes: Routes = [
    { path: '', component: HomeComponent, data: { breadcrumb: 'Home' } },
    { path: 'mangas', component: MangasComponent },
    { path: 'genres', component: GenresComponent },
    { path: 'genre/:id', component: GenreComponent },
    { path: 'categories', component: CategoriesComponent },
    { path: 'categorie/:id', component: CategorieComponent },
    { path: 'account', component: AccountComponent, canActivate: [AuthGuard] },
    { path: 'register', component: UsersRegisterComponent },
    { path: 'auteurs', component: AuteursComponent },
    { path: 'auteur/:id', component: AuteurComponent },
    { path: 'privacy', component: PrivacyPolicyComponent },
    { path: 'manga/:id', component: MangaComponent },
    { path: "cart", component: CartComponent },
    { path: "login", component: LoginComponent },
    {
        path: 'search/:query',
        loadComponent: () => import('./pages/search/search.component').then(m => m.SearchComponent)
      } , 
        // Routes administratives protégées par RoleGuard
    {
        path: 'admin', component: AdminLayoutComponent,
        canActivate: [AuthGuard, RoleGuard], 
        data: { role: 'ADMIN_ROLE' }, 
        children: [
            { path: 'mangasAdmin', component: MangasAdminComponent },
            { path: 'genresAdmin', component: GenresAdminComponent },
            { path: 'genre/:id', component: GenreAdminComponent },
            { path: 'categoriesAdmin', component: CategoriesAdminComponent },
            { path: 'category/:id', component: CategoryAdminComponent },
            { path: 'authorsAdmin', component: AuthorsAdminComponent },
        ]
    },
    { path: 'forbidden', component: ForbiddenComponent },
    { path: '404', component: NotFoundComponent },
    { path: '**', redirectTo: '/404' }
];
