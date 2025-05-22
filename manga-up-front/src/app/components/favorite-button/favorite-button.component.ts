import { FavorisService } from './../../service/favoris.service';
import { TestBed } from '@angular/core/testing';
import { Component, Inject, OnInit } from '@angular/core';
import { MangaService } from '../../service/manga.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-favorite-button',
  imports: [],
  templateUrl: './favorite-button.component.html',
  styleUrl: './favorite-button.component.scss'
})
export class FavoriteButtonComponent implements OnInit{
  id: string | undefined ; 
  idOfUrl!: number; 
  isFavorite: boolean | undefined;
  constructor(private mangaService: MangaService,
    private favorisService: FavorisService,
      @Inject(ActivatedRoute) private activatedRoute: ActivatedRoute,
      @Inject(Router) private router: Router,) { }


  ngOnInit(): void {
    const id = this.activatedRoute.snapshot.paramMap.get('id');
    if (id) {
      this.idOfUrl = parseInt(id);
      this.favorisService.isFavorite(this.idOfUrl);

      this.favorisService.currentFavorite.subscribe(fav => {
        this.isFavorite = fav;
        console.log('Est-ce un favori ? =>', fav);
      });
    }
  }
      



  toggleFavorite() {
    if (this.isFavorite) {
      this.favorisService.deleteFavoris(this.idOfUrl).subscribe(() => {
        this.isFavorite = false;
        console.log(this.idOfUrl + " supprimé des favoris");
      });
    } else {
      this.favorisService.addFavorite(this.idOfUrl).subscribe(() => {
        this.isFavorite = true;
        console.log(this.idOfUrl + " ajouté aux favoris");
      });
    }
  }
  

}
