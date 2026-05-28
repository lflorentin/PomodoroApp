import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../../core/auth/auth.service';
import { PomodoroTimerService } from '../../../../core/services/pomodoro-timer.service';
import { NgIf } from '@angular/common';
import { UiStateService } from '../../../../core/services/ui.state.service';

@Component({
    standalone: true,
    selector: 'app-sidebar',
    imports: [RouterLink, RouterLinkActive, NgIf],
    templateUrl: './sidebar.component.html',
    styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent {


    private auth = inject(AuthService);
    private router = inject(Router);
    timer = inject(PomodoroTimerService);
    ui = inject(UiStateService)

    current = this.timer.current;
    displayTime = this.timer.displayTime;

    complete(event: Event) {
        event.stopPropagation();
        this.timer.complete();
    }

    stop(event: Event) {
        event.stopPropagation();
        this.timer.stop();
    }


    logout() {
        this.auth.logout();
        location.href = '/auth/login';
    }

    goToPomodoro() {
        this.router.navigate(['/dashboard/pomodoro']);
    }
}
