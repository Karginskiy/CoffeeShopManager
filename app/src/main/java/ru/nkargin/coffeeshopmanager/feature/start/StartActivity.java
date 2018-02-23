package ru.nkargin.coffeeshopmanager.feature.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import ru.nkargin.coffeeshopmanager.R;
import ru.nkargin.coffeeshopmanager.feature.admin.AdminActivity;
import ru.nkargin.coffeeshopmanager.feature.statistics.StatisticsActivity;
import ru.nkargin.coffeeshopmanager.feature.trade.TradeSessionActivity;
import ru.nkargin.coffeeshopmanager.service.SessionService;

public class StartActivity extends AppCompatActivity {

    private Button statisticsButton;
    private Button adminButton;
    private Button startSessionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initButtons();
    }

    private void initButtons() {
        statisticsButton = findViewById(R.id.statistics_button);
        statisticsButton.setOnClickListener(getStatisticsButtonOnClickListener());

        adminButton = findViewById(R.id.admin_panel);
        adminButton.setOnClickListener(getOnAdminButtonClickListener());

        startSessionButton = findViewById(R.id.start_session_button);
        startSessionButton.setOnClickListener(getOnStartSessionClickButton());
    }

    @NonNull
    private View.OnClickListener getOnStartSessionClickButton() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionService.getInstance().startSessionOrRetrieve(SessionService.getInstance().getCurrentUser());
                startActivity(new Intent(StartActivity.this, TradeSessionActivity.class));
            }
        };
    }


    @NonNull
    private View.OnClickListener getStatisticsButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, StatisticsActivity.class));
            }
        };
    }


    @NonNull
    private View.OnClickListener getOnAdminButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, AdminActivity.class));
            }
        };
    }
}
