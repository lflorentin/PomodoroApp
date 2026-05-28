import { Routes } from '@angular/router';
import { DashboardLayout } from './layout/dashboard.layout';

export const dashboardRoutes: Routes = [
    {
        path: '',
        component: DashboardLayout,
        children: [
            {
                path: '',
                loadComponent: () =>
                    import('./pages/home.page')
                        .then(m => m.HomePage),
            }
            ,
            {
                path: 'tasks',
                loadChildren: () =>
                    import('../tasks/tasks.routes')
                        .then(m => m.tasksRoutes),
            }
            ,
            {
                path: 'pomodoro',
                loadChildren: () =>
                    import('../pomodoro/pomodoro.routes')
                        .then(m => m.pomodoroRoutes),
            }
            ,
            {
                path: 'settings',
                loadChildren: () =>
                    import('../settings/settings.routes')
                        .then(m => m.settingsRoutes),
            },
        ],
    },
];
