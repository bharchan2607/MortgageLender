package com.mortgagelender;

public class Applicant {
    private int dti;
    private int creditScore;
    private double savings;
    private double requestedAmount;
    private String qualification;

    private String status;

    public Applicant(int dti, int creditScore, double savings, double requestedAmount) {
        this.dti = dti;
        this.creditScore = creditScore;
        this.savings = savings;
        this.requestedAmount = requestedAmount;
    }

    public String getQualification() {
        return qualification;
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



    public void setStatus(String status) {
        this.status = status;
    }


}
