import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { CitiesService } from '../services/cities.service';

@Component({
  selector: 'app-city-list',
  templateUrl: './city-list.component.html',
  styleUrls: ['./city-list.component.css'],
})
export class CityListComponent implements OnInit, OnDestroy {
  cities: string[] = [];
  form!: FormGroup;
  citiesSub$!: Subscription;

  constructor(private fb: FormBuilder, private citiesService: CitiesService) {}

  ngOnInit(): void {
    // initialize form
    this.form = this.createForm();

    // initalize cities by geting it from service
    this.cities = this.citiesService.getCities();

    // subscibe to any changes in cities (only gets cities when new city is added)
    this.citiesSub$ = this.citiesService.onCitiesChange.subscribe((res) => {
      this.cities = res;
    });
  }

  createForm() {
    return this.fb.group({
      city: this.fb.control('', [Validators.required]),
    });
  }

  submitForm() {
    // add new city to cities
    const newCity = this.form.get('city')?.value;

    // call service to add new city
    this.citiesService.addCity(newCity);
  }

  /*
  prevent duplicates
  2nd method: create a custom fn to check if form is valid
  */
  isFormInvalid() {
    // check new city already exists in cities list
    const newCity = this.form.get('city')?.value;

    // return true if no input or if city already exists
    return this.form.invalid || this.cities.includes(newCity);
  }

  ngOnDestroy(): void {
    this.citiesSub$.unsubscribe();
  }
}
