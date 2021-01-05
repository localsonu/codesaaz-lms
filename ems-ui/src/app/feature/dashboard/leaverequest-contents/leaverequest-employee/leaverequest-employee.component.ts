import {Component, OnInit} from '@angular/core';
import {concat, Observable, of, Subject} from 'rxjs';
import {EmployeeLeaveService} from '../../services/employeeLeave.service';
import {EmployeeService} from '../../services/employee.service';
import {catchError, debounceTime, distinctUntilChanged, switchMap, tap} from 'rxjs/operators';
import {AuthService} from '../../auth/auth.service';

@Component({
  selector: 'app-leaverequest-employee',
  templateUrl: './leaverequest-employee.component.html',
  styleUrls: ['./leaverequest-employee.component.css']
})
export class LeaverequestEmployeeComponent implements OnInit {
  leaveRequests;
  errorMsg;
  id;
  loading = true;
  currentPage = 1;
  totalElements;
  numberOfElements;
  size = 10;
  sortKey = 'fromDate';
  reverse = false;
  allEmployees: Observable<any>;
  employeeinput$ = new Subject<string>();
  isSelectLoading = false;
  private selected_user_msg: string;

  constructor(private _employeeLeaveService: EmployeeLeaveService, private _employeeService: EmployeeService, public authService: AuthService) {
  }

  ngOnInit() {
    this.getAllEmployeeLeavesByEmployeeId();
    this.loadEmployee();
    this.getUserInfo();
  }

  getPage(page: number) {
    this.loading = true;
    this.currentPage = page;
    this.getAllEmployeeLeavesByEmployeeId();
  }

  sort(key: string) {
    this.loading = true;
    this.sortKey = key + ','.concat(this.reverse ? 'DESC' : 'ASC');
    this.reverse = !this.reverse;
    this.getAllEmployeeLeavesByEmployeeId();
  }

  getAllEmployeeLeavesByEmployeeId() {
    this._employeeLeaveService.getAllEmployeeLeavesByEmpId(this.currentPage - 1, this.size, this.sortKey, this.id = localStorage.getItem('empid'))
      .subscribe(
        data => {
          this.leaveRequests = data.content;
          this.totalElements = data.totalElements;
          this.size = data.size;
          this.numberOfElements = data.numberOfElements;
          this.loading = false;
          // console.log('employees data: ', data);
        },
        error => this.errorMsg = error);
  }

  private loadEmployee() {
    this.allEmployees = concat(
      of([]), // default items
      this.employeeinput$.pipe(
        debounceTime(200),
        distinctUntilChanged(),
        tap(() => this.isSelectLoading = true),
        switchMap(term => this._employeeService.getEmployeeByFullName(term).pipe(
          catchError(() => of([])), // empty list on error
          tap(() => this.isSelectLoading = false)
        ))
      )
    );
  }
  getUserInfo() {
    this._employeeService.getCurrentEmployee()
      .subscribe(
        res => {
          let empId: number  = res.employeeId;
          console.log(res);
          localStorage.setItem('empid', String(empId));
        },
        error => {
          // this.login_user_msg = "Oops ! Can't load Profile";
        });
  }
}
