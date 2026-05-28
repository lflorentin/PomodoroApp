import { ApplicationConfig, APP_INITIALIZER } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';

import { routes } from './app.routes';
import { CORE_PROVIDERS } from './core/core.providers';
import { AuthService } from './core/auth/auth.service';
import { PomodoroTimerService } from './core/services/pomodoro-timer.service';

/* ---------------- AUTH INIT ---------------- */

export function initAuth(authService: AuthService) {
  return () => authService.init();
}

/* ---------------- POMODORO INIT ---------------- */

export function initPomodoro(timer: PomodoroTimerService) {
  return () => {
    timer.loadCurrent();
    timer.loadTasks();
  }
}

/* ---------------- APP CONFIG ---------------- */

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideAnimationsAsync(),
    ...CORE_PROVIDERS,

    {
      provide: APP_INITIALIZER,
      useFactory: initAuth,
      deps: [AuthService],
      multi: true
    },

    {
      provide: APP_INITIALIZER,
      useFactory: initPomodoro,
      deps: [PomodoroTimerService],
      multi: true
    }
  ]
};
