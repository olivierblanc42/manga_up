import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MangasComponent } from './mangas.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

describe('MangasComponent', () => {
  let component: MangasComponent;
  let fixture: ComponentFixture<MangasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MangasComponent],
            providers: [
              provideHttpClient(), 
              provideHttpClientTesting(),
            ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MangasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
