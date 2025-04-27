import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CategoriesComponent } from './categories.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { CategoryService } from '../../service/category.service';
import { BehaviorSubject, of } from 'rxjs';
import { CategoriesProjections } from '../../type';
import { ActivatedRoute } from '@angular/router';

describe('CategoriesComponent', () => {
  let component: CategoriesComponent;
  let fixture: ComponentFixture<CategoriesComponent>;
  let categoryService: Partial<CategoryService>;

  beforeEach(async () => {
    categoryService = {
      categoriesProjections: new BehaviorSubject<CategoriesProjections | null>(null),
      currentCategoriesProjection: of<CategoriesProjections>({
        content: [
          {
            id: 13,
            label: "Naruto",
            description: "Un manga Shonen populaire",
            createdAt: new Date("2025-04-17T15:43:41"),
            mangas: [
              {
                id: 1,
                title: "Naruto - Manga",
                pictures: [
                  { id: 1, url: "https://example.com/naruto.jpg" }
                ]
              }
            ]
          },
          {
            id: 14,
            label: "Bleach",
            description: "Un manga avec des combats d'épées",
            createdAt: new Date("2025-04-17T15:43:41"),
            mangas: [
              {
                id: 2,
                title: "Bleach - Manga",
                pictures: [
                  { id: 2, url: "https://example.com/bleach.jpg" }
                ]
              }
            ]
          }
        ],
        size: 8,
        totalElements: 2,
        totalPages: 1
      }),
      getAllCategoriesWithPagination: jasmine.createSpy('getAllCategoriesWithPagination').and.returnValue(of()),

    };

    await TestBed.configureTestingModule({
      imports: [CategoriesComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: CategoryService, useValue: categoryService },
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({ id: '1' }),
            snapshot: {
              paramMap: {
                get: (key: string) => '1'
              }
            }
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CategoriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should fetch category on initialization', () => {

    expect(categoryService.getAllCategoriesWithPagination).toHaveBeenCalled();
  });



  it('should have categories populated from observable', () => {

    // Mettre à jour la valeur du BehaviorSubject pour simuler l'arrivée de données
    categoryService.categoriesProjections?.next({
      content: [
        {
          id: 13,
          label: "Naruto",
          description: "Un manga Shonen populaire",
          createdAt: new Date("2025-04-17T15:43:41"),
          mangas: [
            { id: 1, title: "Naruto - Manga", pictures: [{ id: 1, url: "https://example.com/naruto.jpg" }] }
          ]
        },
        {
          id: 14,
          label: "Bleach",
          description: "Un manga avec des combats d'épées",
          createdAt: new Date("2025-04-17T15:43:41"),
          mangas: [
            { id: 2, title: "Bleach - Manga", pictures: [{ id: 2, url: "https://example.com/bleach.jpg" }] }
          ]
        }
      ],
      size: 8,
      totalElements: 2,
      totalPages: 1
    });
    // Mettre à jour la vue du composant
    fixture.detectChanges(); 

    // Vérifier que les catégories sont bien peuplées
    expect(component.categories).toEqual({
      content: [
        {
          id: 13,
          label: "Naruto",
          description: "Un manga Shonen populaire",
          createdAt: new Date("2025-04-17T15:43:41"),
          mangas: [
            { id: 1, title: "Naruto - Manga", pictures: [{ id: 1, url: "https://example.com/naruto.jpg" }] }
          ]
        },
        {
          id: 14,
          label: "Bleach",
          description: "Un manga avec des combats d'épées",
          createdAt: new Date("2025-04-17T15:43:41"),
          mangas: [
            { id: 2, title: "Bleach - Manga", pictures: [{ id: 2, url: "https://example.com/bleach.jpg" }] }
          ]
        }
      ],
      size: 8,
      totalElements: 2,
      totalPages: 1
    });
  });



  it('should create', () => {
    expect(component).toBeTruthy();
  });


});
