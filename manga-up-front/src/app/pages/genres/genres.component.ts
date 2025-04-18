import { Component, OnInit } from '@angular/core';
import { GenreProjections } from '../../type';
import { GenreService } from '../../service/genre.service';
import { CardComponent } from '../../components/card/card.component';

@Component({
  selector: 'app-genres',
  imports: [CardComponent],
  standalone: true,
  templateUrl: './genres.component.html',
  styleUrl: './genres.component.scss'
})
export class GenresComponent implements OnInit {

  genres: GenreProjections | null = null;

 constructor(
    private genreService : GenreService,
    
  ) { }

  ngOnInit(): void {
    this.genreService.getAllGenreWithPagination();

   this.genreService.currentGenresProjectionPaginations.subscribe((data)=>{
    this.genres = data;
     console.log("Genres récupérés :", this.genres);
   })

  }


}
