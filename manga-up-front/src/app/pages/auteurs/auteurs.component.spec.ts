import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AuteursComponent } from './auteurs.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { BehaviorSubject, of, throwError } from 'rxjs';
import { AuthorService } from '../../service/author.service';
import { AuthorProjections } from '../../type';
import { ActivatedRoute } from '@angular/router';

describe('AuteursComponent', () => {
  let component: AuteursComponent;
  let fixture: ComponentFixture<AuteursComponent>;

  let authorProjectionSubject: BehaviorSubject<AuthorProjections | null>;
  let authorServiceMock: Partial<AuthorService>;

  const mockAuthorData: AuthorProjections = {
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
    authorProjectionSubject = new BehaviorSubject<AuthorProjections | null>(null);

    authorServiceMock = {
      authorProjection: authorProjectionSubject,
      currentauthorProjection: authorProjectionSubject.asObservable(),
      getAllAuthorWithPagination: jasmine.createSpy('getAllAuthorWithPagination').and.returnValue(of(mockAuthorData))
    };

    await TestBed.configureTestingModule({
      imports: [AuteursComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: AuthorService, useValue: authorServiceMock },
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({ id: '1' }),
            snapshot: {
              paramMap: {
                get: () => '1'
              }
            }
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
    expect(authorServiceMock.getAllAuthorWithPagination).toHaveBeenCalled();
  });

  it('should set authors from observable', () => {
    authorProjectionSubject.next(mockAuthorData);
    fixture.detectChanges();
    expect(component.authors).toEqual(mockAuthorData);
  });

  it('should display author images correctly', () => {
    authorProjectionSubject.next(mockAuthorData);
    fixture.detectChanges();
    const img = fixture.nativeElement.querySelectorAll('.image');
    expect(img.length).toBe(2);

    expect(img[0].src).toContain('https://example.com/naruto.jpg');
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

  it('should handle null authorProjection gracefully', () => {
    authorProjectionSubject.next(null);
    fixture.detectChanges();
    expect(component.authors).toBeNull();
  });

  it('should handle error from getAllAuthorWithPagination', () => {
    (authorServiceMock.getAllAuthorWithPagination as jasmine.Spy).and.returnValue(
      throwError(() => new Error('Erreur serveur'))
    );
    component.ngOnInit();
    fixture.detectChanges();
    expect(authorServiceMock.getAllAuthorWithPagination).toHaveBeenCalled();
  });
});
