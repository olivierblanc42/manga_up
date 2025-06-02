import { Component, OnInit } from '@angular/core';
import { MangaService } from '../../../service/manga.service';
import { CategoriesProjections, CategoryDto, CategoryWithMangas, GenreProjections, MangaPaginations } from '../../../type';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { noHtmlTagsValidator, requiredTrueValidator } from '../../../validator';
import { CategoryService } from '../../../service/category.service';
import { GenreService } from '../../../service/genre.service';

@Component({
  selector: 'app-mangas',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  standalone: true,
  templateUrl: './mangas.component.html',
  styleUrl: './mangas.component.scss'
})
export class MangasAdminComponent implements OnInit {
  mangaPagination(mangaPagination: any) {
    throw new Error("Method not implemented.");
  }

  mangas: MangaPaginations | null = null;
  pages!: number[];
  lastPage!: number;
  currentPage!: number;
  isModalOpen = false;
  mangaForm!: FormGroup;
  categories: CategoriesProjections | null = null;
  genres: GenreProjections | null = null;

  constructor(
    private mangaservice: MangaService,
    public categoryService: CategoryService,
        private genreService : GenreService,
    
    private fb: FormBuilder


  ) {
    this.currentPage = 0;

  }
  ngOnInit(): void {
    this.initForm();
    this.categoryService.getAllCategoriesWithPagination();
    this.mangaForm.get('genreIds')!.valueChanges.subscribe(val => {
      console.log('Genres sélectionnés (avec valueChanges):', val);
    });
    this.categoryService.currentCategoriesProjection.subscribe((data) => {
      this.categories = data;
      this.pages = this.convertNumberToArray(this.categories?.totalPages!);
      this.lastPage = this.categories?.totalPages!;
      console.log("categories : ", this.categories);
    });


    this.genreService.getAllGenreWithPagination();
    this.genreService.currentGenresProjectionPaginations.subscribe((data) => {
      this.genres = data;
      this.pages = this.convertNumberToArray(this.genres?.totalPages!)
      this.lastPage = this.genres?.totalPages!;
      console.log("Genres récupérés :", this.genres);
    })


    this.mangaservice.getMangas();
    this.mangaservice.currentMangaPaginations.subscribe((data) => {
      this.mangas = data;
      console.log("manga récupérés :", this.mangas);
      this.pages = this.convertNumberToArray(this.mangas?.totalPages!)
      this.lastPage = this.mangas?.totalPages!;
    })
  }
  convertNumberToArray(size: number) {
    const array = new Array<number>(size);
    for (let i = 0; i < array.length; i++) {
      array[i] = i;
    }
    return array;
  }
  pageMangas(page: number) {
    console.log("dans pageMangas page : ", page);
    this.currentPage = page;
    console.log("dans pageMangas currentPage : ", this.currentPage);
    this.mangaservice.getMangas(page);
  }

  pagePrevious() {
    console.log("dans pagePrevious currentPage : ", this.currentPage);
    if (this.currentPage > 0) {
      this.pageMangas(this.currentPage - 1);
    }
  }
  pageNext() {
    console.log("dans pageNext currentPage : ", this.currentPage);
    if (this.currentPage < this.lastPage - 1) {
      this.pageMangas(this.currentPage + 1);
    }
  }

  async deleteManga(id: number) {
    const confirmed = confirm('Voulez-vous vraiment supprimer ce manga ?');
    if (!confirmed) return;
    try {
      await this.mangaservice.deleteManga(id);
      // this.loadGenres();
    } catch (error) {
      console.error('Erreur lors de la suppression', error);
    }
  }
  initForm(): void {

    this.mangaForm = this.fb.group({
      title: ['', [Validators.required, noHtmlTagsValidator, Validators.maxLength(100)]],
      subtitle: ['', [ noHtmlTagsValidator, Validators.maxLength(100)]],
      releaseDate: [null, Validators.required],
      summary: ['', [Validators.required, noHtmlTagsValidator, Validators.maxLength(500)]],
      price: [1, [Validators.required, Validators.min(1)]],
      isStock: [true],
      isActive: [true], 
      categoryId: [null, Validators.required],   
      genreIds: [[], Validators.required],
      authorsIds: [[], Validators.required]
    });

  }
  openModal() {
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
    this.mangaForm.reset();
  }
  async createManga() {

  }


  
  
  onGenreSelectChange() {
    console.log('Genres sélectionnés :', this.mangaForm.get('genreIds')!.value);
  }

}
