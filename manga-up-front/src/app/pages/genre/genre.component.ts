import { Component, Inject, OnInit } from '@angular/core';
import { GenreProjection, GenreWithMangas } from '../../type';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { GenreService } from '../../service/genre.service';
import { CardComponent } from "../../components/card/card.component";
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-genre',
  imports: [CardComponent, RouterModule,MatProgressSpinnerModule, CommonModule],
  standalone: true,
  templateUrl: './genre.component.html',
  styleUrl: './genre.component.scss'
})
export class GenreComponent implements OnInit {
  id: string | null = null; 
  idOfUrl!: number; 
  genre: GenreWithMangas | null = null;
  isLoading = true;
  showEmptyMessage = false;

  constructor(
    @Inject(ActivatedRoute) private activatedRoute: ActivatedRoute,
    @Inject(Router) private router: Router,
    private genreService: GenreService) {

     }
  ngOnInit(): void {
    const id = this.activatedRoute.snapshot.paramMap.get('id');
    if (id) {
      this.idOfUrl = parseInt(id);
      if (!isNaN(this.idOfUrl)) {
        this.genreService.getGenreManga(this.idOfUrl);
      }
    }

    this.genreService.curentGenreSolo.subscribe((data)=>{
      if (!data) {
        this.isLoading = true;
        setTimeout(() => {
          if (!this.genre) {
            this.showEmptyMessage = true;
            this.isLoading = false;

          }
        }, 10000);
        return;
      } else {
        

        this.genre = data;
        console.log('Genre reçu :', this.genre);
        this.isLoading = false;
        this.showEmptyMessage = false;
      }
      
    })


  }
}
