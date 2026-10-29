package com.guerra.localcash;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.guerra.localcash.views.CuentasFragment;
import com.guerra.localcash.views.ComprasFragment;
import com.guerra.localcash.views.DeudasFragment;
import com.guerra.localcash.views.ContactosFragment;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_cuentas) {
                selectedFragment = new CuentasFragment();
            } else if (itemId == R.id.nav_compras) {
                selectedFragment = new ComprasFragment();
            } else if (itemId == R.id.nav_deudas) {
                selectedFragment = new DeudasFragment();
            } else if (itemId == R.id.nav_contactos) {
                selectedFragment = new ContactosFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CuentasFragment())
                    .commit();
        }
    }
}