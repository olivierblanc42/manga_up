import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, firstValueFrom } from "rxjs";
import { environment } from "../../environments/environment.prod";

@Injectable({
    providedIn: 'root',
})
export class FavorisService {
    private apiUrl = `${environment.apiUrl}api/users`;

    constructor(private http: HttpClient) { }

    private favorite = new BehaviorSubject<boolean>(false);
    currentFavorite = this.favorite.asObservable();

    // Ajouter un manga aux favoris
    addFavorite(mangaId: number) {
        return this.http.post(`${this.apiUrl}/manga/${mangaId}`, {});
    }

    // Supprimer un manga des favoris
    deleteFavoris(mangaId: number) {
        return this.http.delete(`${this.apiUrl}/manga/${mangaId}`);
    }

    // Vérifier si un manga est en favori
    async isFavorite(mangaId: number) {
        try {
            const isFav = await firstValueFrom(
                this.http.get<boolean>(`${this.apiUrl}/manga/${mangaId}/is-favorite`, { withCredentials: true })
            );
            this.favorite.next(isFav);
            console.log('Manga favori :', isFav);
        } catch (err: any) {
            if (err.status === 403) {
                console.warn('Accès interdit : vous devez être connecté pour ajouter en favoris.');
                this.favorite.next(false); 
            } else {
                console.error('Erreur lors de la vérification du favori :', err.message || err);
            }
        }
    }
    

}
