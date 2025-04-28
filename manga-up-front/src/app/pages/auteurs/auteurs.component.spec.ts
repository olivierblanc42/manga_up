import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuteursComponent } from './auteurs.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { AuthorProjections } from '../../type';
import { BehaviorSubject, of } from 'rxjs';
import { AuthorService } from '../../service/author.service';
import { ActivatedRoute } from '@angular/router';

describe('AuteursComponent', () => {
  let component: AuteursComponent;
  let fixture: ComponentFixture<AuteursComponent>;
  let authorService: Partial<AuthorService>;
  beforeEach(async () => {

    authorService = {
      authorProjection : new BehaviorSubject<AuthorProjections | null>(null),
      currentauthorProjection: of<AuthorProjections>({
        content: [
          {
            birthdate: new Date("2025-04-17T15:43:41"),
            createdAt: new Date("2025-04-17T15:43:41"),
            description: "Créateur de Naruto",
            firstname: "Masashi",
            genre: "Masculin",
            id: 1,
            lastname: "Kishimoto",
            url: "https://example.com/naruto.jpg"

          },
          {
            birthdate: new Date("1974-11-08T15:43:41"),
            createdAt: new Date("2025-04-17T15:43:41"),
            description: "Créateur de Bleach",
            firstname: "Tite",
            genre: "Masculin",
            id: 2,
            lastname: "Kubo",
            url: "https://example.com/bleach.jpg"

          }
        ],
        size : 8,
        totalElements : 2,
        totalPages : 1
      }),
      getAllAuthorWithPagination: jasmine.createSpy('getAllAuthorWithPagination').and.returnValue(of()),
    }

    await TestBed.configureTestingModule({
      imports: [AuteursComponent],
            providers: [
                    provideHttpClient(), 
                    provideHttpClientTesting(),
                            { provide: AuthorService, useValue: authorService },
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

    fixture = TestBed.createComponent(AuteursComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });


  it('should fetch category on initialization', () => {
    expect(authorService.getAllAuthorWithPagination).toHaveBeenCalled();
  });

  it('should set authorProjection on initialization', () => {
    authorService.authorProjection?.next({
      content: [
        {
          birthdate: new Date("2025-04-17T15:43:41"),
          createdAt: new Date("2025-04-17T15:43:41"),
          description: "Créateur de Naruto",
          firstname: "Masashi",
          genre: "Masculin",
          id: 1,
          lastname: "Kishimoto",
          url: "https://example.com/naruto.jpg"

        },
        {
          birthdate: new Date("1974-11-08T15:43:41"),
          createdAt: new Date("2025-04-17T15:43:41"),
          description: "Créateur de Bleach",
          firstname: "Tite",
          genre: "Masculin",
          id: 2,
          lastname: "Kubo",
          url: "https://example.com/bleach.jpg"

        }
      ],
      size: 8,
      totalElements: 2,
      totalPages: 1
    });
    fixture.detectChanges(); 
    expect(component.authors).toEqual({
      content: [
        {
          birthdate: new Date("2025-04-17T15:43:41"),
          createdAt: new Date("2025-04-17T15:43:41"),
          description: "Créateur de Naruto",
          firstname: "Masashi",
          genre: "Masculin",
          id: 1,
          lastname: "Kishimoto",
          url: "https://example.com/naruto.jpg"

        },
        {
          birthdate: new Date("1974-11-08T15:43:41"),
          createdAt: new Date("2025-04-17T15:43:41"),
          description: "Créateur de Bleach",
          firstname: "Tite",
          genre: "Masculin",
          id: 2,
          lastname: "Kubo",
          url: "https://example.com/bleach.jpg"

        }
      ],
      size: 8,
      totalElements: 2,
      totalPages: 1
    })
  });



  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
