import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerificationOtp } from './verification-otp';

describe('VerificationOtp', () => {
  let component: VerificationOtp;
  let fixture: ComponentFixture<VerificationOtp>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VerificationOtp],
    }).compileComponents();

    fixture = TestBed.createComponent(VerificationOtp);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
