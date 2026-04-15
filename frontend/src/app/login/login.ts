import { Component, inject } from "@angular/core";
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms'; // For handling input data
import { AuthService } from "../core/services/auth-service";
import { CommonModule } from "@angular/common";
@Component({
  selector: "app-login",
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    FormsModule
  ],
  templateUrl: "./login.html",
  styleUrl: "./login.css"
})
export class Login {
  authService = inject(AuthService);

  //local state for the form
  username = '';
  password = '';

  onLogin() {
    if (this.username && this.password) {
      this.authService.login({
        username: this.username,
        password: this.password
      }).subscribe({
        next: () => {
          console.log('Login successful');
        },
        error: (err) => {
          console.log('Login failed', err);
        }
      })
    }
  }
}