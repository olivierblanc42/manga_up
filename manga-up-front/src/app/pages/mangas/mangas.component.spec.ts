import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MangasComponent } from './mangas.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { MangaService } from '../../service/manga.service';
import { BehaviorSubject, of } from 'rxjs';
import { MangaPaginations, MangaProjections } from '../../type';
import { ActivatedRoute } from '@angular/router';

describe('MangasComponent', () => {
  let component: MangasComponent;
  let fixture: ComponentFixture<MangasComponent>;
  let mangasService: Partial<MangaService>;
  beforeEach(async () => {
    mangasService = {
      mangaPagination: new BehaviorSubject<MangaPaginations | null>(null),
      currentMangaPaginations: of<MangaPaginations>({
        content: [
          {
            id: 1,
            pictureId: "1",
            pictureUrl: "https://example.com/manga1.jpg",
            title: "Manga 1"
          },
          {
            id: 2,
            pictureId: "2",
            pictureUrl: "https://example.com/manga2.jpg",
            title: "Manga 2"
          }
        ],
        size: 10,
        totalElements: 2,
        totalPages: 1
      }), 
getMangas: jasmine.createSpy('getMangas').and.returnValue(of({
  content: [
    {
      id: 1,
      pictureId: "1",
      pictureUrl: "https://example.com/manga1.jpg",
      title: "Manga 1"
    },
    {
      id: 2,
      pictureId: "2",
      pictureUrl: "https://example.com/manga2.jpg",
      title: "Manga 2"
    }
  ],
  size: 10,
  totalElements: 2,
  totalPages: 1
}))
,
}
    await TestBed.configureTestingModule({
      imports: [MangasComponent],
            providers: [
              provideHttpClient(), 
              provideHttpClientTesting(),
              { provide: MangaService, useValue: mangasService },
              {
                provide: ActivatedRoute,
                useValue: {
                  params: of({ id: 1 }), 
                  queryParams: of({ page: 0 }), 
                },
              },
            ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MangasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should fetch manga data on init', () => {
    expect(mangasService.getMangas).toHaveBeenCalled();
  });



  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
