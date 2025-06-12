import {  MangaProjection } from './../../type.d';
import { Component, Inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MangaService } from './../../service/manga.service';
import { FavoriteButtonComponent } from "../../components/favorite-button/favorite-button.component";
import { CommonModule, DatePipe } from '@angular/common';

@Component({
  selector: 'app-manga',
  standalone: true,
  templateUrl: './manga.component.html',
  styleUrls: ['./manga.component.scss'],
  imports: [FavoriteButtonComponent, RouterModule, CommonModule],
  providers: [DatePipe]
})
export class MangaComponent implements OnInit {
  id: string | null = null; 
  idOfUrl!: number; 
  manga: MangaProjection | null = null;
  
  constructor(
    @Inject(ActivatedRoute) private activatedRoute: ActivatedRoute,
    @Inject(Router) private router: Router,
    private mangaService: MangaService,
    private datePipe: DatePipe
  ) { }

  ngOnInit(): void {
    const id = this.activatedRoute.snapshot.paramMap.get('id');
    if (id) {
      this.idOfUrl = parseInt(id);
      if (!isNaN(this.idOfUrl)) {
        this.mangaService.getManga(this.idOfUrl);
      }
    } 

    this.mangaService.currentMangaProjection.subscribe((data)=>{
      this.manga = data
    })
    
  }

  getYearFromDate(dateString?: string | Date): string {
    if (!dateString) {
      return '';
    }
    return this.datePipe.transform(dateString, 'yyyy') || '';
  }
  


}
