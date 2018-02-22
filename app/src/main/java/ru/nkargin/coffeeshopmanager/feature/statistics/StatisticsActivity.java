package ru.nkargin.coffeeshopmanager.feature.statistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ru.nkargin.coffeeshopmanager.R;

public class StatisticsActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = StatisticsForPeriodFragment.getInstance();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    break;
                case R.id.navigation_dashboard:
                    selectedFragment = StatisticsForRecentPeriodFragment.getInstance();
                    break;
            }

            setFragment(selectedFragment);
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setFragment(StatisticsForPeriodFragment.getInstance());
    }

    private void setFragment(Fragment fragment) {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.statistics_layout, fragment);
        transaction.commit();
    }
}
