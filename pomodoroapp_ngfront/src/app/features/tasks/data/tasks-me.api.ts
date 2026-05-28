import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

export interface TacheMeDto {
    idTache: number;
    titre: string;
    description?: string | null;
    validation: boolean;
    position: number;
}

export interface TacheCreateDto {
    titre: string;
    description?: string;
}

export interface TacheUpdateDto {
    titre?: string;
    description?: string;
    validation?: boolean;
}


@Injectable({ providedIn: 'root' })
export class TasksMeApi {
    private http = inject(HttpClient);
    private baseUrl = `${environment.apiUrl}/api/taches/me`;

    getAll() {
        return this.http.get<TacheMeDto[]>(this.baseUrl);
    }

    create(dto: TacheCreateDto) {
        return this.http.post<TacheMeDto>(this.baseUrl, dto);
    }

    update(id: number, dto: TacheUpdateDto) {
        return this.http.patch<TacheMeDto>(`${this.baseUrl}/${id}`, dto);
    }

    delete(id: number) {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }

    reorder(id: number, newPosition: number) {
        return this.http.patch<TacheMeDto>(
            `${this.baseUrl}/${id}/reorder`,
            null,
            { params: { newPosition } }
        );
    }
}
