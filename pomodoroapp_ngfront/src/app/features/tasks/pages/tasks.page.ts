import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TasksMeApi, TacheMeDto } from '../data/tasks-me.api';
import { CdkDragDrop, DragDropModule, moveItemInArray } from '@angular/cdk/drag-drop';

@Component({
    standalone: true,
    imports: [CommonModule, FormsModule, DragDropModule],
    template: `
  <div class="tasks">
    <header>
        <h1>Tâches</h1>

        <form (ngSubmit)="create()" class="task-form">
        <input
            [(ngModel)]="newTitle"
            name="title"
            placeholder="Titre de la tâche"
            required
        />

        <textarea
            [(ngModel)]="newDescription"
            name="description"
            placeholder="Description (optionnelle)"
            rows="2">
        </textarea>

        <button type="submit">Ajouter</button>
        </form>
    </header>

    <ul cdkDropList (cdkDropListDropped)="drop($event)">
        <li *ngFor="let t of tasks(); index as i" cdkDrag>
        <input
            type="checkbox"
            [checked]="t.validation"
            (change)="toggle(t)"
        />

        <div class="task-content">
            <span class="title" [class.done]="t.validation">
            {{ t.titre }}
            </span>

            <small *ngIf="t.description" class="description">
            {{ t.description }}
            </small>
        </div>

        <button class="danger" (click)="remove(t)">✕</button>
        </li>
    </ul>
    </div>

  `,
    styles: [`
    .tasks {
        max-width: 640px;
    }

    header {
        display: flex;
        flex-direction: column;
        gap: 1rem;
    }

    .task-form {
        display: flex;
        flex-direction: column;
        gap: .5rem;
    }

    input, textarea {
        padding: .5rem;
        font-size: .95rem;
        border-radius: 6px;
        border: 1px solid #d1d5db;
    }

    button {
        align-self: flex-end;
    }

    ul {
        list-style: none;
        padding: 0;
        margin-top: 1rem;
    }

    li {
        display: flex;
        align-items: flex-start;
        gap: .75rem;
        padding: .75rem;
        background: #fff;
        border: 1px solid #e5e7eb;
        border-radius: 10px;
        margin-bottom: .5rem;
    }

    .task-content {
        display: flex;
        flex-direction: column;
        flex: 1;
    }

    .title {
        font-weight: 500;
    }

    .description {
        color: #6b7280;
        font-size: .85rem;
    }

    .done {
        text-decoration: line-through;
        opacity: .6;
    }

    .danger {
        color: #ef4444;
        background: none;
        border: none;
    cursor: pointer;
    }

  `]
})
export class TasksPage {
    private api = inject(TasksMeApi);

    tasks = signal<TacheMeDto[]>([]);
    newTitle = '';
    newDescription = '';

    constructor() {
        this.load();
    }

    load() {
        this.api.getAll().subscribe(t => this.tasks.set(t));
    }

    create() {
        if (!this.newTitle.trim()) return;

        this.api.create({
            titre: this.newTitle,
            description: this.newDescription || undefined
        }).subscribe(t => {
            this.tasks.update(list => [...list, t]);
            this.newTitle = '';
            this.newDescription = '';
        });
    }


    toggle(t: TacheMeDto) {
        this.api.update(t.idTache, { validation: !t.validation })
            .subscribe(updated => {
                this.tasks.update(list =>
                    list.map(x => x.idTache === updated.idTache ? updated : x)
                );
            });
    }

    remove(t: TacheMeDto) {
        this.api.delete(t.idTache).subscribe(() => {
            this.tasks.update(list => list.filter(x => x.idTache !== t.idTache));
        });
    }

    drop(event: CdkDragDrop<TacheMeDto[]>) {
        if (event.previousIndex === event.currentIndex) return;

        const list = [...this.tasks()];
        moveItemInArray(list, event.previousIndex, event.currentIndex);
        this.tasks.set(list);

        const moved = list[event.currentIndex];
        this.api.reorder(moved.idTache, event.currentIndex + 1).subscribe();
    }
}
