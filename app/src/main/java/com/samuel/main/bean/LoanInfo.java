package com.samuel.main.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.UUID;

public class LoanInfo extends DataSupport implements Serializable {
    private String loanId = "";
    /** 开始日期 */
    private String startDate = "";
    /** 总金额 */
    private int amount;
    /** 贷款年限 */
    private int years;
    /** 贷款利率(值为百分比, 如6.6%) */
    private double rate;

    public LoanInfo() {
        this.loanId = UUID.randomUUID().toString();
    }

    public LoanInfo(String date, int money, int year, double rate) {
        this.loanId = UUID.randomUUID().toString();
        this.startDate = date;
        this.amount = money;
        this.years = year;
        this.rate = rate;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
