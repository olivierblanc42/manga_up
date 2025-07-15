import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CategoriesAdminComponent } from './categories.component';
import { BehaviorSubject, of, throwError } from 'rxjs';
import { CategoriesProjections } from '../../../type';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { CategoryService } from '../../../service/category.service';
import { ActivatedRoute } from '@angular/router';

describe('CategoriesAdminComponent', () => {
  let component: CategoriesAdminComponent;
  let fixture: ComponentFixture<CategoriesAdminComponent>;
  let categoryService: {
    categoriesProjections: BehaviorSubject<CategoriesProjections | null>,
    currentCategoriesProjection: BehaviorSubject<CategoriesProjections | null>,
    getAllCategoriesWithPagination: jasmine.Spy,
  };

  // Données simulées pour les tests
  const mockCategories: CategoriesProjections = {
    content: [
      {
        id: 13,
        label: 'Naruto',
        description: 'Un manga Shonen populaire',
        createdAt: new Date('2025-04-17T15:43:41'),
        url: 'https://example.com/naruto.jpg',
      },
      {
        id: 14,
        label: 'Bleach',
        description: "Un manga avec des combats d'épées",
        createdAt: new Date('2025-04-17T15:43:41'),
        url: 'https://example.com/bleach.jpg',
      }
    ],
    size: 8,
    totalElements: 2,
    totalPages: 1,
  };

  beforeEach(async () => {
    // Simulation du service avec BehaviorSubjects
    categoryService = {
      categoriesProjections: new BehaviorSubject<CategoriesProjections | null>(null),
      currentCategoriesProjection: new BehaviorSubject<CategoriesProjections | null>(mockCategories),
      getAllCategoriesWithPagination: jasmine.createSpy('getAllCategoriesWithPagination').and.returnValue(of()),
    };

    await TestBed.configureTestingModule({
      imports: [CategoriesAdminComponent],
      providers: [
        provideHttpClientTesting(),
        { provide: CategoryService, useValue: categoryService },
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({ id: '1' }),
            snapshot: { paramMap: { get: (key: string) => '1' } }
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CategoriesAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // Vérifie que le composant se crée sans erreur
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Vérifie que les données sont chargées à l'initialisation
  it('should fetch categories on initialization', () => {
    expect(categoryService.getAllCategoriesWithPagination).toHaveBeenCalled();
  });

  // Vérifie que les catégories sont bien affectées au composant
  it('should set categoriesProjection on initialization', () => {
    categoryService.currentCategoriesProjection.next(mockCategories);
    fixture.detectChanges();
    expect(component.categories).toEqual(mockCategories);
  });

  // Vérifie l'affichage des images dans la carte
  it('should display categories images correctly', () => {
    component.categories = {
      content: [
        {
          id: 13,
          label: 'Naruto',
          description: 'Un manga Shonen populaire',
          createdAt: new Date(),
          url: 'https://example.com/naruto.jpg',
        }
      ],
      size: 8,
      totalElements: 1,
      totalPages: 1
    };
    fixture.detectChanges();
    const img = fixture.nativeElement.querySelector('.image');
    expect(img.src).toBe('https://example.com/naruto.jpg');
  });

  // Vérifie la pagination : boutons, clics, méthodes appelées
  // it('should display pagination buttons and call the right methods', () => {
  //   spyOn(component, 'pagePrevious');
  //   spyOn(component, 'pageNext');
  //   spyOn(component, 'pageCategories');

  //   component.pages = [0, 1, 2];
  //   component.currentPage = 0;
  //   component.lastPage = 3;

  //   fixture.detectChanges();

  //   const buttons = fixture.nativeElement.querySelectorAll('button');
  //   expect(buttons.length).toBe(5);

  //   expect(buttons[1].textContent.trim()).toBe('1');
  //   expect(buttons[2].textContent.trim()).toBe('2');
  //   expect(buttons[3].textContent.trim()).toBe('3');

  //   buttons[0].click();
  //   expect(component.pagePrevious).toHaveBeenCalled();

  //   buttons[2].click();
  //   expect(component.pageCategories).toHaveBeenCalledWith(1);

  //   buttons[4].click();
  //   expect(component.pageNext).toHaveBeenCalled();
  // });

  // Vérifie le titre affiché
  it('should have the correct title', () => {
    const titleElement = fixture.nativeElement.querySelector('h1');
    expect(titleElement.textContent).toBe('Catégories:');
  });

  // Gère une erreur émise par le service
  it('should handle error from getAllCategoriesWithPagination', () => {
    categoryService.getAllCategoriesWithPagination.and.returnValue(
      throwError(() => new Error('Erreur'))
    );
    component.ngOnInit();
    fixture.detectChanges();
    expect(categoryService.getAllCategoriesWithPagination).toHaveBeenCalled();
  });

  // Gère le cas où aucune catégorie n’est retournée
  it('should handle null currentCategoriesProjection gracefully', () => {
    categoryService.currentCategoriesProjection.next(null);
    fixture.detectChanges();
    expect(component.categories).toBeNull();
  });
});
