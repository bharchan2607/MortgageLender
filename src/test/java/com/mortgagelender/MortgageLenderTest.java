package com.mortgagelender;

import com.mortgagelender.exception.NotQualifiedApplicantException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MortgageLenderTest {

    MortgageLender lender;
    @BeforeEach
    public void setup(){
        lender = new MortgageLender(100000);
    }
    /**
     * When I check my available funds
     * Then I should see how much funds I currently have
     */
    @Test
    public void checkAvailableFunds(){
        assertEquals(100000, lender.checkAvailableFunds());
    }

    /**
     * Given I have <current_amount> available funds
     * When I add <deposit_amount>
     * Then my available funds should be <total>
     *
     * Examples:
     * | current_amount | deposit_amount |   total  |
     * |     100,000    |      50,000    | 150,000  |
     * |     200,000    |      30,000    | 230,000  |
     */
    @Test
    public void depositAmountToFunds(){
        lender.deposit(50000);
        assertEquals(150000,lender.checkAvailableFunds());
    }

    /**
     * Rule: To qualify for the full amount, candidates must have debt-to-income (DTI) less than 36%, credit score above 620
     * and savings worth 25% of requested loan amount.
     *
     * Rule: To partially qualify, candidates must still meet the same dti and credit score thresholds.
     * The loan amount for partial qualified applications is four times the applicant's savings.
     *
     * Given a loan applicant with <dti>, <credit_score>, and <savings>
     * When they apply for a loan with <requested_amount>
     * Then their qualification is <qualification>
     * And their loan amount is <loan_amount>
     * And their loan status is <status>
     *
     * Example:
     * |  requested_amount  |   dti  |  credit_score  |  savings  |     qualification    |  loan_amount  |   status   |
     * |      250,000       |   21   |       700      | 100,000   |       qualified      |   250,000     |  qualified |
     * |      250,000       |   37   |       700      | 100,000   |     not qualified    |         0     |  denied    |
     * |      250,000       |   30   |       600      | 100,000   |     not qualified    |         0     |  denied    |
     * |      250,000       |   30   |       700      |  50,000   |  partially qualified |   200,000     |  qualified |
     */
    @Test
    public void loanApplicationQualification(){
        lender.deposit(200000);
        Applicant applicant = new Applicant(21, 700, 100000,250000);
        lender.setApplicant(applicant);
        lender.approveLoan();
        assertEquals("qualified", applicant.getQualification());
        assertEquals(250000, applicant.getLoanAmount());
        assertEquals("qualified", applicant.getStatus());

        Applicant applicant1 = new Applicant(37, 700, 100000,250000);
        lender.setApplicant(applicant1);
        lender.approveLoan();
        assertEquals("not qualified", applicant1.getQualification());
        assertEquals(0, applicant1.getLoanAmount());
        assertEquals("denied", applicant1.getStatus());

        Applicant applicant2 = new Applicant(30, 600, 100000,250000);
        lender.setApplicant(applicant2);
        lender.approveLoan();
        assertEquals("not qualified", applicant2.getQualification());
        assertEquals(0, applicant2.getLoanAmount());
        assertEquals("denied", applicant2.getStatus());

        Applicant applicant3 = new Applicant(30, 700, 50000,250000);
        lender.setApplicant(applicant3);
        lender.approveLoan();
        assertEquals("partially qualified", applicant3.getQualification());
        assertEquals(200000, applicant3.getLoanAmount());
        assertEquals("qualified", applicant3.getStatus());

    }
    /**
     * Given I have <available_funds> in available funds
     * When I process a qualified loan
     * Then the loan status is set to <status>
     *
     * Example:
     * | loan_amount | available_funds |    status  |
     * |   125,000   |    100,000      |   on hold  |
     * |   125,000   |    200,000      |  approved  |
     * |   125,000   |    125,000      |  approved  |
     *
     * When I process a not qualified loan
     * Then I should see a warning to not proceed
     */
    @Test
    public void approveLoan(){
        Applicant applicant = new Applicant(21, 700, 100000,125000);
        lender.setApplicant(applicant);
        lender.sanctionLoan();
        assertEquals("on hold", applicant.getApprovedLoanStatus());

        lender.deposit(25000);
        Applicant applicant2 = new Applicant(21, 700, 100000,125000);
        lender.setApplicant(applicant2);
        lender.sanctionLoan();
        assertEquals("approved", applicant2.getApprovedLoanStatus());

        lender.deposit(100000);
        Applicant applicant1 = new Applicant(21, 700, 100000,125000);
        lender.setApplicant(applicant1);
        lender.sanctionLoan();
        assertEquals("approved", applicant1.getApprovedLoanStatus());

    }

    @Test
    public void displayWarningNotQualifiedLoan(){
        Applicant applicant = new Applicant(38, 700, 100000,125000);
        lender.setApplicant(applicant);
        NotQualifiedApplicantException exception = assertThrows(NotQualifiedApplicantException.class, ()->lender.sanctionLoan());
        assertEquals("You can't proceed with the loan application", exception.getMessage());
    }

    /**
     * Given I have approved a loan
     * Then the requested loan amount is moved from available funds to pending funds
     * And I see the available and pending funds reflect the changes accordingly
     */
    @Test
    public void moveLoanAmountToPendingFunds(){
        lender.deposit(200000);
        Applicant applicant1 = new Applicant(21, 700, 100000,125000);
        lender.setApplicant(applicant1);
        lender.sanctionLoan();
        assertEquals(125000, lender.getPendingFund());
        assertEquals(175000, lender.getCurrentAmount());
    }
    /**
     * Given I have an approved loan
     * When the applicant accepts my loan offer
     * Then the loan amount is removed from the pending funds
     * And the loan status is marked as accepted
     *
     * Given I have an approved loan
     * When the applicant rejects my loan offer
     * Then the loan amount is moved from the pending funds back to available funds
     * And the loan status is marked as rejected
     */
    @Test
    public void loanAcceptanceStatus(){
        lender.deposit(200000);
        Applicant applicant1 = new Applicant(21, 700, 100000,125000);
        lender.setApplicant(applicant1);
        lender.sanctionLoan();
        String status =lender.loanAccepted(true);
        assertEquals("accepted",status);
        assertEquals(0,lender.getPendingFund());

    }
    @Test
    public void loanRejectedStatus(){
        lender.deposit(200000);
        Applicant applicant1 = new Applicant(21, 700, 100000,125000);
        lender.setApplicant(applicant1);
        lender.sanctionLoan();
        String status =lender.loanAccepted(false);
        assertEquals("rejected",status);
        assertEquals(0,lender.getPendingFund());
        assertEquals(300000,lender.checkAvailableFunds());

    }
}
