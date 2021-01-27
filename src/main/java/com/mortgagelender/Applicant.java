package com.mortgagelender;

public class Applicant {
    private int dti;
    private int creditScore;
    private double savings;
    private double requestedAmount;
    private String qualification;
    private double loanAmount;
    private String status;
    private String approvedLoanStatus;
    public Applicant(int dti, int creditScore, double savings, double requestedAmount) {
        this.dti = dti;
        this.creditScore = creditScore;
        this.savings = savings;
        this.requestedAmount = requestedAmount;
    }

    public String getQualification() {
        return qualification;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public String getStatus() {
        return status;
    }

    public int getDti() {
        return dti;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public double getSavings() {
        return savings;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApprovedLoanStatus() {
        return this.approvedLoanStatus;
    }

    public void setApprovedLoanStatus(String approvedStatus) {
        this.approvedLoanStatus = approvedStatus;
    }
}
