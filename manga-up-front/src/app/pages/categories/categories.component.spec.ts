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
  let mockCategoriesSubject: BehaviorSubject<CategoriesProjections | null>;

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
    mockCategoriesSubject = new BehaviorSubject<CategoriesProjections | null>(null);

    categoryService = {
      categoriesProjections: mockCategoriesSubject,
      currentCategoriesProjection: mockCategoriesSubject.asObservable(),
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
                get: () => '1',
              },
            },
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CategoriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch categories on init', () => {
    expect(categoryService.getAllCategoriesWithPagination).toHaveBeenCalled();
  });

  it('should set categories from observable', () => {
    mockCategoriesSubject.next(defaultCategories);
    fixture.detectChanges();
    expect(component.categories).toEqual(defaultCategories);
  });

  it('should display categories with correct data', () => {
    component.categories = defaultCategories;
    fixture.detectChanges();

    const cards = fixture.nativeElement.querySelectorAll('ui-card');
    expect(cards.length).toBe(2);

    const titles = fixture.nativeElement.querySelectorAll('.card-genre__title');
    expect(titles[0].textContent).toContain('Naruto');
    expect(titles[1].textContent).toContain('Bleach');
  });

  it('should handle empty category list', async () => {
    const emptyCategories: CategoriesProjections = {
      content: [],
      size: 0,
      totalElements: 0,
      totalPages: 0,
    };

    mockCategoriesSubject.next(emptyCategories);
    fixture.detectChanges();

    expect(component.categories?.content.length).toBe(0);

    const cards = fixture.nativeElement.querySelectorAll('ui-card');
    expect(cards.length).toBe(0);
  });

  it('should handle null categories gracefully', () => {
    mockCategoriesSubject.next(null);
    fixture.detectChanges();
    expect(component.categories).toBeNull();
  });

  it('should handle error from getAllCategoriesWithPagination', () => {
    (categoryService.getAllCategoriesWithPagination as jasmine.Spy).and.returnValue(
      throwError(() => new Error('Erreur'))
    );

    component.ngOnInit();
    fixture.detectChanges();

    expect(categoryService.getAllCategoriesWithPagination).toHaveBeenCalled();
  });
});
