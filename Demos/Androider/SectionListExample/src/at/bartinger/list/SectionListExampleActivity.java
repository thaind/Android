package at.bartinger.list;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import at.bartinger.list.item.EntryAdapter;
import at.bartinger.list.item.EntryItem;
import at.bartinger.list.item.Item;
import at.bartinger.list.item.SectionItem;

public class SectionListExampleActivity extends ListActivity {
    /** Called when the activity is first created. */
	
	 ArrayList<Item> items = new ArrayList<Item>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
       
        items.add(new SectionItem("Category 1"));
        items.add(new EntryItem("Item 1", "This is item 1.1"));
        items.add(new EntryItem("Item 2", "This is item 1.2"));
        items.add(new EntryItem("Item 3", "This is item 1.3"));
        
        
        items.add(new SectionItem("Category 2"));
        items.add(new EntryItem("Item 4", "This is item 2.1"));
        items.add(new EntryItem("Item 5", "This is item 2.2"));
        items.add(new EntryItem("Item 6", "This is item 2.3"));
        items.add(new EntryItem("Item 7", "This is item 2.4"));
        
        items.add(new SectionItem("Category 3"));
        items.add(new EntryItem("Item 8", "This is item 3.1"));
        items.add(new EntryItem("Item 9", "This is item 3.2"));
        items.add(new EntryItem("Item 10", "This is item 3.3"));
        items.add(new EntryItem("Item 11", "This is item 3.4"));
        items.add(new EntryItem("Item 12", "This is item 3.5"));
        
        EntryAdapter adapter = new EntryAdapter(this, items);
        
        setListAdapter(adapter);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	
    	if(!items.get(position).isSection()){
    		
    		EntryItem item = (EntryItem)items.get(position);
    		
    		Toast.makeText(this, "You clicked " + item.title , Toast.LENGTH_SHORT).show();

    		
    	}
    	
    	super.onListItemClick(l, v, position, id);
    }
}