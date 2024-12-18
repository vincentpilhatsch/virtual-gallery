import {Component, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatCheckbox} from "@angular/material/checkbox";
import {MatButton} from "@angular/material/button";
import {ManageModesEnum} from "../models/manage-modes.enum";
import {Artwork} from "../../artwork/model/artwork";
import {HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {SnackBarService} from "../../misc/services/snack-bar.service";
import {ImageUploadComponent} from "../../misc/image-upload/image-upload.component";
import {Subscription} from "rxjs";
import {MatIcon} from "@angular/material/icon";
import {Exhibition} from "../../exhibitions/model/exhibition";
import {ExhibitionService} from "../../exhibitions/services/exhibition.service";
import {ExhibitionForm} from "../models/exhibition-form";

@Component({
  selector: 'app-add-exhibition-artwork',
  standalone: true,
  imports: [
    MatFormField,
    MatInput,
    MatLabel,
    MatCheckbox,
    MatButton,
    ReactiveFormsModule,
    ImageUploadComponent,
    MatIcon
  ],
  templateUrl: './add-edit-exhibition.component.html',
  styleUrl: './add-edit-exhibition.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class AddEditExhibitionComponent implements OnInit, OnDestroy {
  file: File | undefined;
  manageMode: ManageModesEnum | undefined;
  disableCreateButton: boolean = true;
  exhibition: Exhibition = {
    id: 0,
    title: "",
    description: "",
    location: "",
    startDate: [],
    endDate: [],
    image: "",
  };
  form: FormGroup = this.fb.group({
    title: ['', Validators.required],
    technic: ['', Validators.required],
    width: ['', Validators.required],
    height: ['', Validators.required],
    yearOfCreation: ['', Validators.required],
    price: ['', Validators.required],
    sold: [false],
  });
  subscriptions: Subscription[] = [];

  constructor(private readonly route: ActivatedRoute,
              private readonly exhibitionService: ExhibitionService,
              private readonly router: Router,
              private readonly snackBarService: SnackBarService,
              private readonly fb: FormBuilder) {
  }

  ngOnInit() {
    this.loadExhibitionIfIsInEditMode();
    this.subscribeToFormValueChanges();
    this.updateAddButtonVisibility();
  }

  private loadExhibitionIfIsInEditMode(): void {
    const isAddingArtwork = this.router.url.includes('erstellen');
    if (isAddingArtwork) {
      this.manageMode = ManageModesEnum.ADD;
      return;
    }
    this.getExhibitionFromIdInParam();
    this.manageMode = ManageModesEnum.EDIT
  }

  private getExhibitionFromIdInParam(): void {
    this.subscriptions.push(
      this.route.paramMap.subscribe(params => {
        const id: string | null = params.get('id');
        if (id) {
          const convertedId = Number(id);
          this.loadExhibition(convertedId);
        }
      })
    );
  }

  private loadExhibition(id: number): void {
    this.exhibitionService.getExhibitionById(id).subscribe({
      next: (res: Exhibition): void => {
        this.exhibition = res;
        this.initForm();
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    })
  }

  private initForm(): void {
    if (this.exhibition) {
      const exhibitionForm: ExhibitionForm = {
        title: this.exhibition.title,
        description: this.exhibition.description,
        location: this.exhibition.location,
        startDate: this.exhibition.startDate,
        endDate: this.exhibition.endDate,
        image: this.exhibition.image,
      }
      this.form.setValue(exhibitionForm);
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
    const requiredFields: string[] = ["title", "description", "location", "startDate", "endDate"];
    const allFieldsFilled: boolean = requiredFields.every(field => !!this.form.get(field)?.value);
    const imagePresent: boolean = this.exhibition?.image.length > 0 || this.file !== undefined;

    // console.log('Form status:', this.form.status);
    // console.log('File uploaded:', imagePresent);

    return this.form.valid && allFieldsFilled && imagePresent;
  }

  onFileChange(file: File | undefined): void {
    this.file = file;
    this.updateAddButtonVisibility();
  }

  onCreate(): void {
    if (this.formsAreValid()) {
      const artwork: Artwork = this.getExhibitionByFormData();
      if (this.manageMode === ManageModesEnum.ADD) {
        this.createExhibition(artwork);
      } else if (this.manageMode === ManageModesEnum.EDIT) {
        this.updateExhibition(artwork);
      }
      this.routeBackToManagePage();
    }
  }

  onDelete(): void {
    const artworkId: number | undefined = this.exhibition?.id;
    if (artworkId) {
      this.exhibitionService.deleteArtwork(artworkId).subscribe({
        error: (err: HttpErrorResponse) => {
          console.error(err);
        }
      });
      this.routeBackToManagePage();
    }
  }

  private getExhibitionByFormData(): Exhibition {
    return {
      id: this.exhibition.id,
      title: this.form.get("title")?.value,
      description: this.form.get("description")?.value,
      location: this.form.get("location")?.value,
      startDate: this.form.get("startDate")?.value,
      endDate: this.form.get("endDate")?.value,
      image: this.exhibition.image
    }
  }

  private createExhibition(artwork: Artwork) {
    if (this.file) {
      this.exhibitionService.createArtwork(artwork, this.file).subscribe({
        error: (err: HttpErrorResponse) => {
          console.log(err);
        }
      });
    }
  }

  private updateExhibition(artwork: Artwork) {
    if (this.file) {
      this.exhibitionService.updateArtworkWithImage(artwork, this.file).subscribe({
        error: (err: HttpErrorResponse) => {
          console.log(err);
        }
      })
    } else {
      this.exhibitionService.updateArtwork(artwork).subscribe({
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
