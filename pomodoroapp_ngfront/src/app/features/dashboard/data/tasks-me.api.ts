import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

export interface TacheMeDto {
    idTache?: number; 
    titre: string;
    description?: string | null;
    validation?: boolean;
    position?: number;
}

@Injectable({ providedIn: 'root' })
export class TasksMeApi {
    private http = inject(HttpClient);

    getAll() {
        return this.http.get<TacheMeDto[]>(`${environment.apiUrl}/api/taches/me`);
    }
}
