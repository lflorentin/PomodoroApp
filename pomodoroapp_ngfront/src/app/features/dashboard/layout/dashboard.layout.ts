import { Component, inject } from '@angular/core';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/auth/auth.service';
import { SidebarComponent } from '../components/sidebar/sidebar.component';
import { UiStateService } from '../../../core/services/ui.state.service';
import { filter } from 'rxjs';

@Component({
    standalone: true,
    selector: 'app-dashboard-layout',
    imports: [CommonModule, RouterOutlet, SidebarComponent],
    templateUrl: './dashboard.layout.html',
    styleUrl: './dashboard.layout.scss'
})
export class DashboardLayout {
    private auth = inject(AuthService);
    private router = inject(Router);
    ui = inject(UiStateService)

    readonly user = this.auth.user;

    isPomodoro = false;

    constructor() {
        this.router.events
            .pipe(filter(e => e instanceof NavigationEnd))
            .subscribe((e: any) => {
                this.isPomodoro = e.url.includes('/pomodoro');
            });
    }


    logout() {
        this.auth.logout();
        this.router.navigate(['/auth/login']);
    }
}
