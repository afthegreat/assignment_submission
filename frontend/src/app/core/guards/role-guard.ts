// src/app/core/guards/role.guard.ts
import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth-service';

export const roleGuard = (requiredRole: string): CanActivateFn => {
  return () => {
    const authService = inject(AuthService);
    const router = inject(Router);
    const user = authService.currentUser();

    // Check if user exists and has the required role
    if (user && user.authorities.includes(requiredRole)) {
      return true;
    }

    // Redirect to login or "Unauthorized" page if they don't have access
    return router.parseUrl('/login');
  };
};
