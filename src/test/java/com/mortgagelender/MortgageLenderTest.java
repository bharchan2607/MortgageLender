package com.mortgagelender;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
