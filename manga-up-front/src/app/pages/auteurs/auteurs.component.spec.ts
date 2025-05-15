import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AuteursComponent } from './auteurs.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { AuthorProjections } from '../../type';
import { BehaviorSubject, of, throwError } from 'rxjs';
import { AuthorService } from '../../service/author.service';
import { ActivatedRoute } from '@angular/router';

describe('AuteursComponent', () => {
  let component: AuteursComponent;
  let fixture: ComponentFixture<AuteursComponent>;
  let authorService: Partial<AuthorService>;

  beforeEach(async () => {
    authorService = {
      authorProjection: new BehaviorSubject<AuthorProjections | null>(null),
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
        size: 8,
        totalElements: 2,
        totalPages: 1
      }),
      getAllAuthorWithPagination: jasmine.createSpy('getAllAuthorWithPagination').and.returnValue(of()),
    };

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
            snapshot: { paramMap: { get: (key: string) => '1' } }
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AuteursComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
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
    });
  });

  it('should display authors images correctly', () => {
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
    expect(img.src).toBe('https://example.com/bleach.jpg');
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

    expect(buttons[1].textContent.trim()).toBe('1');
    expect(buttons[2].textContent.trim()).toBe('2');
    expect(buttons[3].textContent.trim()).toBe('3');

    buttons[0].click();
    expect(component.pagePrevious).toHaveBeenCalled();

    buttons[2].click();
    expect(component.pageAuthor).toHaveBeenCalledWith(1);

    buttons[4].click();
    expect(component.pageNext).toHaveBeenCalled();
  });

  it('should have the correct title', () => {
    const titleElement = fixture.nativeElement.querySelector('h1');
    expect(titleElement.textContent).toBe('Auteurs');
  });

  it('should handle error from getAllAuthorWithPagination', () => {
    (authorService.getAllAuthorWithPagination as jasmine.Spy).and.returnValue(
      throwError(() => new Error('Erreur'))
    );
    component.ngOnInit();
    fixture.detectChanges();
    expect(authorService.getAllAuthorWithPagination).toHaveBeenCalled();
  });
});
