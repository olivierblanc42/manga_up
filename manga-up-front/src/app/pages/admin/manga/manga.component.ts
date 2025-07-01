import { Manga, Picture } from './../../../type.d';
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AuthorProjections, CategoriesProjections, GenreProjections, GenreWithMangas, MangaProjection } from '../../../type';
import { GenreService } from '../../../service/genre.service';
import { MangaService } from '../../../service/manga.service';
import { noHtmlTagsValidator } from '../../../validator';
import { CategoryService } from '../../../service/category.service';
import { AuthorService } from '../../../service/author.service';
import { NgSelectModule } from '@ng-select/ng-select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-manga',
  imports: [CommonModule, ReactiveFormsModule, RouterModule, NgSelectModule, MatProgressSpinnerModule],
  standalone: true,
  templateUrl: './manga.component.html',
  styleUrl: './manga.component.scss'
})
export class MangaComponent {
  idOfUrl!: number;
  genreForm!: FormGroup;
  genre: GenreWithMangas | null = null;
  manga: MangaProjection | null = null;
  categories: CategoriesProjections | null = null;
  genres: GenreProjections | null = null;
  authors: AuthorProjections | null = null;
  mangaForm!: FormGroup;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private genreService: GenreService,
    private mangaService: MangaService,
    private fb: FormBuilder,
        private categoryService: CategoryService,
                private authorService : AuthorService,
        
    
  ) { }

  ngOnInit(): void {
    this.initForm();

    const id = this.activatedRoute.snapshot.paramMap.get('id');
    if (id) {
      this.idOfUrl = parseInt(id);
      if (!isNaN(this.idOfUrl)) {
        this.mangaService.getManga(this.idOfUrl);
      }
    }
    this.categoryService.getAllCategoriesWithPagination();
    this.categoryService.currentCategoriesProjection.subscribe((data) => {
      this.categories = data;
      console.log("categories : ", this.categories);
    });

    this.authorService.getAllAuthorWithPagination();
    this.authorService.currentauthorProjection.subscribe((data) => {
      this.authors = data;
      console.log("Genres récupérés :", this.authors);
    })
    this.genreService.getAllGenreWithPagination();
    this.genreService.currentGenresProjectionPaginations.subscribe((data) => {
      this.genres = data;
    })

    this.mangaService.currentMangaProjection.subscribe((data) => {
      this.manga = data;
      console.log("mangas", this.manga);
      if (this.manga) {
        this.mangaForm.patchValue({
          title: this.manga.title || '',
          subtitle: this.manga.subtitle || '',
          releaseDate: this.manga.releaseDate || null,
          summary: this.manga.summary || '',
          price: this.manga.price ?? 1,
          inStock: this.manga.inStock ?? false,
          active: this.manga.active ?? false,
          idCategories: this.manga.idCategories,
          genres: this.manga.genres?.map(g => g.id) ?? [],
          authors: this.manga.authors?.map(a => a.id) ?? [],
        });

        const picturesArray = this.mangaForm.get('pictures') as FormArray;
        picturesArray.clear();

  
        if (this.manga.pictures && Array.isArray(this.manga.pictures)) {
          this.manga.pictures.forEach((picture) => {
            picturesArray.push(this.fb.group({
              id: [picture.id],
              url: [picture.url, Validators.required],
              isMain: [picture.isMain ]
            }));
          });
        }
      
      }
    });
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
      genres: [[], [Validators.required, ]],
      authors: [[], [Validators.required,]],
      pictures: this.fb.array([])
    });
  }

  get pictures(): FormArray {
    return this.mangaForm.get('pictures') as FormArray;
  }

  addImage(url: string) {
    if (url && url.trim() !== '') {
      this.pictures.push(this.fb.control(url.trim()));
    }
  }

  deleteImage(index: number) {
    this.pictures.removeAt(index);
  }

  async onSubmit() {
    if (this.mangaForm.valid) {
      const formValue = this.mangaForm.value;
      const updatedManga = {
        ...formValue,
        id: this.idOfUrl,
        idCategories: formValue.idCategories,
        releaseDate: formValue.releaseDate ? new Date(formValue.releaseDate).toISOString() : null,  
          
      };

      try {
        await this.mangaService.updateManga(updatedManga);
        this.router.navigate(['/admin/mangasAdmin']); 
      } catch (error) {
        console.error('Erreur lors de la mise à jour', error);
      }
    } else {
      this.mangaForm.markAllAsTouched();
    }
  }

  setAsMain(index: number) {
    this.pictures.controls.forEach((ctrl, i) => {
      const fg = ctrl as FormGroup;
      fg.get('isMain')!.setValue(i === index);
    });
  }

}
