package alexgochi.superb.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import alexgochi.superb.R;

public class CustomAdapter extends BaseAdapter{

    private Context c;
    private ArrayList<Seminar> seminars;
    private LayoutInflater inflater;


    public CustomAdapter(Context c, ArrayList<Seminar> seminars) {
        this.c = c;
        this.seminars = seminars;

        //INITIALIZED
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return seminars.size();
    }

    @Override
    public Object getItem(int position) {
        return seminars.get(position);
    }

    @Override
    public long getItemId(int position) {
        return seminars.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.model, parent, false);
        }

        TextView seminarTxt = (TextView) convertView.findViewById(R.id.seminarTxt);
        TextView dateTxt = (TextView) convertView.findViewById(R.id.dateTxt);
        TextView descTxt = (TextView) convertView.findViewById(R.id.descTxt);

        seminarTxt.setText(seminars.get(position).getName());
        dateTxt.setText(seminars.get(position).getDate());
        descTxt.setText(seminars.get(position).getDescription());

        //ITEM CLICKS
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c, seminars.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
