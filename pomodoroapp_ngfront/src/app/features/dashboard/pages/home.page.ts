import { Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { forkJoin } from 'rxjs';

import { StatsMeApi, StatJourneeMeDto } from '../data/stats-me.api';
import { SessionsMeApi, SessionPomodoroMeDto } from '../data/sessions-me.api';
import { TasksMeApi, TacheMeDto } from '../data/tasks-me.api';
import { RouterLink } from "@angular/router";
import { ParamsMeApi, ParamsMeDto } from '../../settings/data/params-me.api';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
  <div class="home">
    <div class="header">
      <div>
        <h1>Dashboard</h1>
        <p class="muted">Vue rapide de ta journée</p>
      </div>
      <button class="btn" (click)="reload()" [disabled]="loading()">Rafraîchir</button>
    </div>

    <div class="grid">

      <!-- Card Stats -->
      <section class="card">
        <h2>Aujourd’hui</h2>

        <ng-container *ngIf="stats(); else statsLoading">
          <div class="kpis">
            <div class="kpi">
              <div class="label">Sessions</div>
              <div class="value">{{ stats()!.nbSessions }}</div>
            </div>
            <div class="kpi">
              <div class="label">Temps total</div>
              <div class="value">{{ formatMinutes(stats()!.tempsTotal) }}</div>
            </div>
            <div class="kpi">
              <div class="label">Objectif</div>
              <div class="value">
                {{ completedWorkSessions() }} / {{ OBJECTIF_SESSIONS() }}
              </div>
            </div>
          </div>

          <div class="progress">
            <div class="bar">
              <div class="fill" [style.width.%]="progressPct()"></div>
            </div>
            <div class="muted small">{{ progressPct() }}%</div>
          </div>
        </ng-container>

        <ng-template #statsLoading>
          <p class="muted">Chargement…</p>
        </ng-template>
      </section>

      <!-- Card Current Session -->
      <section class="card">
        <h2>Session en cours</h2>

        <ng-container *ngIf="currentSessionLoaded(); else currentLoading">
          <ng-container *ngIf="currentSession(); else noCurrent">
            <div class="row">
              <span class="pill">{{ currentSession()!.type }}</span>
              <span class="pill">{{ currentSession()!.status }}</span>
            </div>
            <div class="big">
              {{ currentSession()!.duree }} min
            </div>
            <div class="muted small" *ngIf="currentSession()!.tacheId">
              Liée à la tâche #{{ currentSession()!.tacheId }}
            </div>
          </ng-container>

          <ng-template #noCurrent>
            <p class="muted">Aucune session active.</p>
          </ng-template>
        </ng-container>

        <ng-template #currentLoading>
          <p class="muted">Chargement…</p>
        </ng-template>
      </section>

      <!-- Card Tasks -->
      <section class="card">
        <div class="card-head">
          <h2>Tâches</h2>
          <button class="btn ghost" type="button" routerLink="/dashboard/tasks">Voir tout</button>
        </div>

        <ng-container *ngIf="tasks(); else tasksLoading">
          <ng-container *ngIf="tasks()!.length > 0; else noTasks">
            <ul class="list">
              <li *ngFor="let t of topTasks()">
                <span class="dot" [class.done]="t.validation"></span>
                <div class="txt">
                  <div class="title">{{ t.titre }}</div>
                  <div class="muted small" *ngIf="t.description">{{ t.description }}</div>
                </div>
              </li>
            </ul>
          </ng-container>

          <ng-template #noTasks>
            <p class="muted">Aucune tâche pour le moment.</p>
          </ng-template>
        </ng-container>

        <ng-template #tasksLoading>
          <p class="muted">Chargement…</p>
        </ng-template>
      </section>
    </div>
  </div>
  `,
  styles: [`
    .home { display: flex; flex-direction: column; gap: 1rem; }
    .header { display: flex; justify-content: space-between; align-items: end; gap: 1rem; }
    h1 { margin: 0; font-size: 1.4rem; }
    h2 { margin: 0 0 .75rem 0; font-size: 1.05rem; }
    .muted { color: #667085; margin: .25rem 0 0 0; }
    .small { font-size: .85rem; }
    .grid { display: grid; grid-template-columns: 1fr; gap: 1rem;}
    @media (min-width: 900px) { .grid { grid-template-columns: 1.2fr .8fr; } }
    @media (min-width: 1200px) { .grid { grid-template-columns: 1.2fr .8fr; } }

    .card { background: #ecababff; border-radius: 14px; padding: 1rem; box-shadow: 0 1px 10px rgba(16,24,40,.06); }
    .card-head { display:flex; align-items:center; justify-content:space-between; gap: 1rem; margin-bottom: .75rem; }

    .btn { border: 1px solid #d0d5dd; background: #fff; border-radius: 10px; padding: .5rem .75rem; cursor: pointer; }
    .btn:disabled { opacity: .6; cursor: not-allowed; }
    .btn.ghost { background: transparent; border-color: transparent; color: #344054; }

    .kpis { display:grid; grid-template-columns: repeat(3, 1fr); gap: .75rem; }
    .kpi { background:#f9fafb; border:1px solid #eaecf0; border-radius: 12px; padding: .75rem; }
    .label { font-size: .8rem; color:#667085; }
    .value { font-size: 1.25rem; font-weight: 700; margin-top: .25rem; }

    .progress { display:flex; align-items:center; justify-content:space-between; gap:.75rem; margin-top: .75rem; }
    .bar { flex:1; height: 10px; background:#f2f4f7; border-radius: 999px; overflow:hidden; border:1px solid #eaecf0; }
    .fill { height: 100%; background:#12b76a; width:0%; }

    .row { display:flex; gap: .5rem; margin-bottom: .75rem; flex-wrap: wrap; }
    .pill { font-size:.8rem; padding:.25rem .5rem; background:#f2f4f7; border:1px solid #eaecf0; border-radius: 999px; color:#344054; }
    .big { font-size: 1.6rem; font-weight: 800; }

    .list { list-style:none; padding:0; margin:0; display:flex; flex-direction:column; gap:.75rem; }
    .list li { display:flex; align-items:flex-start; gap:.75rem; }
    .dot { width: 12px; height: 12px; border-radius: 50%; background:#f2f4f7; border:1px solid #d0d5dd; margin-top: .25rem; }
    .dot.done { background:#12b76a; border-color:#12b76a; }
    .title { font-weight: 600; }
  `]
})
export class HomePage {
  private statsApi = inject(StatsMeApi);
  private sessionsApi = inject(SessionsMeApi);
  private tasksApi = inject(TasksMeApi);
  private paramsApi = inject(ParamsMeApi);

  readonly OBJECTIF_SESSIONS = computed(() => this.params()?.objectifSessions ?? 8);


  loading = signal(false);

  stats = signal<StatJourneeMeDto | null>(null);
  currentSession = signal<SessionPomodoroMeDto | null>(null);
  currentSessionLoaded = signal(false);
  tasks = signal<TacheMeDto[] | null>(null);
  todaySessions = signal<SessionPomodoroMeDto[] | null>(null);
  params = signal<ParamsMeDto | null>(null);

  topTasks = computed(() => (this.tasks() ?? []).slice(0, 5));

  completedWorkSessions = computed(() => {
    const sessions = this.todaySessions();
    if (!sessions) return 0;

    return sessions.filter(
      s => s.type === 'travail' && s.status === 'completee'
    ).length;
  });



  progressPct = computed(() => {
    const done = this.completedWorkSessions();
    const total = this.OBJECTIF_SESSIONS();
    if (total <= 0) return 0;

    const pct = Math.round((done / total) * 100);
    return Math.max(0, Math.min(100, pct));
  });


  constructor() {
    this.reload();
  }

  reload() {
    this.loading.set(true);
    this.currentSessionLoaded.set(false);

    forkJoin({
      stats: this.statsApi.getToday(),
      current: this.sessionsApi.getCurrent(),
      tasks: this.tasksApi.getAll(),
      sessions: this.sessionsApi.getToday(),
      params: this.paramsApi.get()
    }).subscribe({
      next: ({ stats, current, tasks, sessions, params }) => {
        this.stats.set(stats);
        this.currentSession.set(current);
        this.todaySessions.set(sessions);
        this.currentSessionLoaded.set(true);
        this.tasks.set(tasks);
        this.loading.set(false);
        this.params.set(params)
      },
      error: () => {
        this.loading.set(false);
        this.currentSessionLoaded.set(true);
      },
    });
  }

  formatMinutes(min: number) {
    if (min == null) return '-';
    const h = Math.floor(min / 60);
    const m = min % 60;
    if (h <= 0) return `${m} min`;
    if (m === 0) return `${h} h`;
    return `${h} h ${m} min`;
  }
}
