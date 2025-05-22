import { Component, Inject, OnInit } from '@angular/core';
import { GenreProjection, GenreWithMangas } from '../../type';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { GenreService } from '../../service/genre.service';
import { CardComponent } from "../../components/card/card.component";

@Component({
  selector: 'app-genre',
  imports: [CardComponent, RouterModule],
  standalone: true,
  templateUrl: './genre.component.html',
  styleUrl: './genre.component.scss'
})
export class GenreComponent implements OnInit {
  id: string | null = null; 
  idOfUrl!: number; 
  genre: GenreWithMangas | null = null;


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
      this.genre =data;
    })


  }
}
