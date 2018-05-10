package alexgochi.superb.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import alexgochi.superb.R;
import alexgochi.superb.helper.SQLiteHandler;

public class AccountActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView user_image;
    TextView username, email_user, phone_user;
    EditText mPhone;
    String mPhoneSet;
    SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        username = (TextView) findViewById(R.id.username);
        email_user = (TextView) findViewById(R.id.email_user);
        phone_user = (TextView) findViewById(R.id.phone_user);
        user_image = (ImageView) findViewById(R.id.image_user);
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        username.setText(name);
        email_user.setText(email);

        if (savedInstanceState != null) {
            String save_phone = savedInstanceState.getString("PHONE_NUMBER");
            phone_user.setText(String.valueOf(save_phone));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && data != null) {
            Uri selectedImage = data.getData();
            user_image.setImageURI(selectedImage);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("PHONE_NUMBER", mPhoneSet);
        super.onSaveInstanceState(outState);
    }

    public void add_phone(View view) {
        LayoutInflater inflater = AccountActivity.this.getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.add_phone, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogLayout)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPhone = dialogLayout.findViewById(R.id.note);
                        if (mPhone.getText().toString().equals("")) {
                            showToast();
                        } else if (mPhone.getText().toString().length() < 12 || mPhone.getText().toString().length() > 12) {
                            showAlert();
                        } else {
                            mPhoneSet = mPhone.getText().toString();
                            phone_user.setText(mPhoneSet);
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void showAlert() {
        Toast toast = Toast.makeText(getApplicationContext(), "Phone must 12 digit", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void showToast() {
        Toast toast = Toast.makeText(getApplicationContext(), "Phone Cannot Empty", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AccountActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
