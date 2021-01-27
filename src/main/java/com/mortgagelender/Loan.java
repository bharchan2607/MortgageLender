package com.mortgagelender;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Loan {
    private Applicant applicant;
    private String approvedLoanStatus;
    private double loanAmount;
    private LocalDate approvedDate;

    public Loan(Applicant applicant) {
        this.applicant=applicant;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public String getApprovedLoanStatus() {
        return approvedLoanStatus;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public LocalDate getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedLoanStatus(String approvedLoanStatus) {
        this.approvedLoanStatus = approvedLoanStatus;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setApprovedDate(LocalDate approvedDate) {
        this.approvedDate = approvedDate;
    }
}