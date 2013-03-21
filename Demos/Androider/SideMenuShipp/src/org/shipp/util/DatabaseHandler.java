package org.shipp.util;

import java.util.ArrayList;
import java.util.List;

import org.shipp.model.Menu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class to access menu database 
 * @author Leonardo Salles
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Shipp";
    private static final String TABLE_MENU = "menu";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String ICON = "icon";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	StringBuilder tableMenu = new StringBuilder();
    	tableMenu.append("CREATE TABLE menu ");
    	tableMenu.append("(id INTEGER PRIMARY KEY, ");
    	tableMenu.append("icon NUMBER, ");
    	tableMenu.append("title TEXT, ");
    	tableMenu.append("description TEXT)");

        db.execSQL(tableMenu.toString());
        
        this.addDefaultMenu(MenuConfigAdapter.getMenuDefault(), db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS menu");
        onCreate(db);
    }

    public List<Menu> getMenuConfig(){
        List<Menu> listMenu = new ArrayList<Menu>();
 
        String selectQuery = "SELECT * FROM menu";
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        if (cursor.moveToFirst()) {
            do {
            	Menu menu = new Menu();
            	menu.setId(cursor.getInt(0));
            	menu.setIconReference(cursor.getInt(1));
            	menu.setTitle(cursor.getString(2));
            	menu.setDescription(cursor.getString(3));
            	
            	listMenu.add(menu);
            } while (cursor.moveToNext());
        }
 
        cursor.close();
        db.close();
 
        return listMenu;
    }
    
    public Menu searchMenuById(int menuId) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = db.query(TABLE_MENU, new String[] {ID,
        												   ICON,
        												   TITLE,
        												   DESCRIPTION}, 
        												   ID + " = ?",
        												   new String[] {String.valueOf(menuId)}, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Menu menu = new Menu(cursor.getInt(0),
        					 cursor.getInt(1),
        					 cursor.getString(2),
        					 cursor.getString(3));

        cursor.close();
        db.close();
        close();

        return menu;
    }
    
    public void addDefaultMenu(List<Menu> listaMenu, SQLiteDatabase db){
    	for(int i = 0; i < listaMenu.size(); i++){
            ContentValues values = new ContentValues();
            values.put(ID, listaMenu.get(i).getId());
            values.put(ICON, listaMenu.get(i).getIconReference());
            values.put(TITLE, listaMenu.get(i).getTitle());
            values.put(DESCRIPTION, listaMenu.get(i).getDescription());

            db.insert(TABLE_MENU, null, values);
    	}
    }
}