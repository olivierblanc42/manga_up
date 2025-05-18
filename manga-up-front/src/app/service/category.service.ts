import { CategoriesProjections, CategoryDto, CategoryProjection, CategoryWithMangas, MangasWithImages } from './../type.d';
import { HttpClient, HttpHeaders, } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, firstValueFrom, lastValueFrom, Observable } from 'rxjs';
import { GenreProjections, GenreProjection, GenreDto } from '../type';


@Injectable({
    providedIn: 'root'
})


export class CategoryService {
    url = "/api/public/categories/pagination";
    urlCategori = "/api/public/category/"; 
    urlAdd = "/api/categories/add";  

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

    categoriesWithManga = new BehaviorSubject<CategoryWithMangas | null>(null );
    currentcategoriesWithManga = this.categoriesWithManga.asObservable();
    categoryDto = new BehaviorSubject<CategoryDto | null>(null);

    async getAllCategoriesWithPagination(page: number = 0) {
        try {
            const r = await lastValueFrom(this.http.get<CategoriesProjections>(`${this.url}?page=${page}`));
            if (!r) return;
            this.categoriesProjections.next(r);
            console.log('Categories récupérés avec succès :', r);
        } catch (err) {
            console.error('Erreur lors de la récupération des genres :', err);
        }
    }

    async getCategory(id: number, page: number = 0) {
        try {
            const r = await lastValueFrom(this.http.get<{ category: CategoryWithMangas, mangas: MangasWithImages }>(`${this.urlCategori}${id}/mangas?page=${page}`));
            if (r) {
                const categoryWithMangas: CategoryWithMangas = {
                    ...r.category,
                    mangasWithImages: r.mangas
                };
                this.categoriesWithManga.next(categoryWithMangas);
            }
        } catch (err) {
            console.error('Erreur lors de la récupération des genres :', err);
        }
    }

    async addCategoryTest(category: CategoryDto): Promise<CategoryDto> {
        const response = await firstValueFrom(
            this.http.post<CategoryDto>(this.urlAdd, category,{ withCredentials: true } )
        );

        this.categoryDto.next(response);
        return response; 
    }
      
    addCategoryTest2(category: CategoryDto) {
        firstValueFrom(this.http.post<CategoryDto>(this.urlAdd, category, { withCredentials: true }))
            .then((r) => {
                if (!r) return;
                console.log(r)
                this.categoryDto.next(r);
            })
    }



}