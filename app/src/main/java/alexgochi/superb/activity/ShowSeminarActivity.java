package alexgochi.superb.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import alexgochi.superb.R;
import alexgochi.superb.app.AppConfig;
import alexgochi.superb.helper.Downloader;

public class ShowSeminarActivity extends AppCompatActivity {

    String urlAddress = AppConfig.URL_DATA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_seminar);

        final ListView listView = (ListView) findViewById(R.id.lv);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Downloader d = new Downloader(ShowSeminarActivity.this, urlAddress, listView);
                d.execute();
            }
        });
    }

}
