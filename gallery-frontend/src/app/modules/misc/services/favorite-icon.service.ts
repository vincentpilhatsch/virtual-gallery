import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class FavoriteIconService {
  private favoriteArtworkSubject: BehaviorSubject<number> = new BehaviorSubject<number>(-1);
  favoriteArtworkObserver: Observable<number> = this.favoriteArtworkSubject.asObservable();

  removeArtworkFromFavorites(artworkId: number) {
    this.favoriteArtworkSubject.next(artworkId);
  }

  getFavoriteArtworkObserver(): Observable<number> {
    return this.favoriteArtworkObserver;
  }
}
