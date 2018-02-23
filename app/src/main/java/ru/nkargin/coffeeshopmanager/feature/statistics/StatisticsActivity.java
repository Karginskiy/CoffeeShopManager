package ru.nkargin.coffeeshopmanager.feature.statistics;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ru.nkargin.coffeeshopmanager.R;

public class StatisticsActivity extends AppCompatActivity {

    public static final String SELECTED_FRAGMENT_KEY = "selectedFragment";
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            selectedMenuItem = item.getItemId();
            selectedFragment = StatisticsForPeriodFragment.getInstance();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    break;
                case R.id.navigation_dashboard:
                    selectedFragment = StatisticsForRecentPeriodFragment.getInstance();
                    break;
            }

            setFragment();
            return true;
        }
    };
    private static Fragment selectedFragment;
    private static int selectedMenuItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (selectedMenuItem != -1) {
            navigation.setSelectedItemId(selectedMenuItem);
        }
        setFragment();
    }
    private void setFragment() {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(
                R.id.statistics_layout,
                selectedFragment == null ? StatisticsForPeriodFragment.getInstance() : selectedFragment
        );
        transaction.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        getSupportFragmentManager().putFragment(outState, SELECTED_FRAGMENT_KEY, selectedFragment);
    }
}
