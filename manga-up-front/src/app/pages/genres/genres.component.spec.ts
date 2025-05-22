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
  let genreService: Partial<GenreService>;
  let mockGenresSubject: BehaviorSubject<GenreProjections | null>;

  // genre de données qu'on va utiliser pour tester
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
    // On fait un BehaviorSubject qu’on peut “pousser” pour simuler le service
    mockGenresSubject = new BehaviorSubject<GenreProjections | null>(null);

    // Mock du service, on fait simple
    genreService = {
      genresProjectionPaginations: mockGenresSubject,
      currentGenresProjectionPaginations: mockGenresSubject.asObservable(),
      getAllGenreWithPagination: jasmine.createSpy('getAllGenreWithPagination').and.returnValue(of())
    };

    // Configuration de base du TestBed
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

  // Juste pour être sûr que le composant existe
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // On vérifie que l’appel au service est bien fait au lancement
  it('should fetch genres on init', () => {
    expect(genreService.getAllGenreWithPagination).toHaveBeenCalled();
  });

  // Là on envoie des données au composant via l'observable et on vérifie qu’il les prend bien
  it('should set genres from observable', () => {
    mockGenresSubject.next(mockGenres);
    fixture.detectChanges();
    expect(component.genres).toEqual(mockGenres);
  });

  // Test rapide pour voir si l’image s’affiche avec la bonne URL
  it('should display genres images correctly', () => {
    component.genres = mockGenres;
    fixture.detectChanges();
    const img = fixture.nativeElement.querySelector('.image');
    expect(img.src).toBe('https://i.postimg.cc/brcT8vY2/apr-s-la-pluie-resultat.webp');
  });

  // Juste pour vérifier que le titre est bien “Genres”
  it('should have the correct title', () => {
    const titleElement = fixture.nativeElement.querySelector('h1');
    expect(titleElement.textContent).toBe('Genres');
  });

  // On teste les boutons de pagination, voir si ça appelle bien les méthodes du composant
  it('should display pagination buttons and call the right methods', () => {
    spyOn(component, 'pagePrevious');
    spyOn(component, 'pageNext');
    spyOn(component, 'pageGenres');

    // On donne quelques pages à afficher
    component.pages = [0, 1, 2];
    component.currentPage = 0;
    component.lastPage = 3;

    fixture.detectChanges();

    const buttons = fixture.nativeElement.querySelectorAll('button');
    expect(buttons.length).toBe(5); // bouton précédent + 3 pages + bouton suivant

    expect(buttons[1].textContent.trim()).toBe('1');
    expect(buttons[2].textContent.trim()).toBe('2');
    expect(buttons[3].textContent.trim()).toBe('3');

    // Cliquer sur précédent
    buttons[0].click();
    expect(component.pagePrevious).toHaveBeenCalled();

    // Cliquer sur la page 2
    buttons[2].click();
    expect(component.pageGenres).toHaveBeenCalledWith(1);

    // Cliquer sur suivant
    buttons[4].click();
    expect(component.pageNext).toHaveBeenCalled();
  });

  // Test si jamais l’observable balance un null, on ne casse pas tout
  it('should handle null currentGenresProjectionPaginations gracefully', () => {
    mockGenresSubject.next(null);
    fixture.detectChanges();
    expect(component.genres).toBeNull();
  });

  // Vérifie que si le service renvoie une erreur, ça plante pas (au moins on attrape l’erreur)
  it('should handle error from getAllGenreWithPagination', () => {
    (genreService.getAllGenreWithPagination as jasmine.Spy).and.returnValue(
      throwError(() => new Error('Erreur'))
    );

    component.ngOnInit();
    fixture.detectChanges();

    expect(genreService.getAllGenreWithPagination).toHaveBeenCalled();
  });
});
