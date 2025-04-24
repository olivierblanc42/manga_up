import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GenresAdminComponent } from './genres.component';

describe('GenresAdminComponent', () => {
  let component: GenresAdminComponent;
  let fixture: ComponentFixture<GenresAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GenresAdminComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GenresAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
