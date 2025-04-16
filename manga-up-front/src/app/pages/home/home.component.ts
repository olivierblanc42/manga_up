import { Component,CUSTOM_ELEMENTS_SCHEMA, OnInit } from '@angular/core';
import { CardComponent } from '../../components/card/card.component';
import { GenreProjection } from '../../type';
import { GenreService } from '../../service/genre.service';


@Component({
  selector: 'app-home',
  imports: [CardComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  schemas : [CUSTOM_ELEMENTS_SCHEMA,],
  standalone: true
})

export class HomeComponent implements OnInit{

  genres!: GenreProjection[];


  constructor(
    private genreService : GenreService
  ) { }

  ngOnInit(): void {
    this.genreService.getFourGenre();
    this.genreService.currentGenreFour.subscribe((data) => {
      this.genres = data;
      console.log("Genres récupérés :", this.genres);
    });
    
  }
}
