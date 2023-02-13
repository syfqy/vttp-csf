import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Weather } from './models/weather.model';
import { WeatherService } from './services/weather.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  form!: FormGroup;
  weatherResult!: Weather;

  constructor(
    private fb: FormBuilder,
    private weatherService: WeatherService
  ) {}

  ngOnInit(): void {
    this.form = this.createForm();
  }

  createForm() {
    return this.fb.group({
      city: this.fb.control('', [Validators.required]),
    });
  }

  submitForm() {
    // call weather service to get weather from OPEN WEATHER API
    const city = this.form.get('city')?.value;
    console.log('submit form city: ' + city);

    this.weatherService
      .getWeather(city) // returns a Promise
      .then((res) => {
        // promise resolve (successful)
        // map response to Weather model and assign to weatherResult
        this.weatherResult = {
          city: city,
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

    /*
    {
    "coord": {
        "lon": -0.1257,
        "lat": 51.5085
    },
    "weather": [
        {
            "id": 804,
            "main": "Clouds",
            "description": "overcast clouds",
            "icon": "04n"
        }
    ],
    "base": "stations",
    "main": {
        "temp": 280.33,
        "feels_like": 279,
        "temp_min": 279.26,
        "temp_max": 281.13,
        "pressure": 1038,
        "humidity": 86
    },
    "visibility": 10000,
    "wind": {
        "speed": 2.06,
        "deg": 110
    },
    "clouds": {
        "all": 100
    },
    "dt": 1676185626,
    "sys": {
        "type": 2,
        "id": 2019646,
        "country": "GB",
        "sunrise": 1676186460,
        "sunset": 1676221719
    },
    "timezone": 0,
    "id": 2643743,
    "name": "London",
    "cod": 200
}
    */
  }
}
