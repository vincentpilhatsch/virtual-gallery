import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AddEditArtworkComponent} from './add-edit-artwork.component';

describe('AddArtworkComponent', () => {
  let component: AddEditArtworkComponent;
  let fixture: ComponentFixture<AddEditArtworkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddEditArtworkComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AddEditArtworkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
