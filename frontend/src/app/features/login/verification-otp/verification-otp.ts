import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink, Router } from '@angular/router';

@Component({
  selector: 'app-verification-otp',
  standalone: true,
  imports: [
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    RouterLink
  ],
  templateUrl: './verification-otp.html',
  styleUrl: './verification-otp.css',
})
export class VerificationOtp {
  otp: string = '';
  isLoading: boolean = false;

  constructor(private router: Router) {}

  onSubmit(event: Event) {
    event.preventDefault();
    if (this.otp.length < 6) return;
    
    this.isLoading = true;
    // Simulate API verification
    setTimeout(() => {
      this.isLoading = false;
      this.router.navigate(['/login']); // Navigate to reset or login
    }, 1500);
  }
}
