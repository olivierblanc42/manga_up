import { Component, Inject, OnInit } from '@angular/core';
import { GenreProjection } from '../../type';
import { ActivatedRoute, Router } from '@angular/router';
import { GenreService } from '../../service/genre.service';

@Component({
  selector: 'app-genre',
  imports: [],
  standalone: true,
  templateUrl: './genre.component.html',
  styleUrl: './genre.component.scss'
})
export class GenreComponent implements OnInit {
  id: string | null = null; 
  idOfUrl!: number; 
  genre: GenreProjection | null = null;


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
