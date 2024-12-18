import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ManageExhibitionsComponent} from './manage-exhibitions.component';

describe('ManageArtworksComponent', () => {
  let component: ManageExhibitionsComponent;
  let fixture: ComponentFixture<ManageExhibitionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageExhibitionsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ManageExhibitionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
