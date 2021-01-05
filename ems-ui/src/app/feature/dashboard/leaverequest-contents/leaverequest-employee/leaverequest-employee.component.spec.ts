import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LeaverequestEmployeeComponent } from './leaverequest-employee.component';

describe('LeaverequestEmployeeComponent', () => {
  let component: LeaverequestEmployeeComponent;
  let fixture: ComponentFixture<LeaverequestEmployeeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LeaverequestEmployeeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LeaverequestEmployeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
