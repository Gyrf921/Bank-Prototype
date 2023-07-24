/*
package com.bankprototype.creditconveyor.service;

import com.bankprototype.creditconveyor.web.dto.LoanApplicationRequestDTO;

import java.math.BigDecimal;

public interface IPrescoringCalculation {

    */
/**
     * Сумма ежемесячного платежа для расчета в десятичной системе счисления
     * @param loanAmount - сумма по кредиту
     * @param termInYears - срок кредитования в годах
     * @param creditRatio - общий коэффициент пользователя от страховки и статуса зарплаатного клиента
     *//*

     BigDecimal calculationMonthlyPaymentAmount(BigDecimal loanAmount, Integer termInYears, Integer creditRatio);

    */
/**
     * Расчет ежемесячной процентной ставки
     * @value - годовая процентаная ставка берётся из ресурса
     *//*

    Double calculationMonthlyInterestRate(Double customLoanRate);

    */
/**
     * Расчет окончания периодов начисления процентов
     * @value - количество месяцев в году статично и являеся константой
     * @param term - количество лет, на которые взяли кредит
     *//*

    Integer calculationInterestPeriodsTerm(Integer term);

    */
/**
     * Расчет общей сумма кредита
     * @param monthlyPayment - ежемесячный платёж по кредиту
     * @param countMonth - количество месяцев на которое берётся кребит
     *//*

    BigDecimal calculationTotalAmount(BigDecimal monthlyPayment, Integer countMonth);

}
*/
