package side.menu.sample;

import java.util.ArrayList;

import side.menu.scroll.MenuAdapter;
import side.menu.scroll.ScrollerLinearLayout;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private LinearLayout rootLayout;	
	private ScrollerLinearLayout sideSlideLayout;
	
	private ListView listView;		
	private final String[] menuTitles = {"メニュー1", 
										 "メニュー2", 
										 "メニュー3", 
										 "メニュー4", 
										 "メニュー5",
										 "メニュー6"};	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
        setMenuButton();
        setListView();
        setContent(0);
    }       
    
    private void init(){    	
    	this.sideSlideLayout = (ScrollerLinearLayout) findViewById(R.id.menu_content_side_slide_layout);
    	this.rootLayout = (LinearLayout) findViewById(R.id.menu_content_root);    	    	
    }
    
    
    private void setMenuButton(){
    	Button menuButton = (Button) findViewById(R.id.main_tmp_button);
    	menuButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				sideSlideLayout.scroll();
			}
		});
    }
    
    
    private void setListView() {
    	
    	ArrayList<String> items = new ArrayList<String>();
    	for (int i = 0; i < menuTitles.length; i++) {
    		items.add(menuTitles[i]);
		}
    	
    	listView = (ListView) findViewById(R.id.menu_content_menulist);
    	listView.setFadingEdgeLength(0);
    	
    	MenuAdapter menuAdapter = new MenuAdapter(this, items, this);
		listView.setAdapter(menuAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				setContent(arg2);
				sideSlideLayout.scroll();
			}
		});
	}
    
    private void setContent(int position){
    	rootLayout.removeAllViews();
    	TextView tmpText = new TextView(this);
    	tmpText.setText(menuTitles[position]);
    	rootLayout.addView(tmpText);
    }
}
