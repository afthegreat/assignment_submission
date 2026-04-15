// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { roleGuard } from './core/guards/role-guard';
import { Login } from './login/login';

export const routes: Routes = [
  { path: 'login', component: Login },

  // Student Area
  {
    path: 'student',
    loadComponent: () => import('./features/student/student').then(m => m.Student),
    canActivate: [() => roleGuard('ROLE_STUDENT')]
  },

  // Teacher Area
  {
    path: 'teacher',
    loadComponent: () => import('./features/teacher/teacher').then(m => m.Teacher),
    canActivate: [() => roleGuard('ROLE_TEACHER')]
  },

  // Admin Area
  {
    path: 'admin',
    loadComponent: () => import('./features/admin/admin').then(m => m.Admin),
    canActivate: [() => roleGuard('ROLE_ADMIN')]
  },

  { path: '', redirectTo: 'login', pathMatch: 'full' }
];
