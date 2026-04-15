import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router'; // Import this

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet // Use RouterOutlet instead of Login
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
}
