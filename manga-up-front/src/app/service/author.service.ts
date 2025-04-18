import { AuthorProjection, AuthorProjections, CategoriesProjections } from './../type.d';
import { HttpClient, HttpHeaders, } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, lastValueFrom, Observable } from 'rxjs';
import { GenreProjections, GenreProjection, GenreDto } from '../type';


@Injectable({
    providedIn: 'root'
})


export class AuthorService {
    url = "/api/authors/pagination";
    urlOne = "/api/authors/"

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

    authorOneProjection = new BehaviorSubject<AuthorProjection | null>(null)
    currentAuthorOneProjection = this.authorOneProjection.asObservable();


    async getAllAuthorWithPagination() {
        try {
            const r = await lastValueFrom(this.http.get<AuthorProjections>(this.url));
            if (!r) return;
            this.authorProjection.next(r);
            console.log('Genres récupérés avec succès :', r);
        } catch (err) {
            console.error('Erreur lors de la récupération des genres :', err);
        }
    }



    async getAuthor(id: number) {
        try {
            const r = await lastValueFrom(this.http.get<AuthorProjection>(`${this.urlOne}${id}`));
            if (!r) return;
            this.authorOneProjection.next(r);
            console.log('author récupérés avec succès :', r);
        } catch (err) {
            console.error('Erreur lors de la récupération de author :', err);
        }
    }


}