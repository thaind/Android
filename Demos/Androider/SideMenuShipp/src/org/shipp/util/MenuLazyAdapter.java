package org.shipp.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.shipp.activity.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Class to control of elements in the list menu
 * @author Leonardo Salles
 */
public class MenuLazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, Object>> data;
    private static LayoutInflater inflater = null;

    public MenuLazyAdapter(Activity a, ArrayList<HashMap<String, Object>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if(convertView == null){
            vi = inflater.inflate(R.layout.menu_list_row, null);
        }

        TextView menuTitle = (TextView) vi.findViewById(R.id.menuTitle);
        ImageView menuIcon = (ImageView) vi.findViewById(R.id.menuIcon);

        HashMap<String, Object> menuConfig = new HashMap<String, Object>();
        menuConfig = data.get(position);

        menuTitle.setText((String) menuConfig.get("title"));
        menuIcon.setImageResource((Integer) menuConfig.get("icon"));

        return vi;
    }
}