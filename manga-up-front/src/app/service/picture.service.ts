import { HttpClient, HttpHeaders, } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, firstValueFrom, lastValueFrom, Observable } from 'rxjs';
import { Manga, MangaDto, MangaDtoRandom, MangaOne,MangaPaginations,MangaProjection,MangaProjections, PictureDto, PictureProjection, PictureProjections } from '../type';


@Injectable({
    providedIn: 'root'
})

export class PictureService{

    url = "${environment.apiUrl}/api/picture"


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


    picturesProjection = new BehaviorSubject<PictureProjection | null>(null);
    currentpicturesProjection = this.picturesProjection.asObservable();

    picturesPagination = new BehaviorSubject<PictureProjections | null>(null);
    currentpicturePagination = this.picturesPagination.asObservable();

    picturesDto = new BehaviorSubject<PictureDto | null>(null);


async getPicture(id: number){
            try {
                const r = await lastValueFrom(this.http.get<PictureProjection>(`${this.url}/${id}`, {
                    withCredentials: true
                }));
                if (!r) return;
                this.picturesProjection.next(r);
                console.log('Image récupérés avec succès :', r);
            } catch (err) {
                console.error('Erreur lors de la récupération de l\'image :', err);
        } 
}

    async getAllpictures(page: number = 0) {
        try {
            const r = await lastValueFrom(this.http.get<PictureProjections>(`${this.url}?page=${page}`, {
                withCredentials: true
            }));
            if (!r) return;
            this.picturesPagination.next(r);
        } catch (err) {
            console.error('Erreur lors de la récupération des images :', err);
        }
    }


    async deletePicture(id: number): Promise<PictureDto>{
             const response = await firstValueFrom(
                this.http.delete<PictureDto>(`${this.url}/${id}`,{
                    withCredentials: true
                })
             );
             this.picturesDto.next(response)
             return response
    }

    async updatePicture(picture: PictureDto): Promise<PictureDto> {
        try {
            const urlWithId = `${this.url}/${picture.id}`; 
            const updatedPicture = await lastValueFrom(
                this.http.put<PictureDto>(urlWithId, picture, { withCredentials: true })
            );
            this.picturesDto.next(updatedPicture);
            return updatedPicture;
        } catch (error) {
            console.error('Erreur lors de la mise à jour de l\'image :', error);
            throw error;
        }
    }


}