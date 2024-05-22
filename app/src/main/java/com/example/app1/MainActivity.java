package com.example.app1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        databaseHelper = new DatabaseHelper(this);

        delete_and_add_new_table_with_values();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (validateLogin(username, password)) {
                    String userId = databaseHelper.getUserId(username); // Assuming you have this method
                    storeUserIdInSharedPreferences(userId);
                    navigateToMenuActivity();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void delete_and_add_new_table_with_values(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        db.delete("user_table",null,null);

        databaseHelper.add("admin","pass");
        databaseHelper.add("1","1");
        databaseHelper.add("ali","ali123");
    }
    private boolean validateLogin(String username, String password) {
        // Implement your login validation logic here
        return databaseHelper.validateUser(username, password); // Assuming you have this method
    }

    private void storeUserIdInSharedPreferences(String userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id", userId);
        editor.apply();
    }

    private void navigateToMenuActivity() {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
