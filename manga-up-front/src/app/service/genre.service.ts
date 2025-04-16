import { HttpClient, HttpHeaders, } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, lastValueFrom, Observable } from 'rxjs';
import { GenreProjections, GenreProjection } from '../type';


@Injectable({
    providedIn: 'root'
})


export class GenreService {
    url = "/api/genres";
    urlFour = "api/genres/four";

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
   
    genreFour = new BehaviorSubject< GenreProjection[] >([]);
    currentGenreFour = this.genreFour.asObservable();



    getFourGenre() {
        console.log('Request to fetch four genres is being sent...');
        lastValueFrom(this.http.get<GenreProjection[]>(this.urlFour))
            .then((r) => {
                console.log('Data received from backend:', r); 
                if (!r) return;
                this.genreFour.next(r);
            })
            .catch((err) => {
                console.error('Erreur lors de la récupération des genres :', err);
            });
    }
}
