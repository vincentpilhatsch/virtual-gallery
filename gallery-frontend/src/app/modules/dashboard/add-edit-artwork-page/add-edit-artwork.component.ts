import {Component, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatCheckbox} from "@angular/material/checkbox";
import {MatButton} from "@angular/material/button";
import {ManageModesEnum} from "../models/manage-modes.enum";
import {Artwork} from "../../artwork/model/artwork";
import {HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";
import {ArtworkService} from "../../artwork/services/artwork.service";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ArtworkForm} from "../models/artwork-form";
import {SnackBarService} from "../../misc/services/snack-bar.service";
import {ImageUploadComponent} from "../../misc/image-upload/image-upload.component";
import {Subscription} from "rxjs";
import {MatIcon} from "@angular/material/icon";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-add-edit-artwork',
  standalone: true,
  imports: [
    MatFormField,
    MatInput,
    MatLabel,
    MatCheckbox,
    MatButton,
    ReactiveFormsModule,
    ImageUploadComponent,
    MatIcon,
    MatProgressSpinner,
    TranslateModule
  ],
  templateUrl: './add-edit-artwork.component.html',
  styleUrl: './add-edit-artwork.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class AddEditArtworkComponent implements OnInit, OnDestroy {
  file: File | undefined;
  manageMode: ManageModesEnum | undefined;
  disableCreateButton: boolean = true;
  artwork: Artwork = {
    id: 0,
    title: "",
    width: 0,
    height: 0,
    yearOfCreation: "",
    uploadedAt: undefined,
    technic: "",
    price: 0,
    sold: false,
    image: ""
  };
  form: FormGroup = this.fb.group({
    title: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(120)]],
    width: ['', Validators.required],
    height: ['', Validators.required],
    yearOfCreation: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(4)]],
    technic: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(120)]],
    price: ['', Validators.required],
    sold: [false],
  });
  subscriptions: Subscription[] = [];

  constructor(private readonly route: ActivatedRoute,
              private readonly artworkService: ArtworkService,
              private readonly router: Router,
              private readonly snackBarService: SnackBarService,
              private readonly fb: FormBuilder) {
  }

  ngOnInit() {
    this.loadArtworkIfIsInEditMode();
    this.subscribeToFormValueChanges();
    this.updateAddButtonVisibility();
  }

  private loadArtworkIfIsInEditMode(): void {
    const isAddingArtwork = this.router.url.includes('erstellen');
    if (isAddingArtwork) {
      this.manageMode = ManageModesEnum.ADD;
      return;
    }
    this.getArtworkFromIdInParam();
    this.manageMode = ManageModesEnum.EDIT
  }

  private getArtworkFromIdInParam(): void {
    this.subscriptions.push(
      this.route.paramMap.subscribe(params => {
        const id: string | null = params.get('id');
        if (id) {
          const convertedId = Number(id);
          this.loadArtwork(convertedId);
        }
      })
    );
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
        title: this.artwork.title,
        technic: this.artwork.technic,
        width: this.artwork.width,
        height: this.artwork.height,
        yearOfCreation: this.artwork.yearOfCreation,
        price: this.artwork.price,
        sold: this.artwork.sold,
      }
      this.form.setValue(artworkForm);
    }
  }

  private subscribeToFormValueChanges(): void {
    this.subscriptions.push(
      this.form.valueChanges.subscribe(() => {
        this.updateAddButtonVisibility();
      })
    );
  }

  private updateAddButtonVisibility(): void {
    this.disableCreateButton = !this.formsAreValid();
  }

  private formsAreValid(): boolean {
    const requiredFields: string[] = ["title", "width", "height", "yearOfCreation", "technic", "price"];
    const allFieldsFilled: boolean = requiredFields.every(field => !!this.form.get(field)?.value);
    const imagePresent: boolean = this.artwork?.image.length > 0 || this.file !== undefined;
    return this.form.valid && allFieldsFilled && imagePresent;
  }

  onFileChange(file: File | undefined): void {
    this.file = file;
    this.updateAddButtonVisibility();
    console.log(this.formsAreValid());
  }

  onCreateOrUpdate(): void {
    if (this.formsAreValid()) {
      const artwork: Artwork = this.getArtworkByFormData();
      if (this.manageMode === ManageModesEnum.ADD) {
        this.createArtwork(artwork);
      } else if (this.manageMode === ManageModesEnum.EDIT) {
        this.updateArtwork(artwork);
      }
      this.routeBackToManagePage();
    }
  }

  onDelete(): void {
    const artworkId: number | undefined = this.artwork?.id;
    if (artworkId) {
      this.artworkService.deleteArtwork(artworkId).subscribe({
        error: (err: HttpErrorResponse) => {
          console.error(err);
        }
      });
      this.routeBackToManagePage();
    }
  }

  private getArtworkByFormData(): Artwork {
    return {
      id: this.artwork.id,
      title: this.form.get("title")?.value,
      technic: this.form.get("technic")?.value,
      width: this.form.get("width")?.value,
      height: this.form.get("height")?.value,
      yearOfCreation: this.form.get("yearOfCreation")?.value,
      price: this.form.get("price")?.value,
      sold: this.form.get("sold")?.value,
      image: this.artwork.image
    }
  }

  private createArtwork(artwork: Artwork) {
    if (this.file) {
      this.artworkService.createArtwork(artwork, this.file).subscribe({
        error: (err: HttpErrorResponse) => {
          console.log(err);
        }
      });
    }
  }

  private updateArtwork(artwork: Artwork) {
    if (this.file) {
      this.artworkService.updateArtworkWithImage(artwork, this.file).subscribe({
        error: (err: HttpErrorResponse) => {
          console.log(err);
        }
      })
    } else {
      this.artworkService.updateArtwork(artwork).subscribe({
        error: (err: HttpErrorResponse) => {
          console.log(err);
        }
      })
    }
  }

  private routeBackToManagePage(): void {
    void this.router.navigate(["dashboard"]);
    const jsonString: string = this.manageMode === ManageModesEnum.ADD
      ? "snackbar.dashboard.artwork.created"
      : "snackbar.dashboard.artwork.updated";
    this.snackBarService.openSnackBarByJSON("info", jsonString);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => {
      if (sub) {
        sub.unsubscribe();
      }
    })
  }

  protected readonly ManageModesEnum = ManageModesEnum;
}
