import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environment";
import {ArtworkRequest} from "../model/artworkRequest";
import {CompleteArtworkRequest} from "../model/completeArtworkRequest";

@Injectable({
  providedIn: 'root'
})
export class ArtworkRequestService {
  readonly endpoint: string = `${environment.apiUrl}/artwork-request`;

  constructor(private readonly httpClient: HttpClient) {
  }

  getArtworkRequests(): Observable<ArtworkRequest[]> {
    const url: string = `${this.endpoint}/all`;
    return this.httpClient.get<ArtworkRequest[]>(url);
  }

  getArtworkRequestById(id: number): Observable<ArtworkRequest> {
    const url: string = `${this.endpoint}/${id}`;
    return this.httpClient.get<ArtworkRequest>(url);
  }

  getCompleteArtworkRequests(): Observable<CompleteArtworkRequest[]> {
    const url: string = `${this.endpoint}/allComplete`;
    return this.httpClient.get<CompleteArtworkRequest[]>(url);
  }

  createArtworkRequest(artworkRequest: ArtworkRequest): Observable<ArtworkRequest> {
    const url: string = `${this.endpoint}`;
    return this.httpClient.post<ArtworkRequest>(url, artworkRequest);
  }

  acceptArtworkRequest(artworkRequest: ArtworkRequest): Observable<ArtworkRequest> {
    const url: string = `${this.endpoint}/accept`;
    return this.httpClient.post<ArtworkRequest>(url, artworkRequest);
  }


  rejectArtworkRequest(artworkRequest: ArtworkRequest): Observable<ArtworkRequest> {
    const url: string = `${this.endpoint}/reject`;
    return this.httpClient.post<ArtworkRequest>(url, artworkRequest);
  }

  deleteArtworkRequest(id: number): Observable<void> {
    const url: string = `${this.endpoint}/${id}`;
    return this.httpClient.delete<void>(url);
  }

}
