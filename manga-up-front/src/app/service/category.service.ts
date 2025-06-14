import { CategoriesProjections, CategoryDto, CategoryProjection, CategoryWithMangas, MangasWithImages } from './../type.d';
import { HttpClient, HttpHeaders, } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, firstValueFrom, lastValueFrom, Observable } from 'rxjs';
import { GenreProjections, GenreProjection, GenreDto } from '../type';
import { environment } from '../../environments/environment.prod';


@Injectable({
    providedIn: 'root'
})


export class CategoryService {
    url = `${environment.apiUrl}/api/categories`
    urlPagination = `${environment.apiUrl}/api/public/categories/pagination`;
    urlCategori = `${environment.apiUrl}/api/public/category/`; 
    urlAdd = `${environment.apiUrl}/api/categories/add`;  

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
            const r = await lastValueFrom(this.http.get<CategoriesProjections>(`${this.urlPagination}?page=${page}`));
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

    async addCategory(category: CategoryDto): Promise<CategoryDto> {
        const response = await firstValueFrom(
            this.http.post<CategoryDto>(this.urlAdd, category,{ withCredentials: true } )
        );

        this.categoryDto.next(response);
        return response; 
    }
      
    async deleteCategory(id: number): Promise<CategoryDto> {
        const response = await firstValueFrom(
            this.http.delete<CategoryDto>(`${this.url}/${id}`, {
                withCredentials: true
            })
        );

        this.categoryDto.next(response);
        return response;
    }






    


    async updateCategory(category: CategoryDto): Promise<CategoryDto> {
        try {
            const urlWithId = `${this.url}/${category.id}`; 
            const updatedCategory = await lastValueFrom(
                this.http.put<CategoryDto>(urlWithId, category, { withCredentials: true })
            );
            this.categoryDto.next(updatedCategory);
            return updatedCategory;
        } catch (error) {
            console.error('Erreur lors de la mise à jour de la catégorie :', error);
            throw error;
        }
    }
    

}