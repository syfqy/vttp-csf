import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CityListComponent } from './components/city-list.component';
import { WeatherResultComponent } from './components/weather-result.component';

const routes: Routes = [
  { path: '', component: CityListComponent },
  { path: 'cities', component: CityListComponent },
  { path: 'weather/:city', component: WeatherResultComponent },
  { path: '**', redirectTo: '', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
