package ru.nkargin.coffeeshopmanager.service;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.List;

import ru.nkargin.coffeeshopmanager.model.FormulaParam;
import ru.nkargin.coffeeshopmanager.model.Session;

public class FormulaService {

    private static FormulaService INSTANCE;
    private static final String TAX_PARAM = "tax";
    private static final String PAYMENT_PARAM = "payment";

    private final HashMap<String, FormulaParam> params = new HashMap<>();

    public static FormulaService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FormulaService();
            INSTANCE.fillMapWithCurrentParams();
        }

        return INSTANCE;
    }

    public void setTaxParam(Integer integer) {
        FormulaParam formulaParam = params.get(TAX_PARAM);
        formulaParam.setValue(integer);

        formulaParam.save();

        Session currentSession = SessionService.getInstance().getCurrentSession();
        if (currentSession != null) {
            currentSession.setTax(integer);
            currentSession.save();
        }
    }

    public void setPaymentParam(Integer integer) {
        FormulaParam formulaParam = params.get(PAYMENT_PARAM);
        formulaParam.setValue(integer);

        formulaParam.save();

        Session currentSession = SessionService.getInstance().getCurrentSession();
        if (currentSession != null) {
            currentSession.setPayment(integer);
            currentSession.save();
        }
    }

    public int getTax() {
        return params.get(TAX_PARAM).getValue();
    }

    public int getPayment() {
        return params.get(PAYMENT_PARAM).getValue();
    }

    private void fillMapWithCurrentParams() {
        List<FormulaParam> formulaParams = FormulaParam.find(FormulaParam.class, "param_name = ? OR param_name = ?", TAX_PARAM, PAYMENT_PARAM);
        if (!formulaParams.isEmpty()) {
            for (FormulaParam formulaParam : formulaParams) {
                params.put(formulaParam.getParamName(), formulaParam);
            }
        } else {
            createDefaultFormulaParams();
        }
    }

    private void createDefaultFormulaParams() {
        FormulaParam tax = createDefaultParam(TAX_PARAM);
        FormulaParam payment = createDefaultParam(PAYMENT_PARAM);

        tax.save();
        payment.save();

        params.put(TAX_PARAM, tax);
        params.put(PAYMENT_PARAM, tax);
    }

    @NonNull
    private FormulaParam createDefaultParam(String name) {
        FormulaParam tax = new FormulaParam();
        tax.setParamName(name);
        tax.setValue(0);
        return tax;
    }

}
