package ru.nkargin.coffeeshopmanager.feature.admin;

import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import ru.nkargin.coffeeshopmanager.R;
import ru.nkargin.coffeeshopmanager.databinding.FragmentAddPositionDialogBinding;
import ru.nkargin.coffeeshopmanager.model.Good;
import ru.nkargin.coffeeshopmanager.service.GoodService;

import static com.jakewharton.rxbinding2.widget.RxTextView.textChanges;


public class AddPositionDialogFragment extends DialogFragment {

    private Good good;

    public AddPositionDialogFragment() {}

    public static AddPositionDialogFragment newInstance(Good good) {
        AddPositionDialogFragment addPositionDialogFragment = new AddPositionDialogFragment();
        addPositionDialogFragment.good = good;
        return addPositionDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FragmentAddPositionDialogBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_add_position_dialog, container, false);

        setGoodIfExists(savedInstanceState, binding);

        binding.deletePositionButton.setOnClickListener(getOnDeleteHandler());
        binding.savePositionButton.setOnClickListener(getOnSaveHandler(binding));

        initFormValidation(binding);

        updateValues(binding);

        return binding.getRoot();
    }

    private void initFormValidation(final FragmentAddPositionDialogBinding binding) {
        Observable<Boolean> editTitleValid = RxTextView.textChanges(binding.editTitle).map(onEditTitleInputUpdate());
        Observable<Boolean> editPriceValid = RxTextView.textChanges(binding.editPrice).map(onEditPriceInputUpdate());
        Observable.combineLatest(editPriceValid, editTitleValid, onOneOfFormsUpdate())
                .subscribe(onFormValidityUpdate(binding));
    }

    @NonNull
    private Function<CharSequence, Boolean> onEditTitleInputUpdate() {
        return new Function<CharSequence, Boolean>() {
            @Override
            public Boolean apply(CharSequence charSequence) throws Exception {
                return charSequence != null && charSequence.toString().matches("[0-9A-Za-zА-Яа-я]+");
            }
        };
    }

    @NonNull
    private Function<CharSequence, Boolean> onEditPriceInputUpdate() {
        return new Function<CharSequence, Boolean>() {
            @Override
            public Boolean apply(CharSequence charSequence) throws Exception {
                return charSequence != null && charSequence.toString().matches("[0-9]+");
            }
        };
    }

    @NonNull
    private BiFunction<Boolean, Boolean, Boolean> onOneOfFormsUpdate() {
        return new BiFunction<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean apply(Boolean firstInputValid, Boolean secondInputValid) throws Exception {
                return firstInputValid && secondInputValid;
            }
        };
    }

    @NonNull
    private Consumer<Boolean> onFormValidityUpdate(final FragmentAddPositionDialogBinding binding) {
        return new Consumer<Boolean>() {
            @Override
            public void accept(Boolean bothFormsValid) throws Exception {
                binding.savePositionButton.setEnabled(bothFormsValid);
            }
        };
    }

    @NonNull
    private View.OnClickListener getOnDeleteHandler() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodService.INSTANCE.remove(good);
                getDialog().dismiss();
            }
        };
    }

    private void setGoodIfExists(Bundle savedInstanceState, FragmentAddPositionDialogBinding binding) {
        if (savedInstanceState != null) {
            good = (Good) savedInstanceState.getSerializable("good");
        }

        binding.setModel(good);

        if (good == null) {
            updateRemoveButtonVisibility(binding);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveGood(outState);
    }

    private void saveGood(Bundle outState) {
        if (good != null) {
            outState.putSerializable("good", good);
        }
    }

    private void updateRemoveButtonVisibility(FragmentAddPositionDialogBinding binding) {
        ViewParent parent = binding.deletePositionButton.getParent();
        ((ViewGroup) parent).removeView(binding.deletePositionButton);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
        layoutParams.weight = 4;
        binding.savePositionButton.setLayoutParams(layoutParams);
    }

    private void updateValues(FragmentAddPositionDialogBinding binding) {
        if (good != null) {
            binding.editTitle.setText(good.getTitle());
            binding.editPrice.setText(String.valueOf(good.getPrice()));
        }
    }

    @NonNull
    private View.OnClickListener getOnSaveHandler(final FragmentAddPositionDialogBinding binding) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = binding.editTitle.getText().toString();

                int price = 0;
                if (!binding.editPrice.getText().toString().equals("")) {
                    price = Integer.valueOf(binding.editPrice.getText().toString());
                }
                if (good == null) {
                    good = new Good();
                }

                good.setTitle(title);
                good.setPrice(price);

                GoodService.INSTANCE.save(good);
                getDialog().dismiss();
            }
        };
    }

}
