package com.mortgagelender;

import com.mortgagelender.exception.NotQualifiedApplicantException;

public class MortgageLender {
    private double currentAmount;
    private double pendingFund;
    private final int ALLOWED_DTI = 36;
    private final int ALLOWED_CREDIT_SCORE = 620;
    private final double ALLOWED_SAVINGS = 0.25;
    private Applicant applicant;

    public MortgageLender(double amount) {
        this.currentAmount = amount;
    }

    public double checkAvailableFunds() {
        return this.currentAmount;
    }

    public void deposit(double depositedAmount) {
        currentAmount += depositedAmount;
    }

    public void checkApplicationQualification() {
        if(applicant.getDti() < ALLOWED_DTI &&
        applicant.getCreditScore() > ALLOWED_CREDIT_SCORE) {
            if (applicant.getSavings() > ALLOWED_SAVINGS * applicant.getRequestedAmount()){
                applicant.setQualification("qualified");
            }else{
                applicant.setQualification("partially qualified");
            }
        }else{
            applicant.setQualification("not qualified");
        }
    }

    public void approveLoan() {
        checkApplicationQualification();
        if(applicant.getQualification().equals("qualified")){
            applicant.setLoanAmount(applicant.getRequestedAmount());
            applicant.setStatus("qualified");
        }else if(applicant.getQualification().equals("partially qualified")){
            applicant.setLoanAmount(applicant.getSavings() * 4 );
            applicant.setStatus("qualified");
        }else{
            applicant.setLoanAmount(0);
            applicant.setStatus("denied");
        }

    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public void sanctionLoan() {
        approveLoan();
        if(applicant.getStatus().equals("qualified")) {
            if (applicant.getLoanAmount() <= currentAmount) {
                pendingFund += applicant.getLoanAmount();
                currentAmount -= applicant.getLoanAmount();
                applicant.setApprovedLoanStatus("approved");
            } else {
                applicant.setApprovedLoanStatus("on hold");
            }
        }else{
            throw new NotQualifiedApplicantException("You can't proceed with the loan application");
        }
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public double getPendingFund() {
        return pendingFund;
    }

    public String loanAccepted(boolean accepted) {
        if(accepted){
            pendingFund-=applicant.getLoanAmount();
            return "accepted";
        }
        else{
            currentAmount+=applicant.getLoanAmount();
            pendingFund-=applicant.getLoanAmount();
            return "rejected";
        }

    }
}
