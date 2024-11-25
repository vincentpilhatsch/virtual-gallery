import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Artwork} from "../model/artwork";
import {environment} from "../../environment";

@Injectable({
  providedIn: 'root'
})
export class ArtworkService {
  readonly endpoint: string = `${environment.apiUrl}/artwork`;

  constructor(private readonly httpClient: HttpClient) {
  }

  getArtworks(): Observable<Artwork[]> {
    const url: string = `${this.endpoint}/all`;
    return this.httpClient.get<Artwork[]>(url);
  }

  getArtworkById(id: number): Observable<Artwork> {
    const url: string = `${this.endpoint}/${id}`;
    return this.httpClient.get<Artwork>(url);
  }

  getRandomArtworks(numberOfArtworks: number) {
    const url: string = `${this.endpoint}/random?`;
    const params: HttpParams = new HttpParams()
      .set("numberOfArtworks", numberOfArtworks);
    return this.httpClient.get<Artwork[]>(url + params);
  }
}
