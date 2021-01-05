import {catchError, map} from 'rxjs/operators';
import {Observable, throwError, of} from 'rxjs';
import {Constant} from './../constant/constant';
import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHandler, HttpRequest} from '@angular/common/http';
import {Router} from '@angular/router';
import {Employee} from '../model/employee';
import {EmployeeService} from '../services/employee.service';
import {AuthUserResponse, GenericResponse, LoginRequest} from '../model/model';
import {GlobalUtil} from '../../../GlobalUtil';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  constructor(private http: HttpClient, protected employeeService: EmployeeService, private router: Router) {
  }

  errorHandler(error: HttpErrorResponse): Observable<any> {
    console.log('User api error ', error);
    return throwError(error);
  }

  login(loginRequest: LoginRequest): Observable<string[]> {
    this.clearLocalStorage();
    return this.http.post<GenericResponse<AuthUserResponse>>(Constant.API_ENDPOINT + '/auth/login', loginRequest).pipe(
      map(res => {
        const authUserResponse = res.response;
        localStorage.setItem('token', authUserResponse.token);
        localStorage.setItem('user', JSON.stringify(authUserResponse.user));
        return res;
      }),
      catchError(this.errorHandler)
    );
  }

  getCurrentUser(): Employee {
    const storageUserInfo = localStorage.getItem('user');
    const user: Employee = JSON.parse(storageUserInfo);
    return GlobalUtil.isPresent(user) ? user : null;
  }

  getToken(): string {
    const token = localStorage.getItem('token');
    return GlobalUtil.isNotEmpty(token) ? token : null;
  }

  isAuthenticatedUser(): boolean {
    const currentUser: Employee = this.getCurrentUser();
    const token = this.getToken();
    return GlobalUtil.isPresent(currentUser) && GlobalUtil.isNotEmpty(token);
  }

  isAdmin(): boolean {
    const currentUser: Employee = this.getCurrentUser();
    const isAdmin: boolean = GlobalUtil.isPresent(currentUser) && currentUser.role === 'ROLE_ADMIN';
    return isAdmin;
  }

  // private async getCurrentUser(): Promise<User> {
  //   const storageUserInfo = localStorage.getItem('user');
  //   let user: User = null;
  //   if (storageUserInfo == null || storageUserInfo === undefined){
  //     const authenticatedUser: User = await from(this.userService.getAuthenticatedUser()).toPromise();
  //     user = authenticatedUser;
  //   } else {
  //     user = JSON.parse(storageUserInfo);
  //   }
  //   return user;
  // }

  logout(): void {
    this.clearLocalStorage();
    this.router.navigate(['/']);
  }

  private clearLocalStorage(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  }

  isLoggedIn() {
    // console.log("is Logged In ", !!localStorage.getItem("token"));
    return !!localStorage.getItem('token');
  }
}
