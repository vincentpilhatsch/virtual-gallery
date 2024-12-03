import {Component, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatDivider} from "@angular/material/divider";
import {MatList, MatListItem} from "@angular/material/list";
import {NgOptimizedImage} from "@angular/common";
import {MatCheckbox} from "@angular/material/checkbox";
import {FavoriteIconComponent} from "../../misc/favorite-icon/favorite-icon.component";
import {UserService} from "../../user/services/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ArtworkService} from "../services/artwork.service";
import {forkJoin, Subscription} from "rxjs";
import {Artwork} from "../model/artwork";
import {HttpErrorResponse} from "@angular/common/http";
import {MoneyConverterPipe} from "../pipes/money-converter.pipe";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {ArtworkRequest} from "../model/artworkRequest";
import {ArtworkRequestService} from "../services/artwork-request.service";
import {SnackBarService} from "../../misc/services/snack-bar.service";

@Component({
  selector: 'app-request-artwork',
  standalone: true,
  imports: [
    FavoriteIconComponent,
    MatButton,
    MatDivider,
    MatList,
    MatListItem,
    NgOptimizedImage,
    MatCheckbox,
    MoneyConverterPipe,
    FormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
  ],
  templateUrl: './request-artwork.component.html',
  styleUrl: './request-artwork.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class RequestArtworkComponent implements OnInit, OnDestroy {
  artwork: Artwork | undefined;
  isFavorite: boolean = false;
  formsAreValid: boolean = false;

  form: FormGroup = this.fb.group({
    description: ['', [
      Validators.required,
    ]],
    confirmation: ['', [
      Validators.required,
    ]]
  });

  subscriptions: Subscription[] = [];

  constructor(protected userService: UserService,
              private readonly artworkService: ArtworkService,
              private readonly artworkRequestService: ArtworkRequestService,
              private readonly snackBarService: SnackBarService,
              private readonly route: ActivatedRoute,
              private readonly router: Router,
              private readonly fb: FormBuilder) {
  }

  ngOnInit() {
    this.subscribeToUser();
    this.subscribeToCheckboxChanges();
  }

  private subscribeToUser() {
    this.subscriptions.push(
      this.userService.user$.subscribe((user) => {
        if (user) {
          this.loadArtworkIdFromParam(user.email);
        }
      })
    );
  }

  private loadArtworkIdFromParam(email: string): void {
    this.subscriptions.push(
      this.route.paramMap.subscribe(params => {
        const id: string | null = params.get('id');
        if (id) {
          const convertedId = Number(id);
          this.loadArtworkAndFavoriteIds(convertedId, email);
        }
      })
    );
  }

  private loadArtworkAndFavoriteIds(id: number, email: string): void {
    forkJoin({
      artwork: this.artworkService.getArtworkById(id),
      favoriteIds: this.userService.getFavoriteArtworkIdsByEmail(email)
    }).subscribe({
      next: (res: { artwork: Artwork, favoriteIds: number[] }): void => {
        this.artwork = res.artwork;
        this.isFavorite = res.favoriteIds.includes(this.artwork.id!);
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    });
  }

  private subscribeToCheckboxChanges(): void {
    this.subscriptions.push(
      this.form.valueChanges.subscribe(() => {
        this.formsAreValid = this.checkIfFormsAreValid();
      })
    );
  }

  onRequestSubmitted(): void {
    if (this.checkIfFormsAreValid() && this.userService.user?.id && this.artwork?.id) { // TODO: Comment in for Live
      const formNotification: string = this.form.get("description")?.value;
      const artworkRequest: ArtworkRequest = {
        description: formNotification,
        userId: this.userService.user.id,
        artworkId: this.artwork.id
      }
      this.createArtworkRequest(artworkRequest);
    }
  }

  private checkIfFormsAreValid(): boolean {
    const formNotification: string = this.form.get("description")?.value;
    const formConformation: string = this.form.get("confirmation")?.value;
    const formIsValid: boolean = this.form.valid;
    return formIsValid && !!formNotification && !!formConformation;
  }

  private createArtworkRequest(artworkRequest: ArtworkRequest) {
    this.artworkRequestService.createArtworkRequest(artworkRequest).subscribe({
      next: (): void => {
        if (this.artwork?.id) {
          void this.router.navigate(["kunstwerk", this.artwork.id]);
          this.snackBarService.openSnackBarByJSON("info", "snackbar.artwork.requested");
        }
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    })
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => {
      if (sub) {
        sub.unsubscribe();
      }
    })
  }
}
