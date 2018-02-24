package ru.nkargin.coffeeshopmanager.feature.admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import ru.nkargin.coffeeshopmanager.R;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_admin);

        initAddPositionButton();
        initRecycler();
        initEditFormulaButton();
    }

    private void initEditFormulaButton() {
        Button editFormulaButton = findViewById(R.id.edit_formula);
        editFormulaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditFormulaDialogFragment editFormulaDialogFragment = EditFormulaDialogFragment.newInstance();
                editFormulaDialogFragment.show(getSupportFragmentManager(), "editFormulaDialog");
            }
        });
    }

    private void initAddPositionButton() {
        Button addPositionButton = findViewById(R.id.add_position_button);
        addPositionButton.setOnClickListener(getAddPositionButtonOnClickListener());
    }

    @NonNull
    private View.OnClickListener getAddPositionButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPositionDialogFragment addPositionDialogFragment = AddPositionDialogFragment.newInstance(null);
                addPositionDialogFragment.show(getFragmentManager(), "addPosition");
            }
        };
    }

    private void initRecycler() {
        RecyclerView recyclerView = findViewById(R.id.goods_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(new GoodListAdapter(this));
    }

}
