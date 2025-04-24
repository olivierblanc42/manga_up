import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuteursComponent } from './auteurs.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

describe('AuteursComponent', () => {
  let component: AuteursComponent;
  let fixture: ComponentFixture<AuteursComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AuteursComponent],
            providers: [
                    provideHttpClient(), 
                    provideHttpClientTesting(),
                  ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AuteursComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
