import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService } from '../../../../core/auth/auth.service';

@Component({
  standalone: true,
  selector: 'app-login-page',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
})
export class LoginPage {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);

  loading = false;
  error: string | null = null;

  form = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4)]],
  });

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.error = null;

    this.auth.login(this.form.getRawValue()).subscribe({
      next: () => {
        this.router.navigateByUrl('/dashboard');
      },
      error: () => {
        this.error = 'Email ou mot de passe incorrect';
        this.loading = false;
      },
      complete: () => {
        this.loading = false;
      },
    });
  }
}
