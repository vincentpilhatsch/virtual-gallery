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
import {Event} from "../../event/model/event";
import {EventService} from "../../event/services/event.service";
import {EventForm} from "../models/event-form";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-add-edit-event',
  standalone: true,
  imports: [
    MatFormField,
    MatLabel,
    MatInput,
    MatButton,
    ReactiveFormsModule,
    ImageUploadComponent,
    MatProgressSpinner,
    TranslateModule
  ],
  templateUrl: './add-edit-event.component.html',
  styleUrl: './add-edit-event.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class AddEditEventComponent implements OnInit, OnDestroy {
  file: File | undefined;
  manageMode: ManageModesEnum | undefined;
  disableCreateButton: boolean = true;
  event: Event = {
    id: 0,
    title: "",
    description: "",
    location: "",
    dateTime: [],
    image: "",
    confirmedUserIds: [],
  };
  form: FormGroup = this.fb.group({
    title: ['', Validators.required],
    description: ['', Validators.required],
    location: ['', Validators.required],
    date: ['', Validators.required],
    time: ['', Validators.required],
  });
  subscriptions: Subscription[] = [];

  constructor(private readonly route: ActivatedRoute,
              private readonly eventService: EventService,
              private readonly router: Router,
              private readonly snackBarService: SnackBarService,
              private readonly fb: FormBuilder) {
  }

  ngOnInit() {
    this.loadEventIfIsInEditMode();
    this.subscribeToFormValueChanges();
    this.updateAddButtonVisibility();
  }

  private loadEventIfIsInEditMode(): void {
    const isAddingEvent = this.router.url.includes('erstellen');
    if (isAddingEvent) {
      this.manageMode = ManageModesEnum.ADD;
      return;
    }
    this.getEventFromIdInParam();
    this.manageMode = ManageModesEnum.EDIT
  }

  private getEventFromIdInParam(): void {
    this.subscriptions.push(
      this.route.paramMap.subscribe(params => {
        const id: string | null = params.get('id');
        if (id) {
          const convertedId = Number(id);
          this.loadEvent(convertedId);
        }
      })
    );
  }

  private loadEvent(id: number): void {
    this.eventService.getEventById(id).subscribe({
      next: (res: Event): void => {
        this.event = res;
        this.initForm();
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    })
  }

  private initForm(): void {
    if (this.event) {
      const eventForm: EventForm = {
        title: this.event.title,
        description: this.event.description,
        location: this.event.location,
        date: this.convertArrayToDateString(this.event.dateTime),
        time: this.convertArrayToTimeString(this.event.dateTime)
      }
      this.form.setValue(eventForm);
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
    const requiredFields: string[] = ["title", "description", "location", "date", "time"];
    const allFieldsFilled: boolean = requiredFields.every(field => !!this.form.get(field)?.value);
    const imagePresent: boolean = this.event?.image.length > 0 || this.file !== undefined;

    // console.log('Form status:', this.form.status);
    // console.log('File uploaded:', imagePresent);

    return this.form.valid && allFieldsFilled && imagePresent;
  }

  onFileChange(file: File | undefined): void {
    this.file = file;
    this.updateAddButtonVisibility();
  }

  onCreateOrUpdate(): void {
    if (this.formsAreValid()) {
      const event: Event = this.getEventByFormData();
      if (this.manageMode === ManageModesEnum.ADD) {
        this.createEvent(event);
      } else if (this.manageMode === ManageModesEnum.EDIT) {
        this.updateEvent(event);
      }
      this.routeBackToManagePage();
    }
  }

  onDelete(): void {
    const eventId: number | undefined = this.event?.id;
    if (eventId) {
      this.eventService.deleteEvent(eventId).subscribe({
        error: (err: HttpErrorResponse) => {
          console.error(err);
        }
      });
      this.routeBackToManagePage();
    }
  }

  private getEventByFormData(): Event {
    return {
      id: this.event.id,
      title: this.form.get("title")?.value,
      description: this.form.get("description")?.value,
      location: this.form.get("location")?.value,
      dateTime: this.convertDateTimeToArray(
        this.form.get("date")?.value,
        this.form.get("time")?.value
      ),
      image: this.event.image,
      confirmedUserIds: this.event.confirmedUserIds
    }
  }

  convertDateTimeToArray(date: string, time: string): number[] {
    const normalizedDate = this.normalizeDate(date);
    const normalizedTime = this.normalizeTime(time);
    const dateTimeString = `${normalizedDate}T${normalizedTime}`;
    const dateObj = new Date(dateTimeString);

    return [
      dateObj.getFullYear(),
      dateObj.getMonth() + 1,
      dateObj.getDate(),
      dateObj.getHours(),
      dateObj.getMinutes(),
      0, // 0 for seconds
      0 // 0 for millis
    ];
  }

  normalizeDate(date: string): string {
    const [day, month, year] = date.split('.');
    return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
  }

  normalizeTime(time: string): string {
    const [hours, minutes] = time.split(':');
    return `${hours.padStart(2, '0')}:${minutes.padStart(2, '0')}`;
  }

  convertArrayToDateString(dateArray: number[]): string {
    const [year, month, day] = dateArray;
    const formattedDay = day.toString().padStart(2, '0');
    const formattedMonth = month.toString().padStart(2, '0');
    return `${formattedDay}.${formattedMonth}.${year}`;
  }

  convertArrayToTimeString(timeArray: number[]): string {
    const [year, month, day, hours, minutes] = timeArray;
    const formattedHours = hours.toString().padStart(2, '0');
    const formattedMinutes = minutes.toString().padStart(2, '0');
    return `${formattedHours}:${formattedMinutes}`;
  }

  private createEvent(event: Event) {
    if (this.file) {
      this.eventService.createEvent(event, this.file).subscribe({
        error: (err: HttpErrorResponse) => {
          console.log(err);
        }
      });
    }
  }

  private updateEvent(event: Event) {
    if (this.file) {
      console.log(event);
      this.eventService.updateEventWithImage(event, this.file).subscribe({
        error: (err: HttpErrorResponse) => {
          console.log(err);
        }
      })
    } else {
      this.eventService.updateEvent(event).subscribe({
        error: (err: HttpErrorResponse) => {
          console.log(err);
        }
      })
    }
  }

  private routeBackToManagePage(): void {
    void this.router.navigate(["dashboard"]);
    const jsonString: string = this.manageMode === ManageModesEnum.ADD
      ? "snackbar.dashboard.event.created"
      : "snackbar.dashboard.event.updated";
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
