import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GenresComponent } from './genres.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { GenreService } from '../../service/genre.service';
import { GenreProjections } from '../../type';
import { BehaviorSubject, of } from 'rxjs';
import { ActivatedRoute } from '@angular/router';

describe('GenresComponent', () => {
  let component: GenresComponent;
  let fixture: ComponentFixture<GenresComponent>;
  let genreService: Partial<GenreService>;

  beforeEach(async () => {
    // Mock du service avec les observables nécessaires
    genreService = {
      genresProjectionPaginations: new BehaviorSubject<GenreProjections | null>(null),
      currentGenresProjectionPaginations: of({
        content: [
          {
            url: 'https://i.postimg.cc/brcT8vY2/apr-s-la-pluie-resultat.webp',
            id: 12,
            label: 'Romance',
            createdAt: new Date('2025-04-17T15:43:41')
          },
        ],
        size: 8,
        totalElements: 2,
        totalPages: 1
      }),
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
            snapshot: {
              paramMap: {
                get: (key: string) => '1'
              }
            }
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(GenresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should fetch genres on init', () => {
    // Vérifie que la méthode de récupération des genres est bien appelée au démarrage
    expect(genreService.getAllGenreWithPagination).toHaveBeenCalled();
  });

  it('should have genres populated from observable', () => {
    // Simule l’arrivée de données dans le BehaviorSubject pour tester la mise à jour du composant
    genreService.genresProjectionPaginations!.next({
      content: [
        {
          url: 'https://i.postimg.cc/brcT8vY2/apr-s-la-pluie-resultat.webp',
          id: 12,
          label: 'Romance',
          createdAt: new Date('2025-04-17T15:43:41')
        },
      ],
      size: 8,
      totalElements: 2,
      totalPages: 1
    });
    // On force Angular à détecter les changements
    fixture.detectChanges(); 

    // On s'assure que les genres dans le composant correspondent bien à ceux envoyés par le service
    expect(component.genres).toEqual({
      content: [
        {
          url: 'https://i.postimg.cc/brcT8vY2/apr-s-la-pluie-resultat.webp',
          id: 12,
          label: 'Romance',
          createdAt: new Date('2025-04-17T15:43:41')
        },
      ],
      size: 8,
      totalElements: 2,
      totalPages: 1
    });
  });

  it('should display category images correctly', () => {
    // On prépare le composant avec un genre pour tester l'affichage de l'image
    component.genres = {
      content: [
        {
          url: 'https://i.postimg.cc/brcT8vY2/apr-s-la-pluie-resultat.webp',
          id: 12,
          label: 'Romance',
          createdAt: new Date('2025-04-17T15:43:41')
        }
      ],
      size: 8,
      totalElements: 2,
      totalPages: 1
    };

    fixture.detectChanges();

    const img = fixture.nativeElement.querySelector('.image');
    // Vérifie que la source de l'image correspond bien à l'URL attendue
    expect(img.src).toBe('https://i.postimg.cc/brcT8vY2/apr-s-la-pluie-resultat.webp');
  });

  it('should create', () => {
    // Simple test de création du composant
    expect(component).toBeTruthy();
  });


//Simple Test pour le titre de la page 
   it('should have the correct title',()=>{
     const titleElement = fixture.nativeElement.querySelector('h1');
     expect(titleElement.textContent).toBe('Genres');
   });



   
});

