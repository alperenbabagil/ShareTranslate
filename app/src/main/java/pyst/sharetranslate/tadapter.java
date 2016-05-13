package pyst.sharetranslate;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alperen on 22.4.2015.
 */
public class tadapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> contactList;
    LayoutInflater mInflater;
    Context c;
    Display d;



    tadapter(ArrayList<HashMap<String, String>> l, Context c, Display di){
        contactList=l;

        mInflater = (LayoutInflater) c.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.c=c;
        d=di;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row, null);
        }
        TextView tsol=(TextView)convertView.findViewById(R.id.sol);
        tsol.setText(contactList.get(position).get("sagment"));
        //tsol.setMovementMethod(new ScrollingMovementMethod());
        TextView tsag=(TextView)convertView.findViewById(R.id.sag);
        tsag.setText(contactList.get(position).get("translation"));
        LinearLayout lsol=(LinearLayout) convertView.findViewById(R.id.lsol);
        LinearLayout lsag=(LinearLayout) convertView.findViewById(R.id.lsag);
        lsol.getLayoutParams().width=d.getWidth()/2;
        lsag.getLayoutParams().width=d.getWidth()/2;



        /*Toast.makeText(c,contactList.size()+"   new",Toast.LENGTH_SHORT).show();
            Toast.makeText(c,"ffffffffff",Toast.LENGTH_SHORT).show();*/

        return convertView;

    }
}
