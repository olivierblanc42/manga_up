import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CategoriesComponent } from './categories.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { CategoryService } from '../../service/category.service';
import { BehaviorSubject, of, throwError } from 'rxjs';
import { CategoriesProjections } from '../../type';
import { ActivatedRoute } from '@angular/router';

describe('CategoriesComponent', () => {
  let component: CategoriesComponent;
  let fixture: ComponentFixture<CategoriesComponent>;
  let categoryService: Partial<CategoryService>;
  let mockCategorySubject: BehaviorSubject<CategoriesProjections | null>;

  // Données par défaut pour les tests
  const defaultCategories: CategoriesProjections = {
    content: [
      {
        id: 13,
        label: 'Naruto',
        description: 'Un manga Shonen populaire',
        createdAt: new Date('2025-04-17T15:43:41'),
        url: 'test',
      },
      {
        id: 14,
        label: 'Bleach',
        description: "Un manga avec des combats d'épées",
        createdAt: new Date('2025-04-17T15:43:41'),
        url: 'test',
      },
    ],
    size: 8,
    totalElements: 2,
    totalPages: 1,
  };

  beforeEach(async () => {
    // Initialisation du BehaviorSubject pour simuler l'observable categoriesProjections
    mockCategorySubject = new BehaviorSubject<CategoriesProjections | null>(null);

    // Mock partiel du service CategoryService
    categoryService = {
      categoriesProjections: mockCategorySubject,
      currentCategoriesProjection: of(defaultCategories),
      getAllCategoriesWithPagination: jasmine.createSpy('getAllCategoriesWithPagination').and.returnValue(of()),
    };

    // Configuration du module de test
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
                get: (key: string) => '1',
              },
            },
          },
        },
      ],
    }).compileComponents();

    // Création du composant et initialisation
    fixture = TestBed.createComponent(CategoriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    // Vérifie que le composant est créé correctement
    expect(component).toBeTruthy();
  });

  it('should fetch category on initialization', () => {
    // Vérifie que la méthode getAllCategoriesWithPagination a été appelée lors de l'initialisation
    expect(categoryService.getAllCategoriesWithPagination).toHaveBeenCalled();
  });

  it('should have categories populated from observable', () => {
    // Simule l'arrivée de données dans le BehaviorSubject
    mockCategorySubject.next(defaultCategories);
    fixture.detectChanges();

    // Vérifie que la propriété categories du composant a bien été mise à jour
    expect(component.categories).toEqual(defaultCategories);
  });

  it('should display categories with correct data', () => {
    // Assigne manuellement des catégories au composant
    component.categories = defaultCategories;
    fixture.detectChanges();

    // Vérifie que les cartes affichées correspondent bien aux catégories
    const cards = fixture.nativeElement.querySelectorAll('ui-card');
    expect(cards.length).toBe(2);

    // Vérifie que les titres affichés sont corrects
    const titles = fixture.nativeElement.querySelectorAll('.card-genre__title');
    expect(titles[0].textContent).toContain('Naruto');
    expect(titles[1].textContent).toContain('Bleach');
  });

  it('should handle empty category list', async () => {
    // Données avec liste vide
    const emptyCategories: CategoriesProjections = {
      content: [],
      size: 0,
      totalElements: 0,
      totalPages: 0,
    };

    // Réinitialisation du TestBed pour tester le cas d'une liste vide
    await TestBed.resetTestingModule().configureTestingModule({
      imports: [CategoriesComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: CategoryService,
          useValue: {
            categoriesProjections: new BehaviorSubject<CategoriesProjections | null>(null),
            currentCategoriesProjection: of(emptyCategories),
            getAllCategoriesWithPagination: jasmine.createSpy().and.returnValue(of()),
          },
        },
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({ id: '1' }),
            snapshot: {
              paramMap: {
                get: () => '1',
              },
            },
          },
        },
      ],
    }).compileComponents();

    // Création du composant dans ce nouveau contexte
    fixture = TestBed.createComponent(CategoriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    // Vérifie que la liste des catégories est bien vide dans le composant
    expect(component.categories?.content?.length).toBe(0);

    // Vérifie que rien n'est affiché dans l'UI
    const cards = fixture.nativeElement.querySelectorAll('ui-card');
    expect(cards.length).toBe(0);
  });

  it('should handle error from getAllCategoriesWithPagination', () => {
    // Simule une erreur lors de l'appel à getAllCategoriesWithPagination
    (categoryService.getAllCategoriesWithPagination as jasmine.Spy).and.returnValue(
      throwError(() => new Error('Erreur'))
    );

    // Appel manuel de ngOnInit pour déclencher la méthode
    component.ngOnInit();
    fixture.detectChanges();

    // Vérifie que la méthode a bien été appelée
    expect(categoryService.getAllCategoriesWithPagination).toHaveBeenCalled();
  });
});
