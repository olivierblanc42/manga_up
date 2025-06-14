import { Component, OnInit } from '@angular/core';
import { CategoryWithMangas } from '../../../type';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CategoryService } from '../../../service/category.service';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { noHtmlTagsValidator, urlValidator } from '../../../validator';

@Component({
  selector: 'app-category-admin',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss']
})
export class CategoryAdminComponent implements OnInit {

  idOfUrl!: number;
  category: CategoryWithMangas | null = null;
  categoryForm!: FormGroup;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private categoryService: CategoryService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.initForm();

    const id = this.activatedRoute.snapshot.paramMap.get('id');
    if (id) {
      this.idOfUrl = parseInt(id);
      if (!isNaN(this.idOfUrl)) {
        this.loadCategory(this.idOfUrl);
      }
    }

    this.categoryService.currentcategoriesWithManga.subscribe(data => {
      this.category = data;
      if (this.category) {
        this.categoryForm.patchValue({
          label: this.category.label,
          description: this.category.description,
          url: this.category.url
        });
      }
      console.log('category', this.category);
    });
  }

  initForm(): void {
    this.categoryForm = this.fb.group({
      label: ['', [Validators.required, noHtmlTagsValidator, Validators.maxLength(100)]],
      description: ['', [Validators.required, noHtmlTagsValidator, Validators.maxLength(500)]],
      url: ['', [Validators.required, urlValidator]]
    });
  }

  loadCategory(id: number): void {
    this.categoryService.getCategory(id);
  }

  async onSubmit() {
    if (this.categoryForm.valid) {
      const updatedCategory = {
        id: this.idOfUrl,
        ...this.categoryForm.value
      };

      try {
        console.log(updatedCategory);



        await this.categoryService.updateCategory(updatedCategory);
        alert('Catégorie mise à jour avec succès !');
        this.router.navigate(['/admin/categoriesAdmin']); // ou une autre route de retour
      } catch (error) {
        console.error('Erreur lors de la mise à jour', error);
        alert('Erreur lors de la mise à jour');
      }
    } else {
      this.categoryForm.markAllAsTouched();
    }
  }
}
