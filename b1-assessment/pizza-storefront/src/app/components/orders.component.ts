import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { OrderSummary } from '../models';
import { PizzaService } from '../pizza.service';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css'],
})
export class OrdersComponent implements OnInit, OnDestroy {
  email!: string;
  routeSub$!: Subscription;
  orders: OrderSummary[] = [];

  constructor(
    private pizzaService: PizzaService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.routeSub$ = this.activatedRoute.params.subscribe((params) => {
      this.email = params['email'];
    });

    this.getOrderSummary();
  }

  getOrderSummary(): void {
    this.pizzaService
      .getOrders(this.email)
      .then((res) => {
        this.orders = res;
      })
      .catch((err) => {
        console.error(err);
      });
  }

  ngOnDestroy(): void {
    this.routeSub$.unsubscribe();
  }
}
