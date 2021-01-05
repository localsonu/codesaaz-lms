import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '../dashboard/auth/auth.service';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {FormConstants} from '../../form-constants';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  login_user_msg: string;
  has_error = false;
  hide = true;
  loginForm: FormGroup;
  isSubmitted = false;
  validationMessage = FormConstants.loginForm.validations;
  loading = false;
  hasError = false;
  responseMessage: string;

  constructor(private formBuilder: FormBuilder,
              private authService: AuthService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.initializeForm();
  }

  initializeForm(): void {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', Validators.required]
    });
  }

  get formControls(): { [p: string]: AbstractControl } {
    return this.loginForm.controls;
  }

  login(): void {
    this.isSubmitted = true;
    if (this.loginForm.invalid) {
      return;
    }
    const formValue = this.loginForm.value;
    const loginForm = {
      username: formValue.username,
      password: formValue.password
    };
    this.loading = true;
    this.authService.login(loginForm)
      .pipe()
      .subscribe(
        res => {
          this.loading = false;
          this.hasError = false;
          this.responseMessage = 'Login Successful';
          this.isSubmitted = false;
          this.loginForm.reset();
          console.log('Routing to dashboard');
          this.router.navigate(['/home']);
        },
        err => {
          this.loading = false;
          this.hasError = true;
          this.responseMessage = 'Invalid Username Password !!!';
        }
      );
  }
}
