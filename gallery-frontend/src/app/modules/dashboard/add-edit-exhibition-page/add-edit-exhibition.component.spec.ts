import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AddEditExhibitionComponent} from './add-edit-exhibition.component';

describe('AddArtworkComponent', () => {
  let component: AddEditExhibitionComponent;
  let fixture: ComponentFixture<AddEditExhibitionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddEditExhibitionComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AddEditExhibitionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
