// Implement the methods in PizzaService for Task 3
// Add appropriate parameter and return type

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs';
import { OrderSummary } from './models';

@Injectable({
  providedIn: 'root',
})
export class PizzaService {
  constructor(private httpClient: HttpClient) {}

  // POST /api/order
  // Add any required parameters or return type

  createOrder(formData: FormData): Promise<any> {
    console.log('>>> submit order');
    console.table(formData);

    return lastValueFrom(this.httpClient.post('/api/order', formData));
  }

  // GET /api/order/<email>/all
  // Add any required parameters or return type
  getOrders(email: string): Promise<OrderSummary[]> {
    return lastValueFrom(
      this.httpClient.get<OrderSummary[]>(`/api/order/${email}/all`)
    );
  }
}
