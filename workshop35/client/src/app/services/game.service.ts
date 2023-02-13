import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class GameService {
  constructor(private httpClient: HttpClient) {}

  getGames(limit: number, offset: number): Promise<any> {
    // set limit and offset params
    const params = new HttpParams().set('limit', limit).set('offset', offset);

    // make GET request to spring boot
    return lastValueFrom(
      this.httpClient.get('http://localhost:8080/games', { params: params })
    );
  }
}
