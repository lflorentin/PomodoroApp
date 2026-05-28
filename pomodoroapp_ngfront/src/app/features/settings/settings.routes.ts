import { Routes } from '@angular/router';

export const settingsRoutes: Routes = [
    {
        path: '',
        loadComponent: () =>
            import('./pages/params.page').then(m => m.ParamsPage)
    }
];
