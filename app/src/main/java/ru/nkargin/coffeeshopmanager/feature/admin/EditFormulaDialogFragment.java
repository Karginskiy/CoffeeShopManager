package ru.nkargin.coffeeshopmanager.feature.admin;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import ru.nkargin.coffeeshopmanager.R;
import ru.nkargin.coffeeshopmanager.databinding.FragmentEditFormulaDialogBinding;
import ru.nkargin.coffeeshopmanager.service.FormulaService;
import ru.nkargin.coffeeshopmanager.service.SessionService;

public class EditFormulaDialogFragment extends DialogFragment {

    private static EditFormulaDialogFragment INSTANCE;
    private FragmentEditFormulaDialogBinding binding;

    public EditFormulaDialogFragment() {}

    public static EditFormulaDialogFragment newInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EditFormulaDialogFragment();
        }
        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_formula_dialog, container, false);

        initViewSubscription();
        binding.saveButton.setOnClickListener(updateTaxParams());
        initModel();

        return binding.getRoot();
    }

    private void initModel() {
        binding.setModel(new EditFormulasViewModel());
        binding.getModel().setPayment(FormulaService.getInstance().getPayment());
        binding.getModel().setTax(FormulaService.getInstance().getTax());
    }

    @NonNull
    private View.OnClickListener updateTaxParams() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePayment();
                updateTax();

                getDialog().dismiss();
            }
        };
    }

    private void updatePayment() {
        String s = binding.editPayment.getText().toString();
        int payment = Integer.parseInt(s);
        FormulaService.getInstance().setPaymentParam(payment);

        SessionService.getInstance().getCurrentSession().setPayment(payment);
        SessionService.getInstance().getCurrentSession().save();
    }

    private void updateTax() {
        String s1 = binding.editTax.getText().toString();
        int tax = Integer.parseInt(s1);
        FormulaService.getInstance().setTaxParam(tax);

        SessionService.getInstance().getCurrentSession().setTax(tax);
        SessionService.getInstance().getCurrentSession().save();
    }

    private void initViewSubscription() {
        final Observable<Boolean> isValidTax = RxTextView.textChanges(binding.editTax).map(onNextTax());
        final Observable<Boolean> isPaymentValid = RxTextView.textChanges(binding.editPayment).map(onNextPayment());

        Observable.combineLatest(isValidTax, isPaymentValid, onNextFromBothInputs())
                .subscribe(onFormChanged());
    }

    @NonNull
    private Function<CharSequence, Boolean> onNextTax() {
        return new Function<CharSequence, Boolean>() {
            @Override
            public Boolean apply(CharSequence charSequence) throws Exception {
                return charSequence != null && charSequence.toString().matches("^0$|^[1-9][0-9]?$|^100$");
            }
        };
    }

    @NonNull
    private Function<CharSequence, Boolean> onNextPayment() {
        return new Function<CharSequence, Boolean>() {
            @Override
            public Boolean apply(CharSequence charSequence) throws Exception {
                return charSequence != null && charSequence.toString().matches("^[0-9]+$");
            }
        };
    }

    @NonNull
    private BiFunction<Boolean, Boolean, Boolean> onNextFromBothInputs() {
        return new BiFunction<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean apply(Boolean validTaxInput, Boolean validPaymentInput) throws Exception {
                return validTaxInput && validPaymentInput;
            }
        };
    }

    @NonNull
    private Consumer<Boolean> onFormChanged() {
        return new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isValidForm) throws Exception {
                binding.saveButton.setEnabled(isValidForm);
            }
        };
    }

}
