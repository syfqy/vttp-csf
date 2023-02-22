import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './components/main.component';
import { OrdersComponent } from './components/orders.component';

const routes: Routes = [
  { path: '', component: MainComponent },
  { path: 'orders/:email', component: OrdersComponent },
  { path: '**', redirectTo: '', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
