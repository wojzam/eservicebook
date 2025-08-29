//package com.eservicebook.app.not_used;
//
//import android.os.Bundle;
//import android.view.MenuItem;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.eservicebook.app.R;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//public class OLDMainScreenActivity extends AppCompatActivity {
//
//    private final HomeFragment home = new HomeFragment(this);
//    private final CalendarFragment calendar = new CalendarFragment();
//    private final AccountFragment account = new AccountFragment();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnItemSelectedListener(this::bottomNavMethod);
//        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
//    }
//
//    private boolean bottomNavMethod(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.navigation_home:
//                getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();
//                return true;
////                TODO:do zmiany 2
////            case R.id.navigation_calendar:
////                getSupportFragmentManager().beginTransaction().replace(R.id.container, calendar).commit();
////                return true;
//            case R.id.navigation_account:
//                getSupportFragmentManager().beginTransaction().replace(R.id.container, account).commit();
//                return true;
//        }
//        return false;
//    }
//}