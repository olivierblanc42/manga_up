import { AuthorDto, AuthorProjection, AuthorProjections, AuthorWithMangas, CategoriesProjections, MangasWithImages, MangaWithImages } from './../type.d';
import { HttpClient, HttpHeaders, } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, firstValueFrom, lastValueFrom, Observable } from 'rxjs';
import { GenreProjections, GenreProjection, GenreDto } from '../type';


@Injectable({
    providedIn: 'root'
})


export class AuthorService {
    url = "/api/authors/";
    urlPagination = "/api/public/authors/pagination";
    urlOne = "/api/public/author/"
    urlAdd = "/api/authors/add"
    



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


    authorProjection = new BehaviorSubject<AuthorProjections | null>(null)
    currentauthorProjection = this.authorProjection.asObservable();

    authorOneProjection = new BehaviorSubject<AuthorWithMangas | null>(null);


    currentAuthorOneProjection = this.authorOneProjection.asObservable();
    authorDto = new BehaviorSubject<AuthorDto | null>(null);


    async getAllAuthorWithPagination(page: number = 0) {
        try {
            const r = await lastValueFrom(this.http.get<AuthorProjections>(`${this.urlPagination}?page=${page}`));
            if (!r) return;
            this.authorProjection.next(r);
            console.log('Genres récupérés avec succès :', r);
        } catch (err) {
            console.error('Erreur lors de la récupération des genres :', err);
        }
    }


    async getAuthor(id: number, page: number = 0) {
        try {
            const response = await lastValueFrom(this.http.get<{ author: AuthorWithMangas, mangas: MangasWithImages }>(`${this.urlOne}${id}/mangas?page=${page}`));

            if (response) {
                const authorWithMangas: AuthorWithMangas = {
                    ...response.author, 
                    mangasWithImages: response.mangas 
                };

                this.authorOneProjection.next(authorWithMangas);

                console.log('Auteur récupérés avec succès :', authorWithMangas);
            }
        } catch (err) {
            console.error('Erreur lors de la récupération de l\'auteur :', err);
        }
    }


    async addAuthor(author: AuthorDto): Promise<AuthorDto> {
        const response = await firstValueFrom(
            this.http.post<AuthorDto>(this.urlAdd, author,{ withCredentials: true } )
        );

        this.authorDto.next(response);
        return response; 
    }


    async deleteAuthor(id: number): Promise<AuthorDto> {
        const response = await firstValueFrom(
            this.http.delete<AuthorDto>(`${this.url}/${id}`, {
                withCredentials: true
            })
        );

        this.authorDto.next(response);
        return response;
    }
      

}