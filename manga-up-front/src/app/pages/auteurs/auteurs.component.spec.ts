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

  // Simulation du service AuthorService avec ses flux de données
  let authorService: {
    authorProjection: BehaviorSubject<AuthorProjections | null>,
    currentauthorProjection: BehaviorSubject<AuthorProjections | null>,
    getAllAuthorWithPagination: jasmine.Spy,
  };

  // Données fictives représentant une réponse de l'API
  const mockAuthors: AuthorProjections = {
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
  };

  beforeEach(async () => {
    // Initialisation du service mocké avec des BehaviorSubjects
    authorService = {
      authorProjection: new BehaviorSubject<AuthorProjections | null>(null),
      currentauthorProjection: new BehaviorSubject<AuthorProjections | null>(mockAuthors),
      getAllAuthorWithPagination: jasmine.createSpy('getAllAuthorWithPagination').and.returnValue(of()),
    };

    // Configuration du module de test avec le composant et les providers nécessaires
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

    // Déclenche le cycle de vie Angular (ngOnInit, etc.)
    fixture.detectChanges();
  });

  // Vérifie que le composant se crée sans erreur
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Vérifie que le service est appelé lors de l'initialisation
  it('should fetch authors on initialization', () => {
    expect(authorService.getAllAuthorWithPagination).toHaveBeenCalled();
  });

  // Vérifie que les données sont correctement récupérées et affectées au composant
  it('should set authorProjection on initialization', () => {
    authorService.currentauthorProjection.next(mockAuthors);
    fixture.detectChanges();
    expect(component.authors).toEqual(mockAuthors);
  });

  // Vérifie que l'image de l'auteur est bien affichée dans le DOM
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
      totalElements: 1,
      totalPages: 1
    };
    fixture.detectChanges();
    const img = fixture.nativeElement.querySelector('.image');
    expect(img.src).toBe('https://example.com/bleach.jpg');
  });

  // Vérifie le fonctionnement des boutons de pagination (précédent, suivant, numéros de page)
  it('should display pagination buttons and call the right methods', () => {
    spyOn(component, 'pagePrevious');
    spyOn(component, 'pageNext');
    spyOn(component, 'pageAuthor');

    component.pages = [0, 1, 2];
    component.currentPage = 0;
    component.lastPage = 3;
    fixture.detectChanges();

    const buttons = fixture.nativeElement.querySelectorAll('button');
    expect(buttons.length).toBe(5); // précédent + 3 pages + suivant

    // Vérifie les numéros affichés
    expect(buttons[1].textContent.trim()).toBe('1');
    expect(buttons[2].textContent.trim()).toBe('2');
    expect(buttons[3].textContent.trim()).toBe('3');

    // Simule les clics
    buttons[0].click(); // précédent
    expect(component.pagePrevious).toHaveBeenCalled();

    buttons[2].click(); // page 2 (index 1)
    expect(component.pageAuthor).toHaveBeenCalledWith(1);

    buttons[4].click(); // suivant
    expect(component.pageNext).toHaveBeenCalled();
  });

  // Vérifie que le titre principal est bien affiché
  it('should have the correct title', () => {
    const titleElement = fixture.nativeElement.querySelector('h1');
    expect(titleElement.textContent).toBe('Auteurs');
  });

  // Vérifie que le composant gère proprement une erreur provenant du service
  it('should handle error from getAllAuthorWithPagination', () => {
    authorService.getAllAuthorWithPagination.and.returnValue(
      throwError(() => new Error('Erreur'))
    );
    component.ngOnInit();
    fixture.detectChanges();
    expect(authorService.getAllAuthorWithPagination).toHaveBeenCalled();
  });

  // Vérifie que le composant ne plante pas si les données sont nulles
  it('should handle null currentauthorProjection gracefully', () => {
    authorService.currentauthorProjection.next(null);
    fixture.detectChanges();
    expect(component.authors).toBeNull();
  });

});
