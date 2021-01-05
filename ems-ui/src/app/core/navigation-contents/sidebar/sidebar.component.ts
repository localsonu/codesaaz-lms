import {Component, OnInit} from '@angular/core';
import {SidebarService} from '../service/sidebar.service';
import {AuthService} from '../../../feature/dashboard/auth/auth.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

  isOpen = false;

  constructor(private _sidebarService: SidebarService, private _authService: AuthService) {
  }

  ngOnInit() {
    this._sidebarService.change.subscribe(isOpen => {
      this.isOpen = isOpen;
    });
  }

}
