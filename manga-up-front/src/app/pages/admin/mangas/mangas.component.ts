import { Component, OnInit } from '@angular/core';
import { MangaService } from '../../../service/manga.service';
import { AuthorProjections, CategoriesProjections, CategoryDto, CategoryWithMangas, GenreProjections, MangaPaginations } from '../../../type';
import { CommonModule } from '@angular/common';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { noHtmlTagsValidator, requiredTrueValidator } from '../../../validator';
import { CategoryService } from '../../../service/category.service';
import { GenreService } from '../../../service/genre.service';
import { NgSelectModule } from '@ng-select/ng-select';
import { AuthorService } from '../../../service/author.service';

@Component({
  selector: 'app-mangas',
  imports: [CommonModule, ReactiveFormsModule, RouterModule, NgSelectModule],
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
  authors: AuthorProjections | null = null;
  isLoadingManga = true;
  showEmptyMessage = false;
  constructor(
    private mangaservice: MangaService,
    public categoryService: CategoryService,
        private genreService : GenreService,
        private authorService : AuthorService,
    
    private fb: FormBuilder


  ) {
    this.currentPage = 0;

  }
  ngOnInit(): void {
    this.initForm();
    this.categoryService.getAllCategoriesWithPagination();
    this.mangaForm.get('genres')!.valueChanges.subscribe(val => {
      console.log('Genres sélectionnés (avec valueChanges):', val);
    });
    this.mangaForm.get('authors')!.valueChanges.subscribe(val => {
      console.log('Auteurs sélectionnés (avec valueChanges):', val);
    });


    this.categoryService.currentCategoriesProjection.subscribe((data) => {
      this.categories = data;
      this.pages = this.convertNumberToArray(this.categories?.totalPages!);
      this.lastPage = this.categories?.totalPages!;
      console.log("categories : ", this.categories);
    });

    this.authorService.getAllAuthorWithPagination();
    this.authorService.currentauthorProjection.subscribe((data) => {
      this.authors = data;
      console.log("Genres récupérés :", this.authors);
      this.pages = this.convertNumberToArray(this.authors?.totalPages!)
      this.lastPage = this.authors?.totalPages!;
    })
    this.genreService.getAllGenreWithPagination();
    this.genreService.currentGenresProjectionPaginations.subscribe((data) => {
      this.genres = data;
      this.pages = this.convertNumberToArray(this.genres?.totalPages!)
      this.lastPage = this.genres?.totalPages!;
      console.log("Genres récupérés :", this.genres);
    })


    this.mangaservice.getMangas();
    this.mangaservice.currentMangaPaginations.subscribe((data) => {
      if (!data) {
        this.mangas = null;

        this.isLoadingManga = true;
        setTimeout(() => {
          if (!this.mangas) {
            this.showEmptyMessage = true;
            this.isLoadingManga = false;
          }
        }, 10000);
        return;
      } else {
        this.mangas = data;
        this.isLoadingManga = false;
        this.showEmptyMessage = false;
      }
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
      console.log(id)
      await this.mangaservice.deleteManga(id);
       this.loadManga();
    } catch (error) {
      console.error('Erreur lors de la suppression', error);
    }
  }
  initForm(): void {
    this.mangaForm = this.fb.group({
      title: ['', [Validators.required, noHtmlTagsValidator, Validators.maxLength(100)]],
      subtitle: ['', [noHtmlTagsValidator, Validators.maxLength(100)]],
      releaseDate: [null, Validators.required],
      summary: ['', [Validators.required, noHtmlTagsValidator, Validators.maxLength(1000)]],
      price: [1, [Validators.required, Validators.min(1)]],
      inStock: [false],
      active: [false],
      idCategories: [null, Validators.required],
      genres: [[], [Validators.required, Validators.maxLength(1)]],   
      authors: [[], [Validators.required, Validators.maxLength(1)]],  
      pictures: this.fb.array([])
    });
    
  }
  openModal() {
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
    this.mangaForm.reset();
  }

  get pictures(): FormArray {
    return this.mangaForm.get('pictures') as FormArray;
  }

  addImage(url: string) {
    this.pictures.push(
      this.fb.group({
        
        url: [url, Validators.required],
        isMain: [false]
      })
    );
  }

  deleteImage(index: number) {
    this.pictures.removeAt(index);
  }

  setMainImage(index: number) {
    this.pictures.controls.forEach((control, i) =>
      control.patchValue({ isMain: i === index })
    );
  }


  async createManga() {
    console.log('createManga called', this.mangaForm.valid);

    if (this.mangaForm.valid) {
      const raw = this.mangaForm.value;

      const newGen = {
        ...raw,
        releaseDate: raw.releaseDate
          ? new Date(raw.releaseDate).toISOString()
          : null
      };

      try {
        await this.mangaservice.addManga(newGen);
        await this.loadManga();
        this.closeModal();
      } catch (error) {
        console.error('Erreur lors de la création', error);
      }
    } else {

      this.mangaForm.markAllAsTouched();
    }
  }


  loadManga() {
    try {
      this.mangaservice.getMangas();
      this.mangaservice.currentMangaPaginations.subscribe((data) => {
        this.mangas = data;
        console.log("manga récupérés :", this.mangas);
        this.pages = this.convertNumberToArray(this.mangas?.totalPages!)
        this.lastPage = this.mangas?.totalPages!;
      })
    } catch (error) {
      console.error('Erreur lors du chargement des mangas', error);
    }
  }
  


}
