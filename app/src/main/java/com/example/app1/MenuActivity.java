package com.example.app1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        databaseHelper = new DatabaseHelper(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            // Do nothing when an item is selected
            return true;
        });

        Button topRightButton = findViewById(R.id.top_right_button);
        topRightButton.setOnClickListener(v -> navigateToQRCodeActivity());
    }

    private void navigateToQRCodeActivity() {
        String userId = getUserIdFromSharedPreferences();
        if (userId != null) {
            Intent intent = new Intent(MenuActivity.this, QRCodeActivity.class);
            intent.putExtra("ID", userId);
            startActivity(intent);
        }
    }

    private String getUserIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("user_id", null);
    }
}
