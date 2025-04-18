import { MangaProjections } from './../../type.d';
import { MangaService } from './../../service/manga.service';
import { Component, OnInit } from '@angular/core';
import { CardComponent } from '../../components/card/card.component';

@Component({
  selector: 'app-mangas',
  imports: [],
  standalone: true,
  templateUrl: './mangas.component.html',
  styleUrl: './mangas.component.scss'
})
export class MangasComponent implements OnInit{

  mangas: MangaProjections | null = null;

 constructor(
    private mangaservice : MangaService,
    
  ) { }
  ngOnInit(): void { 
   
 

  }

}
