import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { tap, catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { CookieService } from 'ngx-cookie-service'; // Import this

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private cookieService = inject(CookieService); // Inject the service
  private readonly API_URL = 'http://localhost:8080/auth/login';

  // State management
  currentUser = signal<any>(null);
  isLoading = signal<boolean>(false);
  error = signal<string | null>(null);

  login(credentials: { username: string; password: any }) {
    this.isLoading.set(true);
    this.error.set(null);

    return this.http.post<any>(this.API_URL, credentials).pipe(
      tap({
        next: (response) => {
          // 1. Store the Access Token in a Cookie
          // Params: (name, value, expires (days), path)
          this.cookieService.set('access_token', response.accessToken, 15 / 1440, '/');

          // 2. Extract user info from the JWT payload
          // JWTs are [Header].[Payload].[Signature]. We take index [1].
          const payloadBase64 = response.accessToken.split('.')[1];
          const decodedPayload = JSON.parse(atob(payloadBase64));
          console.log('Full JWT Payload:', decodedPayload);
          console.log('Username (sub):', decodedPayload.sub);
          console.log('Roles (authorities):', decodedPayload.authorities);
          this.currentUser.set(decodedPayload);
          this.isLoading.set(false);
        },
        error: (err) => {
          // Map your backend error "message" field
          this.error.set(err.error?.message || 'Login failed');
          this.isLoading.set(false);
        }
      })
    );
  }

  logout() {
    this.cookieService.delete('access_token', '/');
    this.currentUser.set(null);
  }
}
