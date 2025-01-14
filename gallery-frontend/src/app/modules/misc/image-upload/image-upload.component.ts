import {Component, ElementRef, EventEmitter, Input, Output, ViewChild, ViewEncapsulation} from '@angular/core';
import {Artwork as ArtworkEntity} from "../../artwork/model/artwork";
import {Event as EventEntity} from "../../event/model/event";
import {Exhibition as ExhibitionEntity} from "../../exhibitions/model/exhibition";
import {ManageModesEnum} from "../../dashboard/models/manage-modes.enum";
import {SnackBarService} from "../services/snack-bar.service";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-image-upload',
  standalone: true,
  imports: [
    TranslateModule
  ],
  templateUrl: './image-upload.component.html',
  styleUrl: './image-upload.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ImageUploadComponent {
  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;
  @Output() fileChange = new EventEmitter<File | undefined>(); // Need Emitter because "file" is a primitiv type
  @Input() file: File | undefined;
  @Input() entity: ArtworkEntity | ExhibitionEntity | EventEntity | undefined;
  @Input() manageMode: ManageModesEnum | undefined;

  private readonly VALID_FILE_TYPES = ['image/png', 'image/jpeg'];

  constructor(private readonly snackBar: SnackBarService) {
  }

  triggerFileInput(): void {
    this.fileInput.nativeElement.click();
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const file = input.files[0];

      if (!this.fileTypeIsValid(file)) {
        this.snackBar.openSnackBarByJSON("error", "snackbar.filetype.invalid")
        return;
      }

      this.file = file;
      this.fileChange.emit(this.file);
      this.updateImagePreview();
    }
  }

  private fileTypeIsValid(file: File): boolean {
    return this.VALID_FILE_TYPES.includes(file.type);
  }

  private updateImagePreview(): void {
    if (this.file) {
      const reader = new FileReader();
      reader.onload = (e: ProgressEvent<FileReader>) => {
        this.entity!.image = (e.target?.result as string)?.split(',')[1] || '';
      };
      reader.readAsDataURL(this.file);
    }
  }

  protected readonly ManageModesEnum = ManageModesEnum;
}
