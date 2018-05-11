package alexgochi.superb.helper;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import alexgochi.superb.app.CustomAdapter;
import alexgochi.superb.app.Seminar;

public class DataParser extends AsyncTask<Void, Void, Integer> {

    @SuppressLint("StaticFieldLeak")
    private Context c;
    @SuppressLint("StaticFieldLeak")
    private ListView lv;
    private String jsonData;

    private ProgressDialog pd;
    private ArrayList<Seminar> seminars = new ArrayList<>();

    DataParser(Context c, ListView lv, String jsonData) {
        this.c = c;
        this.lv = lv;
        this.jsonData = jsonData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(c);
        pd.setTitle("Parse");
        pd.setMessage("Parsing... Please Wait!");
        pd.show();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return this.parseData();
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        pd.dismiss();
        if (result == 0) {
            Toast.makeText(c, "Unable to Parse", Toast.LENGTH_SHORT).show();
        }else {
            //CALL ADAPTER TO BIND DATA
            CustomAdapter adapter = new CustomAdapter(c, seminars);
            lv.setAdapter(adapter);
        }
    }

    private int parseData() {
        try {
            JSONArray ja = new JSONArray(jsonData);
            JSONObject jo;

            seminars.clear();
            Seminar s;

            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                int id = jo.getInt("id");
                String name = jo.getString("name");
                String date = jo.getString("date");
                String description = jo.getString("description");

                s = new Seminar();
                s.setId(id);
                s.setName(name);
                s.setDate(date);
                s.setDescription(description);

                seminars.add(s);
            }

            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
