import { Component, OnInit } from '@angular/core';
import {
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { PizzaService } from '../pizza.service';

const SIZES: string[] = [
  'Personal - 6 inches',
  'Regular - 9 inches',
  'Large - 12 inches',
  'Extra Large - 15 inches',
];

const PizzaToppings: string[] = [
  'chicken',
  'seafood',
  'beef',
  'vegetables',
  'cheese',
  'arugula',
  'pineapple',
];

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
})
export class MainComponent implements OnInit {
  pizzaSize = SIZES[0];
  pizzaToppings: string[] = [];
  form!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private pizzaService: PizzaService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.createForm();
  }

  createForm(): FormGroup {
    return this.fb.group({
      name: this.fb.control('', [Validators.required]),
      email: this.fb.control('', [Validators.required, Validators.email]),
      pizzaSize: this.fb.control(1, [Validators.required]),
      base: this.fb.control('', [Validators.required]),
      pizzaSauce: this.fb.control('', [Validators.required]),
      pizzaToppings: this.fb.array([]),
      comments: this.fb.control(''),
    });
  }

  updateSize(size: string) {
    this.pizzaSize = SIZES[parseInt(size)];
  }

  updateToppings(event: Event) {
    const target = event.target as HTMLInputElement;
    if (target.checked) {
      this.pizzaToppings = [...this.pizzaToppings, target.value];
    } else {
      this.pizzaToppings = this.pizzaToppings.filter((t) => t !== target.value);
    }
    console.log(this.pizzaToppings);
  }

  listOrders(): void {
    this.router.navigate(['orders', this.form.get('email')?.value]);
  }

  isFormInvalid(): boolean {
    return this.form.invalid || this.pizzaToppings.length < 1;
  }

  isEmailInvalid(): boolean {
    return (this.form.get('email') as FormControl).invalid;
  }

  submitForm(): void {
    const order = this.form.value;
    order['pizzaToppings'] = this.pizzaToppings.toString();
    const formData = order as FormData;

    this.pizzaService
      .createOrder(formData)
      .then((res) => {
        console.log(res);
        this.listOrders();
      })
      .catch((err) => console.error(err));
  }
}
