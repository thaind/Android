package side.menu.scroll;

import java.util.ArrayList;

import side.menu.sample.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter {	
	private Context context;
	private ArrayList<String> items;
	private LayoutInflater inflater;	
	

	public MenuAdapter(Context context, ArrayList<String> items, Activity act) {
		this.context = context;
		this.items = items;
		this.inflater = LayoutInflater.from(context);		
	}
			

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_menu, null);
		TextView title = (TextView) convertView.findViewById(R.id.menu_title);
		title.setText(getItem(position));			
		return convertView;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public String getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
