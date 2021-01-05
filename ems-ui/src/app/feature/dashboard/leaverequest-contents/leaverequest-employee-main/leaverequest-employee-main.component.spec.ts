import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LeaverequestEmployeeMainComponent } from './leaverequest-employee-main.component';

describe('LeaverequestEmployeeMainComponent', () => {
  let component: LeaverequestEmployeeMainComponent;
  let fixture: ComponentFixture<LeaverequestEmployeeMainComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LeaverequestEmployeeMainComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LeaverequestEmployeeMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
