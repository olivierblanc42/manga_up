import { ActivatedRoute, RouterModule } from '@angular/router';
import { MangaBaseProjection, MangaBaseProjections } from '../../type';
import { SearchService } from './../../service/search.service';
import { Component, OnInit } from '@angular/core';
import { CardComponent } from "../../components/card/card.component";
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [CardComponent, RouterModule, NgClass],
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})




export class SearchComponent implements OnInit {

  mangas: MangaBaseProjections | null = null;
  content: MangaBaseProjection[] = [];
  pages!: number[];
  currentPage: number = 0;
  lastPage!: number;
  selectedLetter = 'a'





  constructor(
    private searchService: SearchService,
    private route: ActivatedRoute

  ) {
    this.currentPage = 0;
  }


  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const query = params.get('query') || 'a';
      this.selectedLetter = query;
      this.currentPage = 0;
      this.fetchMangas();
    });

    if (this.mangas?.content?.length) {
      this.content = this.mangas.content;
    } else {
      this.content = [];
    }

    this.searchService.currentmangasSearch.subscribe((data) => {
      this.mangas = data;
      this.content = data?.content ?? [];
      if (data) {
        this.pages = this.convertNumberToArray(data.totalPages);
        this.lastPage = data.totalPages;
      }
    });
  }

  fetchMangas(): void {
    this.searchService.getMangas(this.selectedLetter, this.currentPage);
  }
  convertNumberToArray(size: number) {
    return Array.from({ length: size }, (_, i) => i);
  }

  pageMangas(page: number) {
    this.currentPage = page;
    this.searchService.getMangas(this.selectedLetter, page);
  }

  pagePrevious() {
    if (this.currentPage > 0) {
      this.pageMangas(this.currentPage - 1);
    }
  }

  pageNext() {
    if (this.currentPage < this.lastPage - 1) {
      this.pageMangas(this.currentPage + 1);
    }
  }
}
