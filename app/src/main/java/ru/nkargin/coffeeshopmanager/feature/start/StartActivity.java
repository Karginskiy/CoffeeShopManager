package ru.nkargin.coffeeshopmanager.feature.start;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;

import java.util.NoSuchElementException;

import ru.nkargin.coffeeshopmanager.R;
import ru.nkargin.coffeeshopmanager.feature.admin.AdminActivity;
import ru.nkargin.coffeeshopmanager.feature.statistics.StatisticsActivity;
import ru.nkargin.coffeeshopmanager.feature.trade.TradeSessionActivity;
import ru.nkargin.coffeeshopmanager.model.Session;
import ru.nkargin.coffeeshopmanager.service.SessionService;

public class StartActivity extends AppCompatActivity {

    private Button statisticsButton;
    private Button adminButton;
    private Button startSessionButton;
    private SessionService sessionService = SessionService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initButtons();
    }

    private void initButtons() {
        statisticsButton = findViewById(R.id.statistics_button);
        adminButton = findViewById(R.id.admin_panel);

        if (sessionService.getCurrentUser().isAdmin()) {
            addButtonsClickListeners();
        } else {
            removeButtonsIfNotAdmin();
        }

        initStartSessionButton();
    }

    private void addButtonsClickListeners() {
        statisticsButton.setOnClickListener(getStatisticsButtonOnClickListener());
        adminButton.setOnClickListener(getOnAdminButtonClickListener());
    }

    private void removeButtonsIfNotAdmin() {
        ViewGroup parent = (ViewGroup) statisticsButton.getParent();
        parent.removeView(statisticsButton);

        parent = (ViewGroup) adminButton.getParent();
        parent.removeView(adminButton);
    }

    private void initStartSessionButton() {
        startSessionButton = findViewById(R.id.start_session_button);
        startSessionButton.setOnClickListener(getOnStartSessionHandler());
        updateStartSessionButtonText();
    }

    @NonNull
    private void showErrorSessionDialog() {
        new AlertDialog.Builder(StartActivity.this)
                .setMessage(R.string.cannot_create_new_session)
                .setTitle(R.string.error_title)
                .setPositiveButton(R.string.confirm_label, dismissDialog())
                .show();
    }

    @NonNull
    private DialogInterface.OnClickListener dismissDialog() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStartSessionButtonText();
    }

    private void updateStartSessionButtonText() {
        if (sessionService.getCurrentSession() != null && !sessionService.getCurrentSession().isClosed()) {
            startSessionButton.setText(R.string.continue_session);
        } else {
            startSessionButton.setText(R.string.start_session);
        }
    }

    @NonNull
    private View.OnClickListener getOnStartSessionHandler() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Session session = sessionService.startSessionOrRetrieve(sessionService.getCurrentUser());
                if (session == null) {
                    showErrorSessionDialog();
                    return;
                }
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
