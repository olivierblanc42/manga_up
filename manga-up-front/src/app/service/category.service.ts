import { CategoriesProjections, CategoryProjection } from './../type.d';
import { HttpClient, HttpHeaders, } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, lastValueFrom, Observable } from 'rxjs';
import { GenreProjections, GenreProjection, GenreDto } from '../type';


@Injectable({
    providedIn: 'root'
})


export class CategoryService {
    url = "/api/categories/pagination";
    urlCategori = "/api/categories/"; 


    options = {
        headers: new HttpHeaders({
            "Content-Type": "application/json",
        }),
        body: {
            id: '',
        },
    };
    constructor(
        private http: HttpClient
    ) { }

    categoriesProjections = new BehaviorSubject<CategoriesProjections | null>(null);
    currentCategoriesProjection = this.categoriesProjections.asObservable();

    categoriesProjection = new BehaviorSubject<CategoryProjection | null>(null);
    currentCategorieProjection = this.categoriesProjection.asObservable();


    async getAllGenreWithPagination() {
        try {
            const r = await lastValueFrom(this.http.get<CategoriesProjections>(this.url));
            if (!r) return;
            this.categoriesProjections.next(r);
            console.log('Genres récupérés avec succès :', r);
        } catch (err) {
            console.error('Erreur lors de la récupération des genres :', err);
        }
    }

    async getCategory(id: number) {
        try {
            const r = await lastValueFrom(this.http.get<CategoryProjection>(`${this.urlCategori}${id}`));
            if (!r) return;
            this.categoriesProjection.next(r);
            console.log('Genres récupérés avec succès :', r);
        } catch (err) {
            console.error('Erreur lors de la récupération des genres :', err);
        }
    }
}