import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Weather } from '../models/weather.model';
import { WeatherService } from '../services/weather.service';

@Component({
  selector: 'app-weather-result',
  templateUrl: './weather-result.component.html',
  styleUrls: ['./weather-result.component.css'],
})
export class WeatherResultComponent implements OnInit, OnDestroy {
  weatherResult!: Weather;
  city!: string;
  routeSub$!: Subscription; // should unsubscribe when component is destroyed

  constructor(
    private weatherService: WeatherService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // set city to current route path variable
    this.routeSub$ = this.activatedRoute.params.subscribe((params) => {
      this.city = params['city'];
    });

    this.getWeather();
  }

  getWeather() {
    // call weather service to get weather from OPEN WEATHER API
    this.weatherService
      .getWeather(this.city) // returns a Promise
      .then((res) => {
        // promise resolve (successful)
        // map response to Weather model and assign to weatherResult
        this.weatherResult = {
          city: this.city,
          description: res.weather[0].description,
          temp: res.main.temp,
          humidity: res.main.humidity,
          windSpeed: res.wind.speed,
          windDegree: res.wind.deg,
        } as Weather;
        console.log(this.weatherResult);
      })
      .catch((err) => {
        console.log(err);
      });
  }

  ngOnDestroy(): void {
    this.routeSub$.unsubscribe();
  }
}
