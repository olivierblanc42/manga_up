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
            url: "test",
          },
          {
            id: 14,
            label: "Bleach",
            description: "Un manga avec des combats d'épées",
            createdAt: new Date("2025-04-17T15:43:41"),
            url: "test",
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
          url:"test"
     
        },
        {
          id: 14,
          label: "Bleach",
          description: "Un manga avec des combats d'épées",
          createdAt: new Date("2025-04-17T15:43:41"),
          url:"test"
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
          url: "test"

        },
        {
          id: 14,
          label: "Bleach",
          description: "Un manga avec des combats d'épées",
          createdAt: new Date("2025-04-17T15:43:41"),
          url: "test"

        }
      ],
      size: 8,
      totalElements: 2,
      totalPages: 1
    });
  });
  it('should display categories with correct data', () => {
    // Simuler l'arrivée des catégories
    component.categories = {
      content: [
        {
          id: 13,
          label: 'Naruto',
          description: 'Un manga Shonen populaire',
          createdAt: new Date(),
          url: "test"
        },
        {
          id: 14,
          label: 'Bleach',
          description: 'Un manga avec des combats d\'épées',
          createdAt: new Date(),
          url: "test"
        }
      ],
      size: 8,
      totalElements: 2,
      totalPages: 1
    };

    fixture.detectChanges();  // Mettre à jour la vue

    // Vérifier que les catégories sont bien affichées
    const cards = fixture.nativeElement.querySelectorAll('ui-card');
    expect(cards.length).toBe(2);  // On a 2 catégories, donc 2 cards

    const titles = fixture.nativeElement.querySelectorAll('.card-genre__title');
    expect(titles[0].textContent).toBe('Naruto');
    expect(titles[1].textContent).toBe('Bleach');
  });




  it('should create', () => {
    expect(component).toBeTruthy();
  });


});
