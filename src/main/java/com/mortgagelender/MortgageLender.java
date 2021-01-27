package com.mortgagelender;

import com.mortgagelender.exception.NotQualifiedApplicantException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class MortgageLender {
    private double currentAmount;
    private double pendingFund;
    private final int ALLOWED_DTI = 36;
    private final int ALLOWED_CREDIT_SCORE = 620;
    private final double ALLOWED_SAVINGS = 0.25;
   private Loan loan;

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
        if(loan.getApplicant().getDti() < ALLOWED_DTI &&
                loan.getApplicant().getCreditScore() > ALLOWED_CREDIT_SCORE) {
            if (loan.getApplicant().getSavings() > ALLOWED_SAVINGS * loan.getApplicant().getRequestedAmount()){
                loan.getApplicant().setQualification("qualified");
            }else{
                loan.getApplicant().setQualification("partially qualified");
            }
        }else{
            loan.getApplicant().setQualification("not qualified");
        }
    }

    public void approveLoan() {
        checkApplicationQualification();
        if(loan.getApplicant().getQualification().equals("qualified")){
            loan.setLoanAmount(loan.getApplicant().getRequestedAmount());
            loan.getApplicant().setStatus("qualified");
        }else if(loan.getApplicant().getQualification().equals("partially qualified")){
            loan.setLoanAmount(loan.getApplicant().getSavings() * 4 );
            loan.getApplicant().setStatus("qualified");
        }else{
            loan.setLoanAmount(0);
            loan.getApplicant().setStatus("denied");
        }

    }


    public void sanctionLoan() {
        approveLoan();
        if(loan.getApplicant().getStatus().equals("qualified")) {
            if (loan.getLoanAmount() <= currentAmount) {
                pendingFund += loan.getLoanAmount() ;
                currentAmount -= loan.getLoanAmount() ;
                loan.setApprovedLoanStatus("approved");
                loan.setApprovedDate(LocalDate.now());
            } else {
                loan.setApprovedLoanStatus("on hold");
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

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public Loan getLoan() {
        return loan;
    }

    public String loanAccepted(boolean accepted) {
        if(accepted){
            pendingFund-=loan.getLoanAmount();
            return "accepted";
        }
        else{
            currentAmount+=loan.getLoanAmount();
            pendingFund-=loan.getLoanAmount();
            return "rejected";
        }

    }

    public void checkExpiredLoan() {
        LocalDate now= LocalDate.now();
        Period diff = Period.between(loan.getApprovedDate(), now);
        if(diff.getDays()>3){
            currentAmount+=loan.getLoanAmount();
            pendingFund-=loan.getLoanAmount();
            loan.setApprovedLoanStatus("expired");
        }
    }
}
