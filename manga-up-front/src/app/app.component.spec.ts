import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppComponent],            providers: [
                    provideHttpClient(), 
                    provideHttpClientTesting(), 
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
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have the 'manga-up-front' title`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('manga-up-front');
  });


});
