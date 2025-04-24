import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MangasAdminComponent } from './mangas.component';

describe('MangasAdminComponent', () => {
  let component: MangasAdminComponent;
  let fixture: ComponentFixture<MangasAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MangasAdminComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MangasAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
