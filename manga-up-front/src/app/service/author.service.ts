import { AuthorProjections, CategoriesProjections } from './../type.d';
import { HttpClient, HttpHeaders, } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, lastValueFrom, Observable } from 'rxjs';
import { GenreProjections, GenreProjection, GenreDto } from '../type';


@Injectable({
    providedIn: 'root'
})


export class AuthorService {
    url = "/api/authors/pagination";


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

}