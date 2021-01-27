package com.mortgagelender;

import com.mortgagelender.exception.NotQualifiedApplicantException;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MortgageLender {
    private double currentAmount;
    private double pendingFund;
    private final int ALLOWED_DTI = 36;
    private final int ALLOWED_CREDIT_SCORE = 620;
    private final double ALLOWED_SAVINGS = 0.25;
   private List<Loan> loans;

    public MortgageLender(double amount) {
        this.currentAmount = amount;
        this.loans=new ArrayList<>();
    }

    public double checkAvailableFunds() {
        return this.currentAmount;
    }

    public void deposit(double depositedAmount) {
        currentAmount += depositedAmount;
    }

    public void checkApplicationQualification(int loanNumber) {
       Loan loan=getLoan(loanNumber);
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

    public void approveLoan(int loanNumber) {
        Loan loan=getLoan(loanNumber);
        checkApplicationQualification(loanNumber);
        if(loan.getApplicant().getQualification().equals("qualified")){
            loan.setLoanAmount(loan.getApplicant().getRequestedAmount());
            loan.setApprovedLoanStatus("qualified");
        }else if(loan.getApplicant().getQualification().equals("partially qualified")){
            loan.setLoanAmount(loan.getApplicant().getSavings() * 4 );
            loan.setApprovedLoanStatus("qualified");
        }else{
            loan.setLoanAmount(0);
            loan.setApprovedLoanStatus("denied");
        }

    }


    public void sanctionLoan(int loanNumber) {
        Loan loan=getLoan(loanNumber);
        approveLoan(loanNumber);
        if(loan.getApprovedLoanStatus().equals("qualified")) {
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
        loans.add(loan);
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void loanAccepted(boolean accepted,int loanNumber) {
        Loan loan=getLoan(loanNumber);

        if(accepted){
            pendingFund-=loan.getLoanAmount();
            loan.setApprovedLoanStatus("accepted");

        }
        else{
            currentAmount+=loan.getLoanAmount();
            pendingFund-=loan.getLoanAmount();
            loan.setApprovedLoanStatus("rejected");

        }

    }

    public void checkExpiredLoan() {
        for(Loan loan: loans){
            LocalDate now= LocalDate.now();
            if(loan.getApprovedLoanStatus().equals("approved")) {
                Period diff = Period.between(loan.getApprovedDate(), now);
                if (diff.getDays() > 3) {
                    currentAmount += loan.getLoanAmount();
                    pendingFund -= loan.getLoanAmount();
                    loan.setApprovedLoanStatus("expired");
                }
            }
        }

    }

    public Loan getLoan(int loanNumber) {
        for(Loan loan : loans){
            if(loan.getLoanNumber()==loanNumber){
                return loan;
            }

        }
        return null;
    }

    public List<String> filterLoansByStatus(String status) {
        return loans.stream()
                .filter(loan -> loan.getApprovedLoanStatus().equals(status))
                .map(loan -> loan.toString())
                .collect(Collectors.toList());


    }
}
