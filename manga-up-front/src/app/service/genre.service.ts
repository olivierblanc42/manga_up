import { HttpClient, HttpHeaders, } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, lastValueFrom, Observable } from 'rxjs';
import { GenreProjections, GenreProjection, GenreDto } from '../type';


@Injectable({
    providedIn: 'root'
})


export class GenreService {
    url = "/api/genres/pagination";
    urlFour = "api/genres/four";
    urlGenre= "/api/genres/"



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

    genresProjectionPaginations = new BehaviorSubject< GenreProjections | null>(null);
    currentGenresProjectionPaginations = this.genresProjectionPaginations.asObservable();
   
    genreFour = new BehaviorSubject<GenreDto[] >([]);
    currentGenreFour = this.genreFour.asObservable();

    genreSolo = new BehaviorSubject<GenreProjection | null>(null);
    curentGenreSolo = this.genreSolo.asObservable();

    async getAllGenreWithPagination() {
        try {
            const r = await lastValueFrom(this.http.get<GenreProjections>(this.url));
            if (!r) return;
            this.genresProjectionPaginations.next(r);
            console.log('Genres récupérés avec succès :', r);
        } catch (err) {
            console.error('Erreur lors de la récupération des genres :', err);
        }
    }


    getFourGenre() {
       // console.log('Request to fetch four genres is being sent...');
        lastValueFrom(this.http.get<GenreDto[]>(this.urlFour))
            .then((r) => {
                if (!r) return;
                this.genreFour.next(r);
            })
            .catch((err) => {
                console.error('Erreur lors de la récupération des genres :', err);
            });
    }


   async getGenreManga(id: number) {
        try {
            const r = await lastValueFrom(this.http.get<GenreProjection>(`${this.urlGenre}${id}`));
            if (!r) return;
            this.genreSolo.next(r);
            console.log('Genres récupérés avec succès :', r);
        } catch (err) {
            console.error('Erreur lors de la récupération des genres :', err);
        }
    }

    
}
