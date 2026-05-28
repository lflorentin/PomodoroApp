import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
    {
        path: 'auth',
        loadChildren: () =>
            import('./features/auth/auth.routes').then(m => m.authRoutes),
    },
    {
        path: '',
        canActivateChild: [authGuard],
        children: [
            {
                path: '',
                pathMatch: 'full',
                redirectTo: 'dashboard',
            },
            {
                path: 'dashboard',
                loadChildren: () =>
                    import('./features/dashboard/dashboard.routes')
                        .then(m => m.dashboardRoutes),
            },
        ],
    },
];
