import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../auth/auth.service';

@Component({
  selector: 'app-events-main',
  templateUrl: './events-main.component.html',
  styleUrls: ['./events-main.component.css']
})
export class EventsMainComponent implements OnInit {

  constructor(private _authService: AuthService) {
  }

  ngOnInit() {
  }

}
