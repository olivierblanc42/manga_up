import { AuthorDto, AuthorProjection, AuthorProjections, AuthorWithMangas, CategoriesProjections, MangasWithImages, MangaWithImages } from './../type.d';
import { HttpClient, HttpHeaders, } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject,  firstValueFrom, Observable } from 'rxjs';
import { GenreProjections, GenreProjection, GenreDto } from '../type';
import { environment } from '../../environments/environment.prod';


@Injectable({
    providedIn: 'root'
})


export class AuthorService {
    url = `${environment.apiUrl}api/authors/`;
    urlPagination = `${environment.apiUrl}api/public/authors/pagination`;
    urlOne = `${environment.apiUrl}api/public/author/`
    urlAdd = `${environment.apiUrl}api/authors/add`
    



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
            const r = await firstValueFrom(this.http.get<AuthorProjections>(`${this.urlPagination}?page=${page}`));
            if (!r) return;
            this.authorProjection.next(r);
            console.log('Genres récupérés avec succès :', r);
        } catch (err) {
            console.error('Erreur lors de la récupération des genres :', err);
        }
    }


    async getAuthor(id: number, page: number = 0) {
        try {
            const response = await firstValueFrom(this.http.get<{ author: AuthorWithMangas, mangas: MangasWithImages }>(`${this.urlOne}${id}/mangas?page=${page}`));

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
      


    async updateAuthor(author: AuthorDto): Promise<AuthorDto> {
            try {
                const urlWithId = `${this.url}/${author.id}`; 
                const updatedCategory = await firstValueFrom(
                    this.http.put<AuthorDto>(urlWithId, author, { withCredentials: true })
                );
                this.authorDto.next(updatedCategory);
                return updatedCategory;
            } catch (error) {
                console.error('Erreur lors de la mise à jour de l\'author :', error);
                throw error;
            }
        }

}