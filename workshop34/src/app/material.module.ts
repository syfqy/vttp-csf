import { NgModule } from '@angular/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';

const materialModule = [
  MatFormFieldModule,
  MatButtonModule,
  MatInputModule,
  MatCardModule,
];

@NgModule({
  declarations: [],
  imports: [materialModule],
  exports: [materialModule],
})
export class MaterialModule {}
