import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQueueForDoctor, NewQueueForDoctor } from '../queue-for-doctor.model';

export type PartialUpdateQueueForDoctor = Partial<IQueueForDoctor> & Pick<IQueueForDoctor, 'id'>;

export type EntityResponseType = HttpResponse<IQueueForDoctor>;
export type EntityArrayResponseType = HttpResponse<IQueueForDoctor[]>;

@Injectable({ providedIn: 'root' })
export class QueueForDoctorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/queue-for-doctors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(queueForDoctor: NewQueueForDoctor): Observable<EntityResponseType> {
    return this.http.post<IQueueForDoctor>(this.resourceUrl, queueForDoctor, { observe: 'response' });
  }

  update(queueForDoctor: IQueueForDoctor): Observable<EntityResponseType> {
    return this.http.put<IQueueForDoctor>(`${this.resourceUrl}/${this.getQueueForDoctorIdentifier(queueForDoctor)}`, queueForDoctor, {
      observe: 'response',
    });
  }

  partialUpdate(queueForDoctor: PartialUpdateQueueForDoctor): Observable<EntityResponseType> {
    return this.http.patch<IQueueForDoctor>(`${this.resourceUrl}/${this.getQueueForDoctorIdentifier(queueForDoctor)}`, queueForDoctor, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQueueForDoctor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQueueForDoctor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getQueueForDoctorIdentifier(queueForDoctor: Pick<IQueueForDoctor, 'id'>): number {
    return queueForDoctor.id;
  }

  compareQueueForDoctor(o1: Pick<IQueueForDoctor, 'id'> | null, o2: Pick<IQueueForDoctor, 'id'> | null): boolean {
    return o1 && o2 ? this.getQueueForDoctorIdentifier(o1) === this.getQueueForDoctorIdentifier(o2) : o1 === o2;
  }

  addQueueForDoctorToCollectionIfMissing<Type extends Pick<IQueueForDoctor, 'id'>>(
    queueForDoctorCollection: Type[],
    ...queueForDoctorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const queueForDoctors: Type[] = queueForDoctorsToCheck.filter(isPresent);
    if (queueForDoctors.length > 0) {
      const queueForDoctorCollectionIdentifiers = queueForDoctorCollection.map(
        queueForDoctorItem => this.getQueueForDoctorIdentifier(queueForDoctorItem)!
      );
      const queueForDoctorsToAdd = queueForDoctors.filter(queueForDoctorItem => {
        const queueForDoctorIdentifier = this.getQueueForDoctorIdentifier(queueForDoctorItem);
        if (queueForDoctorCollectionIdentifiers.includes(queueForDoctorIdentifier)) {
          return false;
        }
        queueForDoctorCollectionIdentifiers.push(queueForDoctorIdentifier);
        return true;
      });
      return [...queueForDoctorsToAdd, ...queueForDoctorCollection];
    }
    return queueForDoctorCollection;
  }
}
