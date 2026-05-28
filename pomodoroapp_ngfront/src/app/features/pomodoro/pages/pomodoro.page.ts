import { Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
  SessionsMeApi,
  SessionPomodoroMeCreateDto
} from '../../dashboard/data/sessions-me.api';

import { PomodoroTimerService } from '../../../core/services/pomodoro-timer.service';
import { TacheMeDto, TasksMeApi } from '../../tasks/data/tasks-me.api';
import { ParamsMeApi, ParamsMeDto } from '../../settings/data/params-me.api';

type SessionChoice = 'travail' | 'pause_courte' | 'pause_longue';

@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
  <div class="pomodoro"  [style.backgroundImage]="backgroundImage()">

    <button class="bg-btn" *ngIf="!backgroundImage()" (click)="fileInput.click()">
      🖼️
    </button>
    <button
      class="bg-btn reset"
      *ngIf="backgroundImage()"
      (click)="clearBackground()">
      ✖
    </button>

    <input
      #fileInput
      type="file"
      accept="image/*"
      hidden
      (change)="onBackgroundSelected($event)"
    />

    <div class="focus-container">

    <div class="timer-block"
     (click)="onMainClick()"
     [class.clickable]="!current()">

      <!-- TYPE ACTUEL -->
      <div class="current-type" *ngIf="current()">
        {{ current()!.type | uppercase }}
      </div>

      <!-- TYPE SELECTOR -->
      <div class="type-selector" *ngIf="!current()">
        <button
          *ngFor="let t of sessionTypes"
          (click)="selectType(t.value, $event)"
          [class.active]="selectedType() === t.value">
          {{ t.label }}
        </button>
      </div>

      <!-- TIMER -->
      <div class="timer">
        {{ displayTimeFinal() }}
      </div>

      <div class="hint" *ngIf="!current()">
        Cliquer pour démarrer
      </div>

      <div class="task-linked" *ngIf="task()">
          🎯 {{ task()!.titre }}
        </div>
    </div>


      <!-- TASK PICKER -->
      <div class="task-picker" *ngIf="!current() && tasks().length">
        <span class="icon">🎯</span>

        <select
          [value]="selectedTaskId()"
          (change)="selectedTaskId.set($any($event.target).value || null)">
          <option value="">Aucune tâche</option>
          <option *ngFor="let t of tasks()" [value]="t.idTache">
            {{ t.titre }}
          </option>
        </select>
      </div>


      <!-- SESSION ACTIVE -->
      <ng-container *ngIf="current()">

        <div class="actions">
          <button class="btn ghost" (click)="stop()">✖</button>
          <button class="btn primary" (click)="complete()">Terminer</button>
        </div>
      </ng-container>

    </div>
  </div>
  `,
  styles: [`
    .pomodoro {
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #0f172a;
      background-repeat: no-repeat;
      background-size: cover;
      background-position: center;
    }

    .bg-btn {
      position: absolute;
      top: 1rem;
      right: 1rem;

      background: rgba(17, 15, 15, 0.85);
      border-color:rgba(196, 44, 44, 0.85);
      border-radius: 999px;
      padding: .5rem .6rem;
      cursor: pointer;
      font-size: 1.1rem;

      box-shadow: 0 4px 12px rgba(0,0,0,.15);
      transition: transform .15s ease, opacity .15s ease;
    }

    .bg-btn:hover {
      transform: scale(1.05);
      opacity: .9;
    }

    .bg-btn.reset {
      right: 1rem;
      width:2.7rem;
      color:white;
      opacity:0.6;
    }


    .focus-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 1.5rem;
    }

    .timer-block.clickable { cursor: pointer; }

    .type { margin-top: .5rem; font-size: .8rem; color: #667085; }
    .hint { color: #98a2b3; font-size: .9rem; }
    .timer-block {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: .75rem;
      position: relative;
      z-index: 1;
      color: #fff;
      text-shadow:
        0 2px 12px rgba(0,0,0,.8),
        0 0 40px rgba(0,0,0,.4);
      background: rgba(255,255,255,.08);
      backdrop-filter: blur(0px);
      padding: 1.5rem 2.5rem;
      border-radius: 24px;

    }

    .current-type {
      font-size: .75rem;
      letter-spacing: .2em;
      text-transform: uppercase;
      color: #98a2b3;
    }

    .type-selector {
      display: flex;
      gap: .5rem;
      margin-bottom: .5rem;
    }

    .type-selector button {
      padding: .35rem .8rem;
      border-radius: 999px;
      border: 1px solid #d0d5dd;
      background: #fff;
      font-size: .75rem;
      cursor: pointer;
      opacity: .80;
      transition: all .15s ease;
    }

    .type-selector button:hover {
      opacity: .90;
    }

    .type-selector button.active {
      background: #111827;
      color: #fff;
      border-color: #111827;
      opacity: 1;
    }

    .timer {
      font-size: clamp(5.5rem, 10vw, 9rem);
      font-weight: 900;
      line-height: 1;
      letter-spacing: -0.03em;
      opacity: .85;
      position: relative;
      z-index: 1;

      color: #fff;
      text-shadow:
        0 2px 12px rgba(0,0,0,.8),
        0 0 40px rgba(0,0,0,.4);

      background: rgba(255,255,255,.08);
      backdrop-filter: blur(10px);
      padding: 1.5rem 2.5rem;
      border-radius: 24px;
    }

    .timer-block.clickable .timer {
      opacity: 0.85;
    }

    .hint {
      margin-top: .25rem;
      font-size: 1rem;
      color:white;
      text-shadow:
        0 2px 12px rgba(0,0,0,.8),
        0 0 40px rgba(0,0,0,.4);
    }

    .task-picker {
      display: inline-flex;
      align-items: center;
      gap: .5rem;
      padding: .45rem .8rem;
      border-radius: 999px;
      background: #f9fafb;
      border: 1px solid #e4e7ec;
      font-size: .8rem;
      color: #4e535aff;
    }

    .task-picker .icon {
      opacity: .7;
    }

    .task-picker select {
      border: none;
      background: transparent;
      outline: none;
      cursor: pointer;
      font-size: .8rem;
      color: inherit;
      max-width: 180px;
    }

    .actions {
      display: flex;
      gap: 1rem;
    }

    .btn {
      padding: .6rem 1.2rem;
      border-radius: 999px;
      border: 1px solid #d0d5dd;
      cursor: pointer;
    }

    .btn.primary {
      background: #12b76a;
      color: #fff;
    }

    .btn.ghost {
      background: red;
      border-color: white;
      font-weight: bold;
      color:white;
    }
  `]
})
export class PomodoroPage {

  private sessionsApi = inject(SessionsMeApi);
  private tasksApi = inject(TasksMeApi);
  private timer = inject(PomodoroTimerService);
  private paramsApi = inject(ParamsMeApi);

  sessionTypes = [
    { value: 'travail' as const, label: '🍅 Travail' },
    { value: 'pause_courte' as const, label: '☕ Pause' },
    { value: 'pause_longue' as const, label: '🌿 Pause longue' },
  ];

  params = signal<ParamsMeDto | null>(null);
  tasks = signal<TacheMeDto[]>([]);
  selectedTaskId = signal<number | null>(null);
  selectedType = signal<SessionChoice>('travail');

  current = this.timer.current;
  displayTime = this.timer.displayTime;

  constructor() {
    this.loadTasks();
    this.paramsApi.get().subscribe(p => this.params.set(p));
    const savedBg = localStorage.getItem('pomodoro-bg');
    if (savedBg) {
      this.backgroundImage.set(`url(${savedBg})`);
    }

  }

  backgroundImage = signal<string | null>(null);

  private bgObjectUrl: string | null = null;

  onBackgroundSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files || !input.files.length) return;

    const file = input.files[0];
    if (this.bgObjectUrl) {
      URL.revokeObjectURL(this.bgObjectUrl);
    }

    this.bgObjectUrl = URL.createObjectURL(file);
    this.backgroundImage.set(`url(${this.bgObjectUrl})`);

    input.value = '';
  }

  clearBackground() {
    if (this.bgObjectUrl) {
      URL.revokeObjectURL(this.bgObjectUrl);
      this.bgObjectUrl = null;
    }

    this.backgroundImage.set(null);
  }

  selectedDuration = computed(() => {
    const p = this.params();
    if (!p) return 25;

    switch (this.selectedType()) {
      case 'travail': return p.dureeTravail;
      case 'pause_courte': return p.dureePauseCourte;
      case 'pause_longue': return p.dureePauseLongue;
      default: return 25;
    }
  });

  backendType = computed<'travail' | 'pause'>(() =>
    this.selectedType() === 'travail' ? 'travail' : 'pause'
  );

  task = computed(() => {
    const s = this.current();
    return s?.tacheId
      ? this.tasks().find(t => t.idTache === s.tacheId) ?? null
      : null;
  });

  previewSeconds = computed(() => {
   
    if (this.current()) return null;
    const minutes = this.selectedDuration();
    return minutes * 60;
  });

  displayTimeFinal = computed(() => {
    if (this.current()) {
      return this.displayTime();
    }

    const s = this.previewSeconds();
    if (s == null) return '00:00';

    const m = Math.floor(s / 60);
    const sec = s % 60;
    return `${m.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}`;
  });


  selectType(type: SessionChoice, event: MouseEvent) {
    event.stopPropagation();
    this.selectedType.set(type);
    this.selectedTaskId.set(null);
  }

  onMainClick() {
    if (this.current()) return;

    this.startSession(
      this.backendType(),
      this.selectedDuration()
    );
  }

  startSession(type: SessionPomodoroMeCreateDto['type'], duree: number) {
    this.sessionsApi.create({
      type,
      status: 'en_cours',
      tacheId: this.selectedTaskId(),
      duree,
      heureDebut: new Date().toISOString()
    }).subscribe(session => this.timer.start(session));
  }

  complete() {
    this.timer.complete();
    this.resetSelection();
  }

  stop() {
    this.timer.stop();
    this.resetSelection();
  }

  loadTasks() {
    this.tasksApi.getAll().subscribe(tasks =>
      this.tasks.set(tasks.filter(t => !t.validation))
    );
  }

  resetSelection() {
    this.selectedTaskId.set(null);
  }

}
