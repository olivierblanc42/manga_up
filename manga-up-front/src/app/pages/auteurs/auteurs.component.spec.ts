import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuteursComponent } from './auteurs.component';

describe('AuteursComponent', () => {
  let component: AuteursComponent;
  let fixture: ComponentFixture<AuteursComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AuteursComponent]
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
