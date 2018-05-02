package alexgochi.superb.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import alexgochi.superb.R;
import alexgochi.superb.helper.SQLiteHandler;
import alexgochi.superb.helper.SessionManager;

public class MainActivity extends Activity {
    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtName = (TextView) findViewById(R.id.name);
//        TextView txtEmail = (TextView) findViewById(R.id.email);
        ImageView btnLogout = (ImageView) findViewById(R.id.btnLogout);
        ImageView btnAccount = (ImageView) findViewById(R.id.btnAccount);
        ImageView btnSeminar = (ImageView) findViewById(R.id.btnSeminar);
        ImageView btnAbout = (ImageView) findViewById(R.id.btnAbout);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
//        String email = user.get("email");

        // Displaying the user details on the screen
        txtName.setText(name);
//        txtEmail.setText(email);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Account Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        btnSeminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Seminar Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "About Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
