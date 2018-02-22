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

import java.util.Objects;

import ru.nkargin.coffeeshopmanager.R;
import ru.nkargin.coffeeshopmanager.databinding.FragmentAddPositionDialogBinding;
import ru.nkargin.coffeeshopmanager.model.Good;
import ru.nkargin.coffeeshopmanager.service.GoodService;


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
        binding.setModel(good);

        if (good == null) {
            updateRemoveButtonVisibility(binding);
        }

        binding.deletePositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodService.INSTANCE.remove(good);
            }
        });
        updateValues(binding);
        binding.savePositionButton.setOnClickListener(getOnSaveHandler(binding));
        return binding.getRoot();
    }

    private void updateRemoveButtonVisibility(FragmentAddPositionDialogBinding binding) {
        ViewParent parent = binding.deletePositionButton.getParent();
        ((ViewGroup) parent).removeView(binding.deletePositionButton);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
        layoutParams.weight = 3;
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
