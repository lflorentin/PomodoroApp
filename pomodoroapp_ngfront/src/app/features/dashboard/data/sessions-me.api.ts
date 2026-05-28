import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { Observable, catchError, of } from 'rxjs';

export type SessionStatus = 'en_cours' | 'completee' | 'annulee'; 
export type SessionType = 'travail' | 'pause'; 

export interface SessionPomodoroMeDto {
    idSession: number;
    type: 'travail' | 'pause';
    status: 'en_cours' | 'completee' | 'annulee';
    duree: number;
    dureeReelle?: number;
    heureDebut: string;
    heureFin?: string;
    tacheId?: number;
}

export interface SessionPomodoroMeCreateDto {
    type: 'travail' | 'pause';
    status: 'en_cours';
    duree: number;
    dureeReelle?: number;
    heureDebut: string;
    tacheId?: number | null;
}

export interface SessionPomodoroMeUpdateDto {
    status?: 'en_cours' | 'completee' | 'annulee';
    duree?: number;
    dureeReelle?: number;
    heureDebut?: string;
    heureFin?: string;
    type?: 'travail' | 'pause';
    tacheId?: number | null;
}

@Injectable({ providedIn: 'root' })
export class SessionsMeApi {

    private http = inject(HttpClient);
    private baseUrl = `${environment.apiUrl}/api/sessions/me`;

    getAll(): Observable<SessionPomodoroMeDto[]> {
        return this.http.get<SessionPomodoroMeDto[]>(`${this.baseUrl}`);
    }

    getToday(): Observable<SessionPomodoroMeDto[]> {
        return this.http.get<SessionPomodoroMeDto[]>(`${this.baseUrl}/today`);
    }

    getCurrent(): Observable<SessionPomodoroMeDto | null> {
        return this.http.get<SessionPomodoroMeDto | null>(`${this.baseUrl}/current`);
    }

    create(dto: SessionPomodoroMeCreateDto): Observable<SessionPomodoroMeDto> {
        return this.http.post<SessionPomodoroMeDto>(`${this.baseUrl}`, dto);
    }

    patch(
        id: number,
        dto: SessionPomodoroMeUpdateDto
    ): Observable<SessionPomodoroMeDto> {
        return this.http.patch<SessionPomodoroMeDto>(`${this.baseUrl}/${id}`, dto);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }

}
