import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class WeatherService {
  private OPEN_WEATHER_API_KEY = '';

  constructor(private httpClient: HttpClient) {}

  getWeather(city: string): Promise<any> {
    // define query params
    const params = new HttpParams()
      .set('q', city)
      .set('appid', this.OPEN_WEATHER_API_KEY);

    // send get request to OPEN WEATHER API
    // lastValueFrom converts Observable to Promise
    return lastValueFrom(
      this.httpClient.get('https://api.openweathermap.org/data/2.5/weather', {
        params: params,
      })
    );
  }
}
