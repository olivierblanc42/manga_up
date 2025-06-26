import { Component, OnInit } from '@angular/core';
import { GenreProjections } from '../../../type';
import { GenreService } from '../../../service/genre.service';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { noHtmlTagsValidator, urlValidator } from '../../../validator';

@Component({
  selector: 'app-genres',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  standalone: true,
  templateUrl: './genres.component.html',
  styleUrl: './genres.component.scss'
})
export class GenresAdminComponent implements OnInit {

  genres: GenreProjections | null = null;
  pages!: number[];
  lastPage!: number;
  currentPage!: number;
    genreForm!: FormGroup;
  isModalOpen = false;
 constructor(
    private genreService : GenreService,
     private fb: FormBuilder
    
 ) {
   this.currentPage = 0;
 }

  ngOnInit(): void {
    this.initForm();
    this.genreService.getAllGenreWithPagination();
    this.genreService.currentGenresProjectionPaginations.subscribe((data) => {
      this.genres = data;
      this.pages = this.convertNumberToArray(this.genres?.totalPages!)
      this.lastPage = this.genres?.totalPages!;
      console.log("Genres récupérés :", this.genres);
    })
 
  }
  convertNumberToArray(size: number) {
    const array = new Array<number>(size);
    for (let i = 0; i < array.length; i++) {
      array[i] = i;
    }
    return array;
  }
  pageGenres(page: number) {
    console.log("dans pageGenres page : ", page);
    this.currentPage = page;
    console.log("dans pageGenres currentPage : ", this.currentPage);
    this.genreService.getAllGenreWithPagination(page);
  }

  pagePrevious() {
    console.log("dans pagePrevious currentPage : ", this.currentPage);
    if (this.currentPage > 0) {
      this.pageGenres(this.currentPage - 1);
    }
  }
  pageNext() {
    console.log("dans pageNext currentPage : ", this.currentPage);
    if (this.currentPage < this.lastPage - 1) {
      this.pageGenres(this.currentPage + 1);
    }
  }

  initForm(): void {
    this.genreForm = this.fb.group({
          label: ['', [Validators.required, noHtmlTagsValidator, Validators.maxLength(100)]],
          description: ['', [Validators.required, noHtmlTagsValidator, Validators.maxLength(500)]],
          url: ['', [Validators.required, urlValidator]]
        });
  }

  openModal() {
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
    this.genreForm.reset();
  }
  async createGenre() {
    if (this.genreForm.valid) {
      const newGen = {
        ...this.genreForm.value,
        createdAt: new Date().toISOString()
      };

      try {
        await this.genreService.addGenre(newGen);
        await this.loadGenres(); 
        this.closeModal();
      } catch (error) {
        console.error('Erreur lors de la création', error);
      }
    } else {
      this.genreForm.markAllAsTouched();
    }
  }



  loadGenres() {
    try {
      this.genreService.getAllGenreWithPagination();
      this.genreService.currentGenresProjectionPaginations.subscribe((data) => {
        this.genres = data;
        this.pages = this.convertNumberToArray(this.genres?.totalPages!)
        this.lastPage = this.genres?.totalPages!;
        console.log("Genres récupérés :", this.genres);
      })
    } catch (error) {
      console.error('Erreur lors du chargement des catégories', error);
    }
  }

  async deleteGenre(id: number) {
    const confirmed = confirm('Voulez-vous vraiment supprimer cet auteur ?');
    if (!confirmed) return;
    try {
      await this.genreService.deleteGenre(id);
      this.loadGenres();
    } catch (error) {
      console.error('Erreur lors de la suppression', error);
    }
  }
}
