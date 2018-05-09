package alexgochi.superb.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import alexgochi.superb.R;
import alexgochi.superb.app.AppConfig;
import alexgochi.superb.helper.SQLiteHandler;

public class SeminarActivity extends AppCompatActivity {
    TextView txtName;
    String txtSeminar;
    private InputStream inputStream = null;
    private String result = null;
    private Spinner spinnerSeminar;
    List<String> listSeminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seminar);

        txtName = (TextView) findViewById(R.id.name);
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");
        txtName.setText(name);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        spinnerSeminar = (Spinner) findViewById(R.id.add_seminar);

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(AppConfig.URL_GET_SEMINAR);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
        }
        catch (Exception e) {
            Log.e("Fail 3", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address", Toast.LENGTH_SHORT).show();
            finish();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"),8);
            StringBuilder stringBuilder = new StringBuilder();
            String dataSeminar;
            while ((dataSeminar = reader.readLine()) != null) {
                stringBuilder.append(dataSeminar).append("\n");
            }
            inputStream.close();
            result = stringBuilder.toString();

        } catch (IOException e) {
            Log.e("Fail 2", e.toString());
        }

        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject;

            String[] seminar = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length() ; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                seminar[i] = jsonObject.getString("seminar");
            }

            listSeminar = new ArrayList<>(Arrays.asList(seminar));
            spinner_fn();

        } catch (JSONException e) {
            Log.e("Fail 1", e.toString());
        }
    }

    private void spinner_fn() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SeminarActivity.this,
                R.layout.spinner_layout, listSeminar);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spinnerSeminar.setAdapter(arrayAdapter);
        spinnerSeminar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(),"Selected : " + listSeminar.get(position), Toast.LENGTH_SHORT).show();
                txtSeminar = spinnerSeminar.getSelectedItem().toString();
//                System.out.println("Item Selected : " + txtSeminar);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void passData(View view) {
        Intent intent = new Intent(SeminarActivity.this, QRActivity.class);
        intent.putExtra("KEY_NAME", txtName.getText().toString());
        intent.putExtra("KEY_SEMINAR", spinnerSeminar.getSelectedItem().toString());
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }
}
