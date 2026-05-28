// features/settings/data/params-me.api.ts
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

export interface ParamsMeDto {
    theme: 'light' | 'dark' | 'system';
    sonActif: boolean;
    dureeTravail: number;
    dureePauseCourte: number;
    dureePauseLongue: number;
    objectifSessions: number;
}

@Injectable({ providedIn: 'root' })
export class ParamsMeApi {
    private http = inject(HttpClient);
    private base = `${environment.apiUrl}/api/params/me`;

    get() {
        return this.http.get<ParamsMeDto>(this.base);
    }

    patch(dto: Partial<ParamsMeDto>) {
        return this.http.patch<ParamsMeDto>(this.base, dto);
    }
}
