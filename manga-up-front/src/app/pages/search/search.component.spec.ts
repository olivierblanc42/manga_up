import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SearchComponent } from './search.component';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject, of } from 'rxjs';
import { SearchService } from './../../service/search.service';
import { MangaBaseProjections } from '../../type';

describe('SearchComponent', () => {
  let component: SearchComponent;
  let fixture: ComponentFixture<SearchComponent>;

  let mangasSearchSubject: BehaviorSubject<MangaBaseProjections | null>;
  let searchServiceMock: {
    currentmangasSearch: BehaviorSubject<MangaBaseProjections | null>,
    getMangas: jasmine.Spy
  };

  beforeEach(async () => {
    mangasSearchSubject = new BehaviorSubject<MangaBaseProjections | null>(null);

    searchServiceMock = {
      currentmangasSearch: mangasSearchSubject,
      getMangas: jasmine.createSpy('getMangas').and.returnValue(Promise.resolve())
    };

    await TestBed.configureTestingModule({
      imports: [SearchComponent],
      providers: [
        provideHttpClientTesting(),
        { provide: SearchService, useValue: searchServiceMock },
        {
          provide: ActivatedRoute,
          useValue: {
            paramMap: of({
              get: (key: string) => 'a',
            }),
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call getMangas on init', () => {
    expect(searchServiceMock.getMangas).toHaveBeenCalledWith('a', 0);
  });

 
});
