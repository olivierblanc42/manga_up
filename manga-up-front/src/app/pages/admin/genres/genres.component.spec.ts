import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GenresAdminComponent } from './genres.component';
import { GenreService } from '../../../service/genre.service';
import { GenreProjections } from '../../../type';
import { BehaviorSubject, of, throwError } from 'rxjs';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';

describe('GenresAdminComponent', () => {
  let component: GenresAdminComponent;
  let fixture: ComponentFixture<GenresAdminComponent>;
  let genreService: Partial<GenreService>;
  let mockGenresSubject: BehaviorSubject<GenreProjections | null>;

  const mockGenres: GenreProjections = {
    content: [
      {
        id: 12,
        label: 'Romance',
        createdAt: new Date('2025-04-17T15:43:41'),
        description:"sdfdvdvd",
        url: 'https://i.postimg.cc/brcT8vY2/apr-s-la-pluie-resultat.webp'
      }
    ],
    size: 8,
    totalElements: 1,
    totalPages: 1
  };

  beforeEach(async () => {
    mockGenresSubject = new BehaviorSubject<GenreProjections | null>(null);

    genreService = {
      genresProjectionPaginations: mockGenresSubject,
      currentGenresProjectionPaginations: mockGenresSubject.asObservable(),
      getAllGenreWithPagination: jasmine.createSpy('getAllGenreWithPagination').and.returnValue(of(mockGenres))
    };

    await TestBed.configureTestingModule({
      imports: [GenresAdminComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: GenreService, useValue: genreService },
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({ id: '1' }),
            snapshot: { paramMap: { get: () => '1' } }
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(GenresAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch genres on init', () => {
    expect(genreService.getAllGenreWithPagination).toHaveBeenCalled();
  });

  it('should set genres from observable', () => {
    mockGenresSubject.next(mockGenres);
    fixture.detectChanges();
    expect(component.genres).toEqual(mockGenres);
  });

  it('should display genres images correctly', () => {
    mockGenresSubject.next(mockGenres);
    fixture.detectChanges();
    const img = fixture.nativeElement.querySelectorAll('.image');
    expect(img[0].src).toBe('https://i.postimg.cc/brcT8vY2/apr-s-la-pluie-resultat.webp');
  });

  

  it('should handle null currentGenresProjectionPaginations gracefully', () => {
    mockGenresSubject.next(null);
    fixture.detectChanges();
    expect(component.genres).toBeNull();
  });

  it('should handle error from getAllGenreWithPagination', () => {
    (genreService.getAllGenreWithPagination as jasmine.Spy).and.returnValue(
      throwError(() => new Error('Erreur'))
    );

    component.ngOnInit();
    fixture.detectChanges();

    expect(genreService.getAllGenreWithPagination).toHaveBeenCalled();
  });
});
