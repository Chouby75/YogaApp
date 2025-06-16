import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';

import { ListComponent } from './list.component';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
  };

  const mockSessions = {
    all: jest.fn().mockReturnValue(
      of([
        {
          id: 1,
          name: 'string',
          description: 'string',
          date: new Date(),
          teacher_id: 1,
          users: [1, 2],
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        {
          id: 2,
          name: 'string',
          description: 'string',
          date: new Date(),
          teacher_id: 2,
          users: [1, 2],
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        {
          id: 2,
          name: 'string',
          description: 'string',
          date: new Date(),
          teacher_id: 2,
          users: [1, 2],
          createdAt: new Date(),
          updatedAt: new Date(),
        },
      ])
    ),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessions },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch all sessions on init', () => {
    expect(mockSessions.all).toHaveBeenCalled();
    const compileComponent = fixture.nativeElement as HTMLElement;
    const sessionCards = compileComponent.querySelectorAll('mat-card');
    expect(sessionCards.length).toBe(4);
  });
});
