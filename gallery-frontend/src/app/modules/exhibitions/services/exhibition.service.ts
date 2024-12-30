import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environment";
import {Observable} from "rxjs";
import {Exhibition} from "../model/exhibition";
import {Artwork} from "../../artwork/model/artwork";

@Injectable({
  providedIn: 'root'
})
export class ExhibitionService {
  readonly endpoint: string = `${environment.apiUrl}/exhibition`;

  constructor(private readonly httpClient: HttpClient) {
  }

  getExhibitionById(id: number): Observable<Exhibition> {
    const url: string = `${this.endpoint}`;
    return this.httpClient.get<Exhibition>(url);
  }

  getExhibitions(): Observable<Exhibition[]> {
    const url: string = `${this.endpoint}/all`;
    return this.httpClient.get<Exhibition[]>(url);
  }

  getLatestExhibitions(): Observable<Exhibition> {
    const url: string = `${this.endpoint}/latest`;
    return this.httpClient.get<Exhibition>(url);
  }

  createExhibition(exhibition: Exhibition, file: File): Observable<Exhibition> {
    const formData: FormData = new FormData();
    const exhibitionBlob = new Blob([JSON.stringify(exhibition)], {type: 'application/json'});
    formData.append('exhibition', exhibitionBlob);
    formData.append('file', file);
    const url: string = `${this.endpoint}`;
    return this.httpClient.post<Exhibition>(url, formData);
  }

  updateExhibition(exhibition: Exhibition): Observable<Exhibition> {
    const url: string = `${this.endpoint}/update`;
    return this.httpClient.put<Exhibition>(url, exhibition);
  }

  updateExhibitionWithImage(exhibition: Exhibition, file: File): Observable<Exhibition> {
    const formData: FormData = new FormData();
    const exhibitionBlob = new Blob([JSON.stringify(exhibition)], {type: 'application/json'});
    formData.append('exhibition', exhibitionBlob);
    formData.append('file', file);
    const url: string = `${this.endpoint}/updateWithImage`;
    return this.httpClient.put<Exhibition>(url, formData);
  }

  deleteExhibition(id: number): Observable<void> {
    const url: string = `${this.endpoint}/${id}`;
    return this.httpClient.delete<void>(url);
  }
}
