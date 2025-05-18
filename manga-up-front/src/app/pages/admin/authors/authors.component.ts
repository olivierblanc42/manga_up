import { Component, OnInit } from '@angular/core';
import { AuthorProjections } from '../../../type';
import { AuthorService } from '../../../service/author.service';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-authors',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  standalone: true,
  templateUrl: './authors.component.html',
  styleUrl: './authors.component.scss'
})
export class AuthorsAdminComponent implements OnInit {
 pages!: number[];
  lastPage!: number;
  currentPage!: number;
  authors: AuthorProjections | null = null;
     authorForm: FormGroup;
    isModalOpen = false;
 constructor(
    private authorService : AuthorService,
         private fb: FormBuilder
    
 ) { this.currentPage = 0;
     this.authorForm = this.fb.group({
        lastname: ['', Validators.required],
        firstname: ['', Validators.required],
        genre: ['', Validators.required],
        description: ['', Validators.required],
        url: ['', Validators.required],
        birthdate: [null, Validators.required]
      })
  }

  ngOnInit(): void {

   this.authorService.getAllAuthorWithPagination();
   this.authorService.currentauthorProjection.subscribe((data)=>{
     this.authors = data;
     console.log("Genres récupérés :", this.authors);
     this.pages = this.convertNumberToArray(this.authors?.totalPages!)
     this.lastPage = this.authors?.totalPages!;
   })


  }
  convertNumberToArray(size: number) {
    const array = new Array<number>(size);
    for (let i = 0; i < array.length; i++) {
      array[i] = i;
    }
    return array;
  }
  pageAuthor(page: number) {
    console.log("dans pageGenres page : ", page);
    this.currentPage = page;
    console.log("dans pageGenres currentPage : ", this.currentPage);
    this.authorService.getAllAuthorWithPagination(page);
  }

  pagePrevious() {
    console.log("dans pagePrevious currentPage : ", this.currentPage);
    if (this.currentPage > 0) {
      this.pageAuthor(this.currentPage - 1);
    }
  }
  pageNext() {
    console.log("dans pageNext currentPage : ", this.currentPage);
    if (this.currentPage < this.lastPage - 1) {
      this.pageAuthor(this.currentPage + 1);
    }
  }


  openModal() {
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
    this.authorForm.reset();
  }

  async createAuthor() {
    if (this.authorForm.valid) {
      const newGen = {
        ...this.authorForm.value,
        createdAt: new Date().toISOString()
      };

      try {
        await this.authorService.addAuthor(newGen);
        await this.loadAuthors();
        this.closeModal();
      } catch (error) {
        console.error('Erreur lors de la création', error);
      }
    } else {
      this.authorForm.markAllAsTouched();
    }
  }

  loadAuthors() {
    try {
      this.authorService.getAllAuthorWithPagination();
      this.authorService.currentauthorProjection.subscribe((data) => {
        this.authors = data;
        console.log("Genres récupérés :", this.authors);
        this.pages = this.convertNumberToArray(this.authors?.totalPages!)
        this.lastPage = this.authors?.totalPages!;
      
      })
    } catch (error) {
      console.error('Erreur lors du chargement des catégories', error);
    }
  }

  async deleteAuthor(id: number) {
    const confirmed = confirm('Voulez-vous vraiment supprimer cet auteur ?');
    if (!confirmed) return;
    try {
      await this.authorService.deleteAuthor(id);
      this.loadAuthors(); 
    } catch (error) {
      console.error('Erreur lors de la suppression', error);
    }
  }
  

}
