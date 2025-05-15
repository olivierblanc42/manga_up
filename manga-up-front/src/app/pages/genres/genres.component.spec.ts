import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GenresComponent } from './genres.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { GenreService } from '../../service/genre.service';
import { GenreProjections } from '../../type';
import { BehaviorSubject, of, throwError } from 'rxjs';
import { ActivatedRoute } from '@angular/router';

describe('GenresComponent', () => {
  let component: GenresComponent;
  let fixture: ComponentFixture<GenresComponent>;
  let genreService: {
    genresProjectionPaginations: BehaviorSubject<GenreProjections | null>,
    currentGenresProjectionPaginations: BehaviorSubject<GenreProjections | null>,
    getAllGenreWithPagination: jasmine.Spy
  };

  // Données de test par défaut
  const mockGenres: GenreProjections = {
    content: [
      {
        id: 12,
        label: 'Romance',
        createdAt: new Date('2025-04-17T15:43:41'),
        url: 'https://i.postimg.cc/brcT8vY2/apr-s-la-pluie-resultat.webp'
      }
    ],
    size: 8,
    totalElements: 1,
    totalPages: 1
  };

  beforeEach(async () => {
    genreService = {
      genresProjectionPaginations: new BehaviorSubject<GenreProjections | null>(null),
      currentGenresProjectionPaginations: new BehaviorSubject<GenreProjections | null>(mockGenres),
      getAllGenreWithPagination: jasmine.createSpy('getAllGenreWithPagination').and.returnValue(of())
    };

    await TestBed.configureTestingModule({
      imports: [GenresComponent],
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

    fixture = TestBed.createComponent(GenresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // Vérifie que le composant est bien créé
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Vérifie que la récupération des genres est bien appelée à l'initialisation
  it('should fetch genres on init', () => {
    expect(genreService.getAllGenreWithPagination).toHaveBeenCalled();
  });

  // Vérifie que les genres sont bien affectés via l'observable
  it('should set genres from observable', () => {
    genreService.currentGenresProjectionPaginations.next(mockGenres);
    fixture.detectChanges();
    expect(component.genres).toEqual(mockGenres);
  });

  // Vérifie que les images s'affichent correctement
  it('should display genres images correctly', () => {
    component.genres = mockGenres;
    fixture.detectChanges();
    const img = fixture.nativeElement.querySelector('.image');
    expect(img.src).toBe('https://i.postimg.cc/brcT8vY2/apr-s-la-pluie-resultat.webp');
  });

  // Vérifie le titre de la page
  it('should have the correct title', () => {
    const titleElement = fixture.nativeElement.querySelector('h1');
    expect(titleElement.textContent).toBe('Genres');
  });

  // Vérifie l'affichage de la pagination et les méthodes appelées
  it('should display pagination buttons and call the right methods', () => {
    spyOn(component, 'pagePrevious');
    spyOn(component, 'pageNext');
    spyOn(component, 'pageGenres');

    component.pages = [0, 1, 2];
    component.currentPage = 0;
    component.lastPage = 3;

    fixture.detectChanges();

    const buttons = fixture.nativeElement.querySelectorAll('button');
    expect(buttons.length).toBe(5); // Previous + 3 pages + Next

    expect(buttons[1].textContent.trim()).toBe('1');
    expect(buttons[2].textContent.trim()).toBe('2');
    expect(buttons[3].textContent.trim()).toBe('3');

    buttons[0].click();
    expect(component.pagePrevious).toHaveBeenCalled();

    buttons[2].click();
    expect(component.pageGenres).toHaveBeenCalledWith(1);

    buttons[4].click();
    expect(component.pageNext).toHaveBeenCalled();
  });

  // Vérifie que l'absence de données ne casse pas le composant
  it('should handle null currentGenresProjectionPaginations gracefully', () => {
    genreService.currentGenresProjectionPaginations.next(null);
    fixture.detectChanges();
    expect(component.genres).toBeNull();
  });

  // Vérifie que le composant gère correctement une erreur du service
  it('should handle error from getAllGenreWithPagination', () => {
    genreService.getAllGenreWithPagination.and.returnValue(
      throwError(() => new Error('Erreur'))
    );
    component.ngOnInit();
    fixture.detectChanges();
    expect(genreService.getAllGenreWithPagination).toHaveBeenCalled();
  });
});
