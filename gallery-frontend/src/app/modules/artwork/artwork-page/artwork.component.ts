import {Component, ElementRef, OnDestroy, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {NgOptimizedImage} from "@angular/common";
import {FavoriteIconComponent} from "../../misc/favorite-icon/favorite-icon.component";
import {MatList, MatListItem} from "@angular/material/list";
import {MatDivider} from "@angular/material/divider";
import {MatButton} from "@angular/material/button";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {ArtworkService} from "../services/artwork.service";
import {Artwork} from "../model/artwork";
import {HttpErrorResponse} from "@angular/common/http";
import {ArtworkCardComponent} from "../artwork-card/artwork-card.component";
import {UserService} from "../../user/services/user.service";
import {forkJoin, Subscription} from "rxjs";
import {MoneyConverterPipe} from "../pipes/money-converter.pipe";
import {ArtworkRequest} from "../model/artworkRequest";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatCheckbox} from "@angular/material/checkbox";
import {MatFormField} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {ArtworkRequestService} from "../services/artwork-request.service";
import {SnackBarService} from "../../misc/services/snack-bar.service";
import {User} from "../../user/model/user";
import {RoleEnum} from "../../user/model/role.enum";

@Component({
  selector: 'app-artwork',
  standalone: true,
  imports: [
    NgOptimizedImage,
    FavoriteIconComponent,
    ArtworkCardComponent,
    MatList,
    MatListItem,
    MatDivider,
    MatButton,
    RouterLink,
    MoneyConverterPipe,
    FormsModule,
    MatCheckbox,
    MatFormField,
    MatInput,
    ReactiveFormsModule
  ],
  templateUrl: './artwork.component.html',
  styleUrl: './artwork.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ArtworkComponent implements OnInit, OnDestroy {
  @ViewChild('requestContainer') requestContainer: ElementRef | undefined;

  artwork: Artwork | undefined;
  randomArtworks: Artwork[] = [];
  readonly NUMBER_OF_RANDOM_ARTWORKS = 3;

  form: FormGroup = this.fb.group({
    description: ['', [
      Validators.required,
    ]],
    // confirmation: ['', [
    //   Validators.required,
    // ]]
  });
  formsAreValid: boolean = false;

  subscriptions: Subscription[] = [];

  favoriteIds: number[] = [];
  isFavorite: boolean = false;
  isAlreadyRequested: boolean = false;

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
    this.loadFourRandomArtworks();
    this.subscribeToCheckboxChanges();
  }

  private subscribeToUser() {
    this.subscriptions.push(
      this.userService.user$.subscribe((user) => {
        if (user) {
          this.loadArtworkIdFromParam(user.email);
        } else {
          this.loadArtworkIdFromParam();
        }
      })
    );
  }

  private loadArtworkIdFromParam(email?: string): void {
    this.subscriptions.push(
      this.route.paramMap.subscribe(params => {
        const id: string | null = params.get('id');
        if (id) {
          const convertedId = Number(id);
          if (email) {
            this.loadArtworkAndFavoriteIdsAndArtworkRequests(convertedId, email);
          } else {
            this.loadArtwork(convertedId);
          }
        }
      })
    );
  }

  private loadArtworkAndFavoriteIdsAndArtworkRequests(id: number, email: string): void {
    forkJoin({
      artwork: this.artworkService.getArtworkById(id),
      favoriteIds: this.userService.getFavoriteArtworkIdsByEmail(email),
      artworkRequests: this.userService.getArtworkRequestsByEmail(email)
    }).subscribe({
      next: (res: { artwork: Artwork, favoriteIds: number[], artworkRequests: ArtworkRequest[] }): void => {
        this.artwork = res.artwork;
        this.favoriteIds = res.favoriteIds;
        this.isFavorite = res.favoriteIds.includes(this.artwork.id!);
        this.isAlreadyRequested = res.artworkRequests.some((artworkRequest: ArtworkRequest) =>
          artworkRequest.artworkId === res.artwork.id
        );
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    });
  }

  private loadArtwork(id: number): void {
    this.artworkService.getArtworkById(id).subscribe({
      next: (res: Artwork) => {
        this.artwork = res;
      },
      error: (err: HttpErrorResponse) => {
        console.error(err);
      }
    })
  }

  private loadFourRandomArtworks(): void {
    this.artworkService.getRandomArtworks(this.NUMBER_OF_RANDOM_ARTWORKS).subscribe({
      next: (res: Artwork[]): void => {
        this.randomArtworks = res;
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err)
      }
    })
  }

  private subscribeToCheckboxChanges(): void {
    this.subscriptions.push(
      this.form.valueChanges.subscribe(() => {
        this.formsAreValid = this.checkIfFormsAreValid();
      })
    );
  }

  onRequestSubmitted(): void {
    if (this.checkIfFormsAreValid() && this.userService.user?.id && this.artwork?.id) {
      const formNotification: string = this.form.get("description")?.value;
      const artworkRequest: ArtworkRequest = {
        description: formNotification,
        userId: this.userService.user.id,
        artworkId: this.artwork.id
      }
      this.createArtworkRequest(artworkRequest);
    }
  }

  onRequestButtonClicked(): void {
    if (this.requestContainer) {
      this.requestContainer.nativeElement.scrollIntoView({behavior: 'smooth'});
    }
  }

  private checkIfFormsAreValid(): boolean {
    const formNotification: string = this.form.get("description")?.value;
    // const formConformation: string = this.form.get("confirmation")?.value;
    const formIsValid: boolean = this.form.valid;
    return formIsValid && !!formNotification; // && !!formConformation;
  }

  private createArtworkRequest(artworkRequest: ArtworkRequest) {
    this.artworkRequestService.createArtworkRequest(artworkRequest).subscribe({
      next: (): void => {
        if (this.artwork?.id) {
          void this.router.navigate(["kunstwerke"]);
          this.snackBarService.openSnackBarByJSON("info", "snackbar.artwork.requested", 6000);
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

  protected readonly RoleEnum = RoleEnum;
}
