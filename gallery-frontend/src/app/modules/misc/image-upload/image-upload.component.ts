import {Component, ElementRef, EventEmitter, Input, Output, ViewChild, ViewEncapsulation} from '@angular/core';
import {Artwork as ArtworkEntity} from "../../artwork/model/artwork";
import {Event as EventEntity} from "../../event/model/event";
import {Exhibition as ExhibitionEntity} from "../../exhibitions/model/exhibition";
import {ManageModesEnum} from "../../dashboard/models/manage-modes.enum";

@Component({
  selector: 'app-image-upload',
  standalone: true,
  imports: [],
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

  triggerFileInput(): void {
    this.fileInput.nativeElement.click();
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.file = input.files[0];
      this.fileChange.emit(this.file);
      this.updateImagePreview();
    }
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
