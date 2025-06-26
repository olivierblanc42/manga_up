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

    const img: HTMLImageElement | null = fixture.nativeElement.querySelector('.image');
    expect(img).not.toBeNull();
    expect(img?.src).toBe('https://i.postimg.cc/brcT8vY2/apr-s-la-pluie-resultat.webp');
  });
  

  it('should have the correct title', () => {
    const titleElement = fixture.nativeElement.querySelector('h1');
    expect(titleElement.textContent).toBe('Genres');
  });

  it('should display pagination buttons and call the right methods', () => {
    spyOn(component, 'pagePrevious');
    spyOn(component, 'pageNext');
    spyOn(component, 'pageGenres');

    component.pages = [0, 1, 2];
    component.currentPage = 0;
    component.lastPage = 3;

    fixture.detectChanges();

    const buttons = fixture.nativeElement.querySelectorAll('button');
    expect(buttons.length).toBe(5); 

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
