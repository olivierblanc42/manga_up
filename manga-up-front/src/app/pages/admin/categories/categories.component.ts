import { Component, OnInit } from '@angular/core';
import { CategoriesProjections } from '../../../type';
import { CategoryService } from '../../../service/category.service';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.scss']
})
export class CategoriesAdminComponent implements OnInit {
  categories: CategoriesProjections | null = null;
  pages!: number[];
  lastPage!: number;
  currentPage!: number;

  isModalOpen = false;

  categoryForm: FormGroup;

  constructor(
    public categoryService: CategoryService,
    private fb: FormBuilder
  ) {
    this.currentPage = 0;
    // Initialisation du formulaire réactif
    this.categoryForm = this.fb.group({
      label: ['', Validators.required],
      description: ['', Validators.required],
      url: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    console.log("in ngOnInit");

    this.categoryService.getAllCategoriesWithPagination();

    this.categoryService.currentCategoriesProjection.subscribe((data) => {
      this.categories = data;
      this.pages = this.convertNumberToArray(this.categories?.totalPages!);
      this.lastPage = this.categories?.totalPages!;
      console.log("categories : ", this.categories);
    });
  }

  convertNumberToArray(size: number) {
    const array = new Array<number>(size);
    for (let i = 0; i < array.length; i++) {
      array[i] = i;
    }
    return array;
  }

  pageCategories(page: number) {
    console.log("dans pageGenres page : ", page);
    this.currentPage = page;
    console.log("dans pageGenres currentPage : ", this.currentPage);
    this.categoryService.getAllCategoriesWithPagination(page);
  }

  pagePrevious() {
    console.log("dans pagePrevious currentPage : ", this.currentPage);
    if (this.currentPage > 0) {
      this.pageCategories(this.currentPage - 1);
    }
  }

  pageNext() {
    console.log("dans pageNext currentPage : ", this.currentPage);
    if (this.currentPage < this.lastPage - 1) {
      this.pageCategories(this.currentPage + 1);
    }
  }

  openModal() {
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
    this.categoryForm.reset();
  }

  async createCategory() {
    if (this.categoryForm.valid) {
      const newCat = {
        ...this.categoryForm.value,
        createdAt: new Date().toISOString()
      };

      try {
        await this.categoryService.addCategory(newCat);
        await this.loadCategories(); 
        this.closeModal();
      } catch (error) {
        console.error('Erreur lors de la création', error);
      }
    } else {
      this.categoryForm.markAllAsTouched();
    }
  }

  loadCategories() {
    try {
      this.categoryService.getAllCategoriesWithPagination();
      this.categoryService.currentCategoriesProjection.subscribe((data) => {
        this.categories = data;
        this.pages = this.convertNumberToArray(this.categories?.totalPages!);
        this.lastPage = this.categories?.totalPages!;
      });
    } catch (error) {
      console.error('Erreur lors du chargement des catégories', error);
    }
  }
  
  async deleteCategory(id: number) {
    const confirmed = confirm('Voulez-vous vraiment supprimer cet auteur ?');
    if (!confirmed) return;
    try {
      await this.categoryService.deleteCategory(id);
      this.loadCategories();
    } catch (error) {
      console.error('Erreur lors de la suppression', error);
    }
  }
}
