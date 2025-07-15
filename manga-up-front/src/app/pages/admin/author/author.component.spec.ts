import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthorAdminComponent } from './author.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { AuthorService } from '../../../service/author.service';
import { AuthorWithMangas } from '../../../type';
import { BehaviorSubject, of } from 'rxjs';
import { ActivatedRoute } from '@angular/router';

describe('AuthorComponent', () => {
  let component: AuthorAdminComponent;
  let fixture: ComponentFixture<AuthorAdminComponent>;
  let authorService: Partial<AuthorService>;
  beforeEach(async () => {

    authorService = {
      authorOneProjection: new BehaviorSubject<AuthorWithMangas | null>(null),
      currentAuthorOneProjection: of<AuthorWithMangas>(
        {
          birthdate: new Date("2025-04-17T15:43:41"),
          createdAt: new Date("2025-04-17T15:43:41"),
          description: "Créateur de Naruto",
          firstname: "Masashi",
          genre: "Masculin",
          id: 123,
          lastname: "Kishimoto",
          url: "https://example.com/naruto.jpg",
          mangasWithImages: {
            content: [
              {
                id_mangas: 1,
                title: "Naruto - Manga",
                picture: "https://example.com/naruto.jpg",
                pictureId: "1", authors: [{
                  firstname: "string",
                  lastname: "string",
                }

                ]
              },
              {
                id_mangas: 2,
                title: "Bleach - Manga",
                picture: "https://example.com/naruto.jpg",
                pictureId: "2", authors: [{
                  firstname: "string",
                  lastname: "string",
                }

                ]

              }
            ],
            size: 8,
            totalElements: 2,
            totalPages: 1
          }
        }),
      getAuthor: jasmine.createSpy('getAuthor').and.returnValue(of({
        birthdate: new Date("2025-04-17T15:43:41"),
        createdAt: new Date("2025-04-17T15:43:41"),
        description: "Créateur de Naruto",
        firstname: "Masashi",
        genre: "Masculin",
        id: 123,
        lastname: "Kishimoto",
        url: "https://example.com/naruto.jpg",
        mangasWithImages: {
          content: [
            {
              id: 1,
              title: "Naruto - Manga",
              pictureUrl: "https://example.com/naruto.jpg",
              pictureId: "1",
            },
            {
              id: 2,
              title: "Bleach - Manga",
              pictureUrl: "https://example.com/naruto.jpg",
              pictureId: "2",
            }
          ],
          size: 8,
          totalElements: 2,
          totalPages: 1
        }
      })),
    }



    await TestBed.configureTestingModule({
      imports: [AuthorAdminComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: AuthorService, useValue: authorService },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: (key: string) => '123',
              },
            },
            params: of({ id: '123' }),
          },
        }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AuthorAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // it('should fetch author on initialization', async () => {
  // });


  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
