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
  let genreService: Partial<GenreService>
  beforeEach(async () => {
    genreService = {
      genresProjectionPaginations: new BehaviorSubject<GenreProjections | null>(null),
      currentGenresProjectionPaginations: of<GenreProjections>({
        content: [
          {
            "url": "https://i.postimg.cc/brcT8vY2/apr-s-la-pluie-resultat.webp",
            "id": 12,
            "label": "Romance",
            "createdAt": new Date("2025-04-17T15:43:41")
          },
          {
            "url": "https://i.postimg.cc/B6tyttGY/aventure-resultat.webp",
            "id": 11,
            "label": "Aventure",
            "createdAt": new Date("2025-04-17T15:43:41")
          },
          {
            "url": "https://i.postimg.cc/VkMVb2pw/isekai.webp",
            "id": 10,
            "label": "Isekai",
            "createdAt": new Date("2025-04-17T15:43:41")
          },
          {
            "url": "https://i.postimg.cc/150FnHBT/comedie.webp",
            "id": 9,
            "label": "Comedie",
            "createdAt": new Date("2025-04-17T15:43:41")
          },
          {
            "url": "https://i.postimg.cc/HxBJsXKF/Issho-Senkin-manga-visual.webp",
            "id": 8,
            "label": "Combat",
            "createdAt": new Date("2025-04-17T15:43:41")
          }
        ],
        size: 8,
        totalElements: 2,
        totalPages: 1
      }),

      getAllGenreWithPagination: jasmine.createSpy('getAllGenreWithPagination').and.returnValue(of()),
    };



    await TestBed.configureTestingModule({
      imports: [GenresComponent,],
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
    })
      .compileComponents();

    fixture = TestBed.createComponent(GenresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
  it('should fetch genres on init', () => {
    expect(genreService.getAllGenreWithPagination).toHaveBeenCalled();
  })


  it('should have genres populated from observable', () => {
    // Mettre à jour la valeur du BehaviorSubject pour simuler l'arrivée de données
    genreService.genresProjectionPaginations?.next({
      content: [
        {
          "url": "https://i.postimg.cc/brcT8vY2/apr-s-la-pluie-resultat.webp",
          "id": 12,
          "label": "Romance",
          "createdAt": new Date("2025-04-17T15:43:41")
        },
        {
          "url": "https://i.postimg.cc/B6tyttGY/aventure-resultat.webp",
          "id": 11,
          "label": "Aventure",
          "createdAt": new Date("2025-04-17T15:43:41")
        },
        {
          "url": "https://i.postimg.cc/VkMVb2pw/isekai.webp",
          "id": 10,
          "label": "Isekai",
          "createdAt": new Date("2025-04-17T15:43:41")
        },
        {
          "url": "https://i.postimg.cc/150FnHBT/comedie.webp",
          "id": 9,
          "label": "Comedie",
          "createdAt": new Date("2025-04-17T15:43:41")
        },
        {
          "url": "https://i.postimg.cc/HxBJsXKF/Issho-Senkin-manga-visual.webp",
          "id": 8,
          "label": "Combat",
          "createdAt": new Date("2025-04-17T15:43:41")
        }
      ],
      size: 8,
      totalElements: 2,
      totalPages: 1
    });

    // Mettre à jour la vue du composant
    fixture.detectChanges();

    // Vérifier que les catégories sont bien peuplées
    expect(component.genres).toEqual({
      content: [
        {
          "url": "https://i.postimg.cc/brcT8vY2/apr-s-la-pluie-resultat.webp",
          "id": 12,
          "label": "Romance",
          "createdAt": new Date("2025-04-17T15:43:41")
        },
        {
          "url": "https://i.postimg.cc/B6tyttGY/aventure-resultat.webp",
          "id": 11,
          "label": "Aventure",
          "createdAt": new Date("2025-04-17T15:43:41")
        },
        {
          "url": "https://i.postimg.cc/VkMVb2pw/isekai.webp",
          "id": 10,
          "label": "Isekai",
          "createdAt": new Date("2025-04-17T15:43:41")
        },
        {
          "url": "https://i.postimg.cc/150FnHBT/comedie.webp",
          "id": 9,
          "label": "Comedie",
          "createdAt": new Date("2025-04-17T15:43:41")
        },
        {
          "url": "https://i.postimg.cc/HxBJsXKF/Issho-Senkin-manga-visual.webp",
          "id": 8,
          "label": "Combat",
          "createdAt": new Date("2025-04-17T15:43:41")
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
