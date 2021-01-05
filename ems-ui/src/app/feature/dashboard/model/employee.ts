export class Employee {

  employeeId: number;
  fullName: string;
  username: string;
  password: string;
  role: string;
  status: number;
  leaveRemaining: number;
  totalLeave: number;
  employeeSupervisor: Employee;

  constructor(id) {
    this.employeeId = id;
  }
}
