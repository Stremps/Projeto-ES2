import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class Auth {
  getCurrentUser() {
    const userStr = localStorage.getItem('currentUser');
    return userStr ? JSON.parse(userStr) : null;
  }

  logout() {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('token');
  }
}

export { Auth as AuthService };
