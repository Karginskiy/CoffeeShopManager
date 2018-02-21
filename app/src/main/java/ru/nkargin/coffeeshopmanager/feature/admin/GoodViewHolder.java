package ru.nkargin.coffeeshopmanager.feature.admin;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ru.nkargin.coffeeshopmanager.databinding.GoodBinding;
import ru.nkargin.coffeeshopmanager.model.Good;


public class GoodViewHolder extends RecyclerView.ViewHolder {

    private Activity activity;
    private GoodBinding binding;

    public GoodViewHolder(final Activity activity, final GoodBinding binding) {
        super(binding.getRoot());
        this.activity = activity;
        this.binding = binding;

        binding.setViewModel(new AdminViewModel());
    }

    public void bind(final Good good) {
        binding.getViewModel().setGood(good);
        binding.goodItem.setOnClickListener(addOnItemClickListener(good));
        binding.executePendingBindings();
    }

    @NonNull
    protected View.OnClickListener addOnItemClickListener(final Good good) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPositionDialogFragment addPositionDialogFragment = AddPositionDialogFragment.newInstance(good);
                addPositionDialogFragment.show(activity.getFragmentManager(), "editPosition");
            }
        };
    }
}
