import {Component, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatDivider} from "@angular/material/divider";
import {MatList, MatListItem} from "@angular/material/list";
import {NgOptimizedImage} from "@angular/common";
import {MatCheckbox} from "@angular/material/checkbox";
import {FavoriteIconComponent} from "../../misc/favorite-icon/favorite-icon.component";
import {UserService} from "../../user/services/user.service";
import {ActivatedRoute} from "@angular/router";
import {ArtworkService} from "../services/artwork.service";
import {forkJoin, Subscription} from "rxjs";
import {Artwork} from "../model/artwork";
import {HttpErrorResponse} from "@angular/common/http";
import {MoneyConverterPipe} from "../pipes/money-converter.pipe";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";

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

  form: FormGroup = this.fb.group({
    notification: ['', [
      Validators.required,
    ]],
    confirmation: ['', [
      Validators.required,
    ]]
  });

  subscriptions: Subscription[] = [];

  constructor(protected userService: UserService,
              private readonly artworkService: ArtworkService,
              private readonly route: ActivatedRoute,
              private readonly fb: FormBuilder) {
  }

  ngOnInit() {
    this.subscribeToUser();
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
    this.route.paramMap.subscribe(params => {
      const id: string | null = params.get('id');
      if (id) {
        const convertedId = Number(id);
        this.loadArtworkAndFavoriteIds(convertedId, email);
      }
    });
  }

  private loadArtworkAndFavoriteIds(id: number, email: string): void {
    forkJoin({
      artwork: this.artworkService.getArtworkById(id),
      favoriteIds: this.userService.getFavoriteArtworkIdsByEmail(email)
    }).subscribe({
      next: (res: { artwork: Artwork, favoriteIds: number[] }): void => {
        this.artwork = res.artwork;
        this.isFavorite = res.favoriteIds.includes(this.artwork.id);
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    });
  }

  onRequestSubmitted(): void {
    if (this.checkIfFormsAreValid()) { // TODO: Comment in for Live
      const formNotification: string = this.form.get("notification")?.value;
      console.log(formNotification);
    }
  }

  private checkIfFormsAreValid(): boolean {
    const formNotification: string = this.form.get("notification")?.value;
    const formIsValid: boolean = this.form.valid;
    return formIsValid && !!formNotification;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => {
      if (sub) {
        sub.unsubscribe();
      }
    })
  }
}
