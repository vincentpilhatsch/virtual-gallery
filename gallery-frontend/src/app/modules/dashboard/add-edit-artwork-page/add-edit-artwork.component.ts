import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatCheckbox} from "@angular/material/checkbox";
import {MatButton} from "@angular/material/button";
import {DashboardDataService} from "../services/dashboard-data.service";
import {ManageModesEnum} from "../models/manage-modes.enum";
import {Artwork} from "../../artwork/model/artwork";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {ArtworkService} from "../../artwork/services/artwork.service";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ArtworkForm} from "../models/artwork-form";

@Component({
  selector: 'app-add-edit-artwork',
  standalone: true,
  imports: [
    MatFormField,
    MatInput,
    MatLabel,
    MatCheckbox,
    MatButton,
    ReactiveFormsModule
  ],
  templateUrl: './add-edit-artwork.component.html',
  styleUrl: './add-edit-artwork.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class AddEditArtworkComponent implements OnInit {
  artwork: Artwork | undefined;
  file: File | null = null;
  manageMode: ManageModesEnum | undefined;
  disableLoginButton: boolean = false;

  form: FormGroup = this.fb.group({
    title: ['', [
      Validators.required,
    ]],
    technic: ['', [
      Validators.required,
    ]],
    width: [0, [
      Validators.required,
    ]],
    height: [0, [
      Validators.required,
    ]],
    yearOfCreation: [0, [
      Validators.required,
    ]],
    price: [0, [
      Validators.required,
    ]],
    sold: [false, [
      // Validators.required,
    ]],
    // notifyUsers: ['', [
    //   Validators.required,
    // ]],
  });

  constructor(private readonly dashboardDataService: DashboardDataService,
              private readonly route: ActivatedRoute,
              private readonly artworkService: ArtworkService,
              private readonly fb: FormBuilder) {
  }

  ngOnInit() {
    this.loadArtworkIfIsInEditMode();
    this.subscribeToFormValueChanges();
  }

  private loadArtworkIfIsInEditMode() {
    this.manageMode = this.dashboardDataService.getManageMode();
    if (this.manageMode === ManageModesEnum.EDIT) {
      this.getArtworkFromIdInParam();
    }
  }

  private getArtworkFromIdInParam(): void {
    this.route.paramMap.subscribe(params => {
      const id: string | null = params.get('id');
      if (id) {
        const convertedId = Number(id);
        this.loadArtwork(convertedId);
      }
    });
  }

  private loadArtwork(id: number): void {
    this.artworkService.getArtworkById(id).subscribe({
      next: (res: Artwork): void => {
        this.artwork = res;
        this.initForm();
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    })
  }

  private initForm(): void {
    if (this.artwork) {
      const artworkForm: ArtworkForm = {
        title: this.artwork.title || '',
        technic: this.artwork.technic || '',
        width: this.artwork.width || 0,
        height: this.artwork.height || 0,
        yearOfCreation: this.artwork.yearOfCreation || '',
        price: this.artwork.price || 0,
        sold: this.artwork.sold || false,
        // city: this.userFromParent.city ?? '' // if attribute is not required
      }
      this.form.setValue(artworkForm);
    }
  }

  private subscribeToFormValueChanges(): void {
    this.form.valueChanges.subscribe(() => {
      this.disableLoginButton = !this.formsAreValid();
    });
  }

  private formsAreValid(): boolean {
    const requiredFields = ["title", "width", "height", "yearOfCreation", "technic", "price"];
    const allFieldsFilled = requiredFields.every(field => !!this.form.get(field)?.value);
    return this.form.valid && this.file !== null && allFieldsFilled;
  }

  private getArtworkByFormData(): Artwork {
    return {
      title: this.form.get("title")?.value,
      technic: this.form.get("technic")?.value,
      width: this.form.get("width")?.value,
      height: this.form.get("height")?.value,
      yearOfCreation: this.form.get("yearOfCreation")?.value,
      price: this.form.get("price")?.value,
      sold: this.form.get("sold")?.value,
      image: this.form.get("image")?.value,
    }
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.file = input.files[0];
    }
  }

  onSubmit(): void {
    if (this.formsAreValid()) {
      const artwork: Artwork = this.getArtworkByFormData();
      this.artworkService.createArtwork(artwork, this.file!).subscribe({
        next: (res: Artwork) => {
          console.log(res);
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
        }
      });
    }
  }

  protected readonly ManageModesEnum = ManageModesEnum;
}
