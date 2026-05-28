import { Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ParamsMeApi, ParamsMeDto } from '../data/params-me.api';

@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
  <div class="settings">

    <h1>Paramètres</h1>

    <section>
      <h2>Pomodoro</h2>

      <label>
        Travail (minutes)
        <input type="number" min="1"
          [value]="form()?.dureeTravail"
          (input)="set('dureeTravail', +$any($event.target).value)" />
      </label>

      <label>
        Pause courte
        <input type="number" min="1"
          [value]="form()?.dureePauseCourte"
          (input)="set('dureePauseCourte', +$any($event.target).value)" />
      </label>

      <label>
        Pause longue
        <input type="number" min="1"
          [value]="form()?.dureePauseLongue"
          (input)="set('dureePauseLongue', +$any($event.target).value)" />
      </label>
    </section>

    <section>
      <h2>Objectif</h2>

      <label>
        Sessions par jour
        <input type="number" min="1"
          [value]="form()?.objectifSessions"
          (input)="set('objectifSessions', +$any($event.target).value)" />
      </label>
    </section>

    <section>
      <h2>Interface</h2>

      <label>
        Thème
        <select
          [value]="form()?.theme"
          (change)="set('theme', $any($event.target).value)">
          <option value="light">Clair</option>
          <option value="dark">Sombre</option>
          <option value="system">Système</option>
        </select>
      </label>

      <label class="row">
        <input type="checkbox"
          [checked]="form()?.sonActif"
          (change)="set('sonActif', $any($event.target).checked)" />
        Sons & notifications
      </label>
    </section>

    <button class="save"
      [disabled]="!canSave()"
      (click)="save()">
      Enregistrer
    </button>

  </div>
  `,
  styles: [`

    h2 {
      margin:0;
    }
    .settings {
      max-width: 640px;
      margin: 0 auto;
      display: flex;
      flex-direction: column;
      gap: 0.5rem;
    }

    section {
      background: #fff;
      border-radius: 16px;
      padding: 0.5rem;
      box-shadow: 0 8px 24px rgba(0,0,0,.06);
      display: grid;
      gap: 0.5rem;
    }

    label {
      display: flex;
      flex-direction: column;
      gap: .25rem;
    }

    input, select {
      padding: .5rem;
      border-radius: 10px;
      border: 1px solid #d0d5dd;
    }

    .row {
      flex-direction: row;
      align-items: center;
      gap: .5rem;
    }

    .save {
      align-self: flex-end;
      padding: .7rem 1.4rem;
      border-radius: 999px;
      border: none;
      background: #12b76a;
      color: white;
      font-weight: 600;
      cursor: pointer;
    }

    .save:disabled {
      opacity: .5;
      cursor: not-allowed;
    }
  `]
})
export class ParamsPage {

  private api = inject(ParamsMeApi);

  original = signal<ParamsMeDto | null>(null);
  form = signal<ParamsMeDto | null>(null);

  constructor() {
    this.api.get().subscribe(p => {
      this.original.set(p);
      this.form.set({ ...p });
    });
  }

  set<K extends keyof ParamsMeDto>(key: K, value: ParamsMeDto[K]) {
    this.form.update(f => f ? { ...f, [key]: value } : f);
  }

  canSave = computed(() => {
    const o = this.original();
    const f = this.form();
    if (!o || !f) return false;

    return JSON.stringify(o) !== JSON.stringify(f)
      && this.isValid();
  });

  isValid(): boolean {
    const f = this.form();
    if (!f) return false;

    return (
      f.dureeTravail > 0 &&
      f.dureePauseCourte > 0 &&
      f.dureePauseLongue > 0 &&
      f.objectifSessions > 0
    );
  }

  save() {
    const f = this.form();
    if (!f) return;

    this.api.patch(f).subscribe(p => {
      this.original.set(p);
      this.form.set({ ...p });
    });
  }
}
