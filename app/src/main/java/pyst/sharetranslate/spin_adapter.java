package pyst.sharetranslate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alperen on 23.4.2015.
 */
public class spin_adapter extends ArrayAdapter<String> {
    Context c;
    ArrayList<String> dizi;

    LayoutInflater inflater ;
    public spin_adapter(Context ce,ArrayList<String> s){
        super(ce,R.layout.spinner_row,s);
        c=ce;
        dizi=s;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent){
        View row = inflater.inflate(R.layout.spinner_row, parent, false);
        TextView t=(TextView) row.findViewById(R.id.spinner_text);
        t.setText(dizi.get(position));
        return row;
    }
}
