import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

export interface StatJourneeMeDto {
    date: string;        
    nbSessions: number;
    tempsTotal: number;  
    objectif: number; 
}

@Injectable({ providedIn: 'root' })
export class StatsMeApi {
    private http = inject(HttpClient);

    getToday() {
        return this.http.get<StatJourneeMeDto>(`${environment.apiUrl}/api/stat-journees/me/today`);
    }
}
