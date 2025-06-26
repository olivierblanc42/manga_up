import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MangasAdminComponent } from './mangas.component';
import { BehaviorSubject, of, throwError } from 'rxjs';
import { MangaPaginations } from '../../../type';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { MangaService } from '../../../service/manga.service';
import { ActivatedRoute } from '@angular/router';

describe('MangasAdminComponent', () =>  {
  let component: MangasAdminComponent;
  let fixture: ComponentFixture<MangasAdminComponent>;

  let mangaPaginationSubject: BehaviorSubject<MangaPaginations | null>;
  let mangaServiceMock: {
    mangaPagination: BehaviorSubject<MangaPaginations | null>,
    currentMangaPaginations: BehaviorSubject<MangaPaginations | null>,
    getMangas: jasmine.Spy
  };

  const mockPaginationData: MangaPaginations = {
    content: [
      {
        id_mangas: 1,
        pictureId: '1',
        picture: 'https://example.com/manga1.jpg',
        title: 'Manga 1',
        authors: [{
          id: 1,
          lastname: "string",
          firstname: "string"
        }]
      },
      {
        id_mangas: 2,
        pictureId: '2',
        picture: 'https://example.com/manga2.jpg',
        title: 'Manga 2',
        authors: [{
          id: 1,
          lastname: "string",
          firstname: "string"
        }]
      }
    ],
    size: 10,
    totalElements: 2,
    totalPages: 1
  };

  beforeEach(async () => {
    mangaPaginationSubject = new BehaviorSubject<MangaPaginations | null>(null);

    mangaServiceMock = {
      mangaPagination: mangaPaginationSubject,
      currentMangaPaginations: mangaPaginationSubject,
      getMangas: jasmine.createSpy('getMangas').and.returnValue(of(mockPaginationData))
    };

    await TestBed.configureTestingModule({
      imports: [MangasAdminComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: MangaService, useValue: mangaServiceMock },
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({ id: 1 }),
            queryParams: of({ page: 0 }),
            snapshot: { paramMap: { get: () => '1' } }
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MangasAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch mangas on initialization', () => {
    expect(mangaServiceMock.getMangas).toHaveBeenCalled();
  });

  it('should set mangas from observable', () => {
    mangaPaginationSubject.next(mockPaginationData);
    fixture.detectChanges();
    expect(component.mangas).toEqual(mockPaginationData);
  });

  it('should display manga images correctly', () => {
    component.mangas = mockPaginationData;
    fixture.detectChanges();

    const img = fixture.nativeElement.querySelector('.image');
    expect(img).toBeTruthy();
    expect(img.src).toBe('https://example.com/manga1.jpg');
  });


  it('should handle null mangaPagination gracefully', () => {
    mangaPaginationSubject.next(null);
    fixture.detectChanges();
    expect(component.mangas).toBeNull();
  });

  it('should handle error from getMangas', () => {
    (mangaServiceMock.getMangas as jasmine.Spy).and.returnValue(
      throwError(() => new Error('Erreur serveur'))
    );

    component.ngOnInit();
    fixture.detectChanges();
    expect(mangaServiceMock.getMangas).toHaveBeenCalled();
  });
});
