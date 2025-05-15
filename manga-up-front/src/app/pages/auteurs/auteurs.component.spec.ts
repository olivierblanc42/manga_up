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


  it('should fetch authors on initialization', () => {
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


  it('should display authors images correctly', () => {
    // On prépare le composant avec un auteur pour tester l'affichage de l'image
    component.authors = {
      content: [
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
    };

    fixture.detectChanges();

    const img = fixture.nativeElement.querySelector('.image');
    // Vérifie que la source de l'image correspond bien à l'URL attendue
    expect(img.src).toBe('https://example.com/bleach.jpg');
  });

  it('should create', () => {
    // Simple test de création du composant
    expect(component).toBeTruthy();
  });



  it('should display pagination buttons and call the right methods', () => {
    spyOn(component, 'pagePrevious');
    spyOn(component, 'pageNext');
    spyOn(component, 'pageAuthor');

    component.pages = [0, 1, 2];
    component.currentPage = 0;
    component.lastPage = 3;

    fixture.detectChanges();

    const buttons = fixture.nativeElement.querySelectorAll('button');
    expect(buttons.length).toBe(5); 

    // Vérifie que le texte des boutons de pages est correct
    expect(buttons[1].textContent.trim()).toBe('1');
    expect(buttons[2].textContent.trim()).toBe('2');
    expect(buttons[3].textContent.trim()).toBe('3');

    // Clique sur Previous
    buttons[0].click();
    expect(component.pagePrevious).toHaveBeenCalled();

    // Clique sur page 1 (qui est en fait index 0 dans le tableau)
    buttons[2].click(); // page 2
    expect(component.pageAuthor).toHaveBeenCalledWith(1);

    // Clique sur Next
    buttons[4].click();
    expect(component.pageNext).toHaveBeenCalled();
  });
  


 
});
