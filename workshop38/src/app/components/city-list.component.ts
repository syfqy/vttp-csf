import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { City } from '../models/city.model';
import { CitiesService } from '../services/cities.service';

@Component({
  selector: 'app-city-list',
  templateUrl: './city-list.component.html',
  styleUrls: ['./city-list.component.css'],
})
export class CityListComponent implements OnInit {
  cities: City[] = [];
  form!: FormGroup;

  constructor(private fb: FormBuilder, private citiesService: CitiesService) {}

  ngOnInit(): void {
    // initialize form
    this.form = this.createForm();

    // initalize cities by geting it from IndexDB
    this.citiesService
      .getCities()
      .then((data) => (this.cities = data))
      .catch(() => console.log('cannot retrieve cities from IndexDB'));
  }

  createForm() {
    return this.fb.group({
      city: this.fb.control('', [Validators.required]),
    });
  }

  submitForm() {
    // add new city to cities
    const newCity = {
      name: this.form.get('city')?.value,
    } as City; // cast Object as City

    // call service to add new city
    this.citiesService.addCity(newCity);

    // retrive new list of cities
    this.ngOnInit();
  }

  /*
  prevent duplicates
  2nd method: create a custom fn to check if form is valid
  */
  isFormInvalid() {
    // check new city already exists in cities list
    const cityName = this.form.get('city')?.value;

    // returns City object if City object matches new name, otherwise returns undefined
    // (!!) City -> true
    // undefined -> false
    const isCityInList: boolean = !!this.cities.find(
      (c) => c.name === cityName
    );

    // return true if no input or if city already exists
    return this.form.invalid || isCityInList;
  }
}
