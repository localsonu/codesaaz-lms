export interface PageRequest {
  page: number;
  size: number;
  sort: string;
  direction: string;
}

export interface GenericResponse<T> {
  messageCode: string;
  response: T;
  totalElements: number;
  httpStatus: string;
}

export interface GenericFilterRequest<T> {
  dataFilter: T;
}

export class LoginRequest {
  username: string;
  password: string;
}

export class AuthUserResponse {
  user?: User;
  token: string;
}

export class PasswordUpdate {
  id: string;
  password: string;
  confirmPassword: string;
}

export interface User {
  id: string;
  userName: string;
  fullName: string;
  role: string;
  status: string;
}

export interface Task {
  id: string;
  title: string;
  description: string;
  reportTo?: User;
  assignedTo?: User;
  startDate?: Date;
  endDate?: Date;
  taskResources: ResourceInfo[];
  taskType: TASK_TYPE;
  submissionStatus: string;
  status: string;
}

export interface ResourceInfo {
  id: number;
  resourceName: string;
  resourceSize: number;
  resourceType: number;
  resourceOwner?: User;
  createdAt?: Date;
  status: string;
}

export enum TASK_TYPE {
  ALL= 'ALL', AUDIO= 'AUDIO'
}
