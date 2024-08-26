import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatCheckbox} from "@angular/material/checkbox";
import {MatButton} from "@angular/material/button";
import {Router} from "@angular/router";
import {ManageArtworkTypes} from "../models/manage-types.enum";

@Component({
  selector: 'app-add-edit-artwork',
  standalone: true,
  imports: [
    MatFormField,
    MatInput,
    MatLabel,
    MatCheckbox,
    MatButton
  ],
  templateUrl: './add-edit-artwork.component.html',
  styleUrl: './add-edit-artwork.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class AddEditArtworkComponent implements OnInit {
  manageType: ManageArtworkTypes = ManageArtworkTypes.ADD_ARTWORK;

  title: string = '';
  buttonText: string = '';

  constructor(private router: Router) {
  }

  ngOnInit() {
    this.setArtworkManageType()
    this.setComponentDataByMangeType();
    console.log(this.manageType);
  }

  private setArtworkManageType(): void {
    const isAddingArtwork = this.router.url.includes('erstellen');
    if (isAddingArtwork) {
      this.manageType = ManageArtworkTypes.ADD_ARTWORK
      return;
    }
    this.manageType = ManageArtworkTypes.EDIT_ARTWORK
  }

  private setComponentDataByMangeType() {
    this.setComponentStrings()
    // this.setComponentForms()
  }

  private setComponentStrings() {
    if (this.manageType === ManageArtworkTypes.ADD_ARTWORK) {
      this.title = 'Kunstwerk hinzufügen'
      this.buttonText = 'Hinzufügen'
      return;
    }
    this.title = 'Kunstwerk bearbeiten'
    this.buttonText = 'Speichern'
  }
}
