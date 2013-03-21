package org.shipp.activity;

import org.shipp.util.MenuEventController;
import org.shipp.util.MenuLazyAdapter;
import org.shipp.util.OnSwipeTouchListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Android activity with menu and layout to add elements of your application
 * @author Leonardo Salles
 */
public class PrincipalActivity extends Activity {
	public static final String ID = "id";
    public static final String ICON = "icon";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    
	private RelativeLayout layout;
	private MenuLazyAdapter menuAdapter;
	private boolean open = false;
    
	private final Context context = this;
    
	private ListView listMenu;
	private TextView appName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
        this.listMenu = (ListView) findViewById(R.id.listMenu);
        this.layout = (RelativeLayout) findViewById(R.id.layoutToMove);
        this.appName = (TextView) findViewById(R.id.appName);

        this.menuAdapter = new MenuLazyAdapter(this, MenuEventController.menuArray.size() == 0 ? MenuEventController.getMenuDefault(this) : MenuEventController.menuArray);
        this.listMenu.setAdapter(menuAdapter);
        
        this.layout.setOnTouchListener(new OnSwipeTouchListener() {
            public void onSwipeRight() {
                if(!open){
                	open = true;
                	MenuEventController.open(context, layout, appName);
                	MenuEventController.closeKeyboard(context, getCurrentFocus());
                }
            }
            public void onSwipeLeft() {
            	if(open){
            		open = false;
            		MenuEventController.close(context, layout, appName);
            		MenuEventController.closeKeyboard(context, getCurrentFocus());
            	}
            }
        });
        
        this.listMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = null;
				if(position == 0){
					//action
				} else if(position == 1){
					//action
				} else if(position == 2){
					//if activity is this just close menu before verify if menu is open
					if(open){
						open = false;
						MenuEventController.close(context, layout, appName);
						MenuEventController.closeKeyboard(context, view);
	            	}
				} else if(position == 3){
					//action
				} else if(position == 4){
					//action
				} else if(position == 5){
					//action
				} else if(position == 6){
					//action
				} else if(position == 7){
					//action
				}
				
				//Check the position if different of current a intent are started else menu just closed
				if(position != 2){
					startActivity(intent);
					PrincipalActivity.this.finish();
					overridePendingTransition(R.anim.slide_left, R.anim.slide_left);
				}
			}
		});
    }

    public void openCloseMenu(View view){
    	if(!this.open){
    		this.open = true;
    		MenuEventController.open(this.context, this.layout, this.appName);
    		MenuEventController.closeKeyboard(this.context, view);
    	} else {
    		this.open = false;
    		MenuEventController.close(this.context, this.layout, this.appName);
    		MenuEventController.closeKeyboard(this.context, view);
    	}
    }
}