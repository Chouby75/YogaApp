import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;
  const mockSessionInformation = {
    token: 'string',
    type: 'string',
    id: 1,
    username: 'test',
    firstName: 'test',
    lastName: 'test',
    admin: false,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should have isLogged false when logged out', () => {
    const logOutMethod = service.logOut;
    expect(service.isLogged).toBe(false);
  });

  it('should have isLogged true when logged in', () => {
    const mockUser: SessionInformation = mockSessionInformation;
    service.logIn(mockUser);
    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(mockUser);
  });
});
