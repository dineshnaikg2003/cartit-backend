package com.cartit.entity;

import java.time.LocalDateTime;

import com.cartit.entity.base.BaseEntity;
import com.cartit.enums.OtpPurpose;

import jakarta.persistence.*;

@Entity
@Table(name = "otp_verifications")
public class OtpVerification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String phone;

    @Column(nullable = false, length = 6)
    private String otp;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private Integer attempts = 0;

    @Column(nullable = false)
    private Boolean verified = false;
    
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private OtpPurpose purpose;
//
//    public OtpPurpose getPurpose() {
//		return purpose;
//	}
//
//	public void setPurpose(OtpPurpose purpose) {
//		this.purpose = purpose;
//	}

	public void setId(Long id) {
		this.id = id;
	}

	public OtpVerification() {
    }

    public Long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}