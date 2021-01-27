package com.mortgagelender;

import com.mortgagelender.exception.NotQualifiedApplicantException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MortgageLenderTest {
    List<Loan> loans= new ArrayList<>();

    MortgageLender lender;
    @BeforeEach
    public void setup(){
        lender = new MortgageLender(100000);
    }

    @Test
    public void checkAvailableFunds(){
        assertEquals(100000, lender.checkAvailableFunds());
    }


    @Test
    public void depositAmountToFunds(){
        lender.deposit(50000);
        assertEquals(150000,lender.checkAvailableFunds());
    }


    @Test
    public void loanApplicationQualification(){
        lender.deposit(200000);
        Applicant applicant = new Applicant(21, 700, 100000,250000);
        lender.setLoan(new Loan(applicant,4));
        lender.approveLoan(4);
        assertEquals("qualified", applicant.getQualification());
        assertEquals(250000,lender.getLoan(4).getLoanAmount());
        assertEquals("qualified",lender.getLoan(4).getApprovedLoanStatus());

        Applicant applicant1 = new Applicant(37, 700, 100000,250000);
        lender.setLoan(new Loan(applicant1,1));
        lender.approveLoan(1);
        assertEquals("not qualified", applicant1.getQualification());
        assertEquals(0, lender.getLoan(1).getLoanAmount());
        assertEquals("denied", lender.getLoan(1).getApprovedLoanStatus());

        Applicant applicant2 = new Applicant(30, 600, 100000,250000);
        lender.setLoan(new Loan(applicant2,2));
        lender.approveLoan(2);
        assertEquals("not qualified", applicant2.getQualification());
        assertEquals(0, lender.getLoan(2).getLoanAmount());
        assertEquals("denied",lender.getLoan(2).getApprovedLoanStatus());

        Applicant applicant3 = new Applicant(30, 700, 50000,250000);
        lender.setLoan(new Loan(applicant3,3));
        lender.approveLoan(3);
        assertEquals("partially qualified", applicant3.getQualification());
        assertEquals(200000, lender.getLoan(3).getLoanAmount());
        assertEquals("qualified", lender.getLoan(3).getApprovedLoanStatus());

    }

    @Test
    public void approveLoan(){
        Applicant applicant = new Applicant(21, 700, 100000,125000);
        lender.setLoan(new Loan(applicant,3));
        lender.sanctionLoan(3);
        assertEquals("on hold", lender.getLoan(3).getApprovedLoanStatus());

        lender.deposit(25000);
        Applicant applicant2 = new Applicant(21, 700, 100000,125000);
        lender.setLoan(new Loan(applicant2,2));
        lender.sanctionLoan(2);
        assertEquals("approved", lender.getLoan(2).getApprovedLoanStatus());

        lender.deposit(200000);
        Applicant applicant1 = new Applicant(21, 700, 100000,125000);
        lender.setLoan(new Loan(applicant1,1));
        lender.sanctionLoan(1);
        assertEquals("approved", lender.getLoan(1).getApprovedLoanStatus());

    }

    @Test
    public void displayWarningNotQualifiedLoan(){
        Applicant applicant = new Applicant(38, 700, 100000,125000);
        lender.setLoan(new Loan(applicant,1));
        NotQualifiedApplicantException exception = assertThrows(NotQualifiedApplicantException.class, ()->lender.sanctionLoan(1));
        assertEquals("You can't proceed with the loan application", exception.getMessage());
    }


    @Test
    public void moveLoanAmountToPendingFunds(){
        lender.deposit(200000);
        Applicant applicant1 = new Applicant(21, 700, 100000,125000);
        lender.setLoan(new Loan(applicant1,1));
        lender.sanctionLoan(1);
        assertEquals(125000, lender.getPendingFund());
        assertEquals(175000, lender.getCurrentAmount());
    }

    @Test
    public void loanAcceptanceStatus(){
        lender.deposit(200000);
        Applicant applicant1 = new Applicant(21, 700, 100000,125000);
        lender.setLoan(new Loan(applicant1,1));
        lender.sanctionLoan(1);
        lender.loanAccepted(true,1);
        assertEquals("accepted",lender.getLoan(1).getApprovedLoanStatus());
        assertEquals(0,lender.getPendingFund());

    }
    @Test
    public void loanRejectedStatus(){
        lender.deposit(200000);
        Applicant applicant1 = new Applicant(21, 700, 100000,125000);
        lender.setLoan(new Loan(applicant1,1));
        lender.sanctionLoan(1);
        lender.loanAccepted(false,1);
        assertEquals("rejected",lender.getLoan(1).getApprovedLoanStatus());
        assertEquals(0,lender.getPendingFund());
        assertEquals(300000,lender.checkAvailableFunds());

    }
    @Test
    public void checkUndecidedLoans(){
        lender.deposit(200000);
        Applicant applicant1 = new Applicant(21, 700, 100000,125000);
        lender.setLoan(new Loan(applicant1,1));
        lender.sanctionLoan(1);
        lender.getLoan(1).setApprovedDate( LocalDate.of(2021, 01, 23));
        lender.checkExpiredLoan();
       assertEquals("expired",lender.getLoan(1).getApprovedLoanStatus());
    }
    @Test
    public void filterLoansByStatus(){
        //on hold
        Applicant applicant4 = new Applicant(21, 700, 100000,125000);
        lender.setLoan(new Loan(applicant4,4));
        lender.sanctionLoan(4);
        assertEquals("on hold",lender.getLoan(4).getApprovedLoanStatus());

        //accepted
        lender.deposit(25000);
        Applicant applicant1 = new Applicant(21, 700, 100000,125000);
        lender.setLoan(new Loan(applicant1,1));
        lender.sanctionLoan(1);
        lender.loanAccepted(true,1);
        assertEquals("accepted",lender.getLoan(1).getApprovedLoanStatus());

        //rejected
        lender.deposit(200000);
        Applicant applicant2 = new Applicant(21, 700, 100000,125000);
        lender.setLoan(new Loan(applicant2,2));
        lender.sanctionLoan(2);
        lender.loanAccepted(false,2);
        assertEquals("rejected",lender.getLoan(2).getApprovedLoanStatus());

        //expired
        lender.deposit(200000);
        Applicant applicant3 = new Applicant(21, 700, 100000,125000);
        lender.setLoan(new Loan(applicant3,3));
        lender.sanctionLoan(3);
        lender.getLoan(3).setApprovedDate( LocalDate.of(2021, 01, 23));
        lender.checkExpiredLoan();
        assertEquals("expired",lender.getLoan(3).getApprovedLoanStatus());

         //denied
        Applicant applicant5 = new Applicant(30, 600, 100000,250000);
        lender.setLoan(new Loan(applicant5,5));
        lender.approveLoan(5);
        assertEquals("denied",lender.getLoan(5).getApprovedLoanStatus());

        //Qualified
        Applicant applicant6 = new Applicant(30, 700, 50000,250000);
        lender.setLoan(new Loan(applicant6,6));
        lender.approveLoan(6);
        assertEquals("qualified",lender.getLoan(6).getApprovedLoanStatus());

        //Approved
        lender.deposit(200000);
        Applicant applicant7 = new Applicant(21, 700, 100000,125000);
        lender.setLoan(new Loan(applicant7,7));
        lender.sanctionLoan(7);
        assertEquals("approved",lender.getLoan(7).getApprovedLoanStatus());

        lender.deposit(200000);
        Applicant applicant8 = new Applicant(30, 700, 100000,125000);
        lender.setLoan(new Loan(applicant8,8));
        lender.sanctionLoan(8);
        assertEquals("approved",lender.getLoan(8).getApprovedLoanStatus());

        List<String> approvedLoan = lender.filterLoansByStatus("approved");
        assertEquals("Loan[loanNumber=7, applicant=Applicant[dti=21, creditScore=700, savings=100000.0, requestedAmount=125000.0, qualification='qualified'], approvedLoanStatus='approved', loanAmount=125000.0, approvedDate=2021-01-27]", approvedLoan.get(0));
        assertEquals("Loan[loanNumber=8, applicant=Applicant[dti=30, creditScore=700, savings=100000.0, requestedAmount=125000.0, qualification='qualified'], approvedLoanStatus='approved', loanAmount=125000.0, approvedDate=2021-01-27]", approvedLoan.get(1));


        List<String> onHoldLoan = lender.filterLoansByStatus("on hold");
        assertEquals("Loan[loanNumber=4, applicant=Applicant[dti=21, creditScore=700, savings=100000.0, requestedAmount=125000.0, qualification='qualified'], approvedLoanStatus='on hold', loanAmount=125000.0, approvedDate=null]", onHoldLoan.get(0));

        List<String> acceptedLoan = lender.filterLoansByStatus("accepted");
        assertEquals("Loan[loanNumber=1, applicant=Applicant[dti=21, creditScore=700, savings=100000.0, requestedAmount=125000.0, qualification='qualified'], approvedLoanStatus='accepted', loanAmount=125000.0, approvedDate=2021-01-27]", acceptedLoan.get(0));

        List<String> rejectedLoan = lender.filterLoansByStatus("rejected");
        assertEquals("Loan[loanNumber=2, applicant=Applicant[dti=21, creditScore=700, savings=100000.0, requestedAmount=125000.0, qualification='qualified'], approvedLoanStatus='rejected', loanAmount=125000.0, approvedDate=2021-01-27]", rejectedLoan.get(0));

        List<String> expiredLoan = lender.filterLoansByStatus("expired");
        assertEquals("Loan[loanNumber=3, applicant=Applicant[dti=21, creditScore=700, savings=100000.0, requestedAmount=125000.0, qualification='qualified'], approvedLoanStatus='expired', loanAmount=125000.0, approvedDate=2021-01-23]", expiredLoan.get(0));

        List<String> deniedLoan = lender.filterLoansByStatus("denied");
        assertEquals("Loan[loanNumber=5, applicant=Applicant[dti=30, creditScore=600, savings=100000.0, requestedAmount=250000.0, qualification='not qualified'], approvedLoanStatus='denied', loanAmount=0.0, approvedDate=null]", deniedLoan.get(0));

        List<String> qualifiedLoan = lender.filterLoansByStatus("qualified");
        assertEquals("Loan[loanNumber=6, applicant=Applicant[dti=30, creditScore=700, savings=50000.0, requestedAmount=250000.0, qualification='partially qualified'], approvedLoanStatus='qualified', loanAmount=200000.0, approvedDate=null]", qualifiedLoan.get(0));

    }
}
