import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { Session } from '../interfaces/session.interface';

import { SessionApiService } from './session-api.service';

describe('SessionsService', () => {
  let service: SessionApiService;
  const mockSession: Session = {
    id: 1,
    name: 'Test Session',
    description: 'This is a test session',
    date: new Date(),
    teacher_id: 1,
    users: [1, 2],
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
    });
    service = TestBed.inject(SessionApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should have a getSessions method', () => {
    expect(service.all).toBeDefined();
    expect(service.unParticipate).toBeDefined();
    expect(service.participate).toBeDefined();
    expect(service.detail).toBeDefined();
    expect(service.delete).toBeDefined();
    expect(service.create).toBeDefined();
    expect(service.update).toBeDefined();
  });

  it('should have a pathService defined', () => {
    expect(service['pathService']).toBeDefined();
    expect(service['pathService']).toBe('api/session');
  });

  it('should return an Observable from all method', () => {
    const observable = service.all();
    expect(observable).toBeDefined();
    expect(observable.subscribe).toBeDefined();
  });

  it('should return an Observable from detail method', () => {
    const observable = service.detail('1');
    expect(observable).toBeDefined();
    expect(observable.subscribe).toBeDefined();
  });

  it('should return an Observable from delete method', () => {
    const observable = service.delete('1');
    expect(observable).toBeDefined();
    expect(observable.subscribe).toBeDefined();
  });

  it('should return an Observable from create method', () => {
    const session: Session = mockSession; // Mock session object
    const observable = service.create(session);
    expect(observable).toBeDefined();
    expect(observable.subscribe).toBeDefined();
  });

  it('should return an Observable from update method', () => {
    const session: Session = mockSession;
    const observable = service.update('1', session);
    expect(observable).toBeDefined();
    expect(observable.subscribe).toBeDefined();
  });

  it('should return an Observable from participate method', () => {
    const observable = service.participate('1', 'user1');
    expect(observable).toBeDefined();
    expect(observable.subscribe).toBeDefined();
  });

  it('should return an Observable from unParticipate method', () => {
    const observable = service.unParticipate('1', 'user1');
    expect(observable).toBeDefined();
    expect(observable.subscribe).toBeDefined();
  });
});
