package org.shipp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.shipp.activity.R;
import org.shipp.model.Menu;

import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Class to control event of menu, open and close using a static methods 
 * @author Leonardo Salles
 */
public class MenuEventController {
	public static final String ID = "id";
    public static final String ICON = "icon";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static ArrayList<HashMap<String, Object>> menuArray = new ArrayList<HashMap<String,Object>>();
    public static Display display;
    
    public static void open(final Context context, final RelativeLayout layout, final TextView name){
    	Animation in = AnimationUtils.loadAnimation(context, R.anim.push_right_out_80);
    	layout.startAnimation(in);
    	
    	in.setAnimationListener(new Animation.AnimationListener(){
    	    @Override
    	    public void onAnimationStart(Animation arg0) {
    	    }           
    	    @Override 
    	    public void onAnimationRepeat(Animation arg0) {
    	    }           
    	    @Override
    	    public void onAnimationEnd(Animation arg0) {
    	    	name.setVisibility(View.INVISIBLE);
    	    	TranslateAnimation anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                anim.setDuration(1);
                layout.startAnimation(anim);
                
                display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                int left = (int) (display.getWidth() * 0.8);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(layout.getLayoutParams());
                params.setMargins(left, 0, 0, 0);
                layout.setLayoutParams(params);
    	    }
    	});
    }
    
    public static void close(final Context context, final RelativeLayout layout, final TextView name){
    	name.setVisibility(View.VISIBLE);
    	Animation out = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
    	layout.startAnimation(out);
    	
    	RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layout.getLayoutParams();
    	params.setMargins(0, 0, 0, 0);
    	layout.setLayoutParams(params);
    }
    
    public static void closeKeyboard(final Context context, final View view){
    	InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    
    public static ArrayList<HashMap<String, Object>> getMenuDefault(final Context context){
		menuArray = new ArrayList<HashMap<String, Object>>();
		
		DatabaseHandler databaseHandler = new DatabaseHandler(context);
		List<Menu> listItemMenu = databaseHandler.getMenuConfig();

		for(int i = 0; i < listItemMenu.size(); i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(ID, listItemMenu.get(i).getId());
			map.put(ICON, listItemMenu.get(i).getIconReference());
			map.put(TITLE, listItemMenu.get(i).getTitle());
			map.put(DESCRIPTION, listItemMenu.get(i).getDescription());

			menuArray.add(map);
		}

		return menuArray;
    }
}
