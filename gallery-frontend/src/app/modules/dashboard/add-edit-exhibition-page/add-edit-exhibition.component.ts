import {Component, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatHint, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatCheckbox} from "@angular/material/checkbox";
import {MatButton} from "@angular/material/button";
import {ManageModesEnum} from "../models/manage-modes.enum";
import {HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {SnackBarService} from "../../misc/services/snack-bar.service";
import {ImageUploadComponent} from "../../misc/image-upload/image-upload.component";
import {Subscription} from "rxjs";
import {MatIcon, MatIconModule} from "@angular/material/icon";
import {Exhibition} from "../../exhibitions/model/exhibition";
import {ExhibitionService} from "../../exhibitions/services/exhibition.service";
import {ExhibitionForm} from "../models/exhibition-form";
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";
import {MatNativeDateModule} from '@angular/material/core';


@Component({
  selector: 'app-add-edit-exhibition',
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
    MatDatepicker,
    MatIconModule,
    MatDatepickerToggle,
    MatDatepickerInput,
    MatHint,
    MatNativeDateModule
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
    description: ['', Validators.required],
    location: ['', Validators.required],
    startDate: ['', Validators.required],
    endDate: ['', Validators.required],
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
    const isAddingExhibition = this.router.url.includes('erstellen');
    if (isAddingExhibition) {
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
        startDate: this.convertArrayToDateString(this.exhibition.startDate),
        endDate: this.convertArrayToDateString(this.exhibition.endDate)
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
      const exhibition: Exhibition = this.getExhibitionByFormData();
      if (this.manageMode === ManageModesEnum.ADD) {
        this.createExhibition(exhibition);
      } else if (this.manageMode === ManageModesEnum.EDIT) {
        this.updateExhibition(exhibition);
      }
      this.routeBackToManagePage();
    }
  }

  onDelete(): void {
    const exhibitionId: number | undefined = this.exhibition?.id;
    if (exhibitionId) {
      this.exhibitionService.deleteExhibition(exhibitionId).subscribe({
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
      startDate: this.convertDateStringToArray(this.form.get("startDate")?.value),
      endDate: this.convertDateStringToArray(this.form.get("endDate")?.value),
      image: this.exhibition.image
    }
  }

  convertDateStringToArray(dateString: string): number[] {
    const [day, month, year] = dateString.split('.').map(Number);
    return [year, month, day];
  }

  convertArrayToDateString(dateArray: number[]): string {
    const [year, month, day] = dateArray;
    const formattedDay = day.toString().padStart(2, '0');
    const formattedMonth = month.toString().padStart(2, '0');
    return `${formattedDay}.${formattedMonth}.${year}`;
  }


  private createExhibition(exhibition: Exhibition) {
    if (this.file) {
      this.exhibitionService.createExhibition(exhibition, this.file).subscribe({
        error: (err: HttpErrorResponse) => {
          console.log(err);
        }
      });
    }
  }

  private updateExhibition(exhibition: Exhibition) {
    if (this.file) {
      this.exhibitionService.updateExhibitionWithImage(exhibition, this.file).subscribe({
        error: (err: HttpErrorResponse) => {
          console.log(err);
        }
      })
    } else {
      this.exhibitionService.updateExhibition(exhibition).subscribe({
        error: (err: HttpErrorResponse) => {
          console.log(err);
        }
      })
    }
  }

  private routeBackToManagePage(): void {
    void this.router.navigate(["dashboard"]);
    const jsonString: string = this.manageMode === ManageModesEnum.ADD
      ? "snackbar.dashboard.exhibition.created"
      : "snackbar.dashboard.exhibition.updated";
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
