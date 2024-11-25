import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environment";
import {ArtworkRequest} from "../model/artworkRequest";

@Injectable({
  providedIn: 'root'
})
export class ArtworkService {
  readonly endpoint: string = `${environment.apiUrl}/artwork-request`;

  constructor(private readonly httpClient: HttpClient) {
  }

  getArtworks(): Observable<ArtworkRequest[]> {
    const url: string = `${this.endpoint}/all`;
    return this.httpClient.get<ArtworkRequest[]>(url);
  }

  getArtworkById(id: number): Observable<ArtworkRequest> {
    const url: string = `${this.endpoint}/${id}`;
    return this.httpClient.get<ArtworkRequest>(url);
  }
}
