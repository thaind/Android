package com.example.lrucachewithdifferentsize;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.LruCache;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	  public abstract class CacheGalleryBaseAdapter extends BaseAdapter {

		     ArrayList<String> GalleryFileList;
		     Context context;
		     
		     CacheGalleryBaseAdapter(Context cont){
		      context = cont;
		      GalleryFileList = new ArrayList<String>();    
		  }
		     
		     abstract Bitmap abstractGetBitmapFromMemCache(String key);
		     abstract BitmapWorkerTask abstractStartBitmapWorkerTask(ImageView imageView);
		     
		  @Override
		  public int getCount() {
		   return GalleryFileList.size();
		  }

		  @Override
		  public Object getItem(int position) {
		   return GalleryFileList.get(position);
		  }

		  @Override
		  public long getItemId(int position) {
		   return position;
		  }

		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {

		   ImageView imageView = new ImageView(context);
		   
		   //---
		   // Use the path as the key to LruCache
		   final String imageKey = GalleryFileList.get(position);

		      final Bitmap bm = abstractGetBitmapFromMemCache(imageKey);
		      if (bm == null){
		       BitmapWorkerTask task = abstractStartBitmapWorkerTask(imageView);
		       task.execute(imageKey);
		      }
		   
		   LinearLayout layout = new LinearLayout(context);
		   layout.setLayoutParams(new Gallery.LayoutParams(250, 250));
		   layout.setGravity(Gravity.CENTER);

		   imageView.setLayoutParams(new Gallery.LayoutParams(200, 200));
		   imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		   imageView.setImageBitmap(bm);
		   
		   layout.addView(imageView);
		   return layout;

		  }
		  
		  public void add(String newitem){
		   GalleryFileList.add(newitem);
		  }

		 }
		    
		    public class CacheGalleryBaseAdapter_SmallSize extends CacheGalleryBaseAdapter{

		     CacheGalleryBaseAdapter_SmallSize(Context cont) {
		   super(cont);
		  }

		  @Override
		  Bitmap abstractGetBitmapFromMemCache(String key) {
		   return getBitmapFromMemCache_SmallSize(key);
		  }

		  @Override
		  BitmapWorkerTask abstractStartBitmapWorkerTask(ImageView imageView) {
		   return (new BitmapWorkerTask_SmallSize(imageView));
		  }
		     
		    }
		    
		    public class CacheGalleryBaseAdapter_BigSize extends CacheGalleryBaseAdapter{

		     CacheGalleryBaseAdapter_BigSize(Context cont) {
		   super(cont);
		  }

		  @Override
		  Bitmap abstractGetBitmapFromMemCache(String key) {
		   return getBitmapFromMemCache_BigSize(key);
		  }

		  @Override
		  BitmapWorkerTask abstractStartBitmapWorkerTask(ImageView imageView) {
		   return(new BitmapWorkerTask_BigSize(imageView));
		  }
		     
		    }

		    CacheGalleryBaseAdapter_SmallSize myFastGallery1BaseAdapter;
		    CacheGalleryBaseAdapter_BigSize myFastGallery2BaseAdapter;
		    
		    Gallery myFastGallery1, myFastGallery2;
		    
		    private LruCache<String, Bitmap> mMemoryCache_SmallSize;
		    private LruCache<String, Bitmap> mMemoryCache_BigSize;
		    
		 @Override
		    public void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.activity_main);
		        myFastGallery1 = (Gallery)findViewById(R.id.fastgallery1);
		        myFastGallery2 = (Gallery)findViewById(R.id.fastgallery2);
		        
		        myFastGallery1BaseAdapter = new CacheGalleryBaseAdapter_SmallSize(this);
		        myFastGallery2BaseAdapter = new CacheGalleryBaseAdapter_BigSize(this);
		        
		        String ExternalStorageDirectoryPath = Environment
		    .getExternalStorageDirectory()
		    .getAbsolutePath();
		        
		        String targetPath = ExternalStorageDirectoryPath + "/test/";
		        
		        File targetDirector = new File(targetPath);
		        
		        File[] files = targetDirector.listFiles();
		        for (File file : files){
		         myFastGallery1BaseAdapter.add(file.getPath());
		         myFastGallery2BaseAdapter.add(file.getPath());
		  }
		        
		        myFastGallery1.setAdapter(myFastGallery1BaseAdapter);
		        myFastGallery2.setAdapter(myFastGallery2BaseAdapter);
		        
		        // Get memory class of this device, exceeding this amount will throw an
		        // OutOfMemory exception.
		        final int memClass = ((ActivityManager)getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();

		        final int cacheSize_SmallSize = 1024 * 1024 * memClass / 12;
		        mMemoryCache_SmallSize = new LruCache_customSize(cacheSize_SmallSize);
		        
		        final int cacheSize_BigSize = 1024 * 1024 * memClass;
		        mMemoryCache_BigSize = new LruCache_customSize(cacheSize_BigSize);

		    }
		 
		 class LruCache_customSize extends LruCache<String, Bitmap>{

		  public LruCache_customSize(int maxSize) {
		   super(maxSize);
		   // TODO Auto-generated constructor stub
		  }
		  
		  protected int sizeOf(String key, Bitmap bitmap) {
		      // The cache size will be measured in bytes rather than number of items.
		      return bitmap.getByteCount(); 
		     }
		  
		 }
		 
		 abstract class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap>{
		  
		  private final WeakReference<ImageView> imageViewReference;
		  
		  public BitmapWorkerTask(ImageView imageView) {
		         // Use a WeakReference to ensure the ImageView can be garbage collected
		         imageViewReference = new WeakReference<ImageView>(imageView);
		     }
		  
		  @Override
		     protected void onPostExecute(Bitmap bitmap) {
		         if (imageViewReference != null && bitmap != null) {
		             final ImageView imageView = (ImageView)imageViewReference.get();
		             if (imageView != null) {
		                 imageView.setImageBitmap(bitmap);
		             }
		         }
		     }
		  
		 }
		 
		 public Bitmap getBitmapFromMemCache_SmallSize(String key) {
		  return (Bitmap) mMemoryCache_SmallSize.get(key);  
		 }
		 
		 public Bitmap getBitmapFromMemCache_BigSize(String key) {
		  return (Bitmap) mMemoryCache_BigSize.get(key);  
		 }
		 
		 class BitmapWorkerTask_SmallSize extends BitmapWorkerTask{

		  public BitmapWorkerTask_SmallSize(ImageView imageView) {
		   super(imageView);
		   // TODO Auto-generated constructor stub
		  }
		  
		  @Override
		  protected Bitmap doInBackground(String... params) {
		   final Bitmap bitmap = decodeSampledBitmapFromUri(params[0], 200, 200);

		   if( getBitmapFromMemCache_SmallSize(String.valueOf(params[0])) == null){
		    mMemoryCache_SmallSize.put(String.valueOf(params[0]), bitmap); 
		   }
		         
		         return bitmap;
		  }
		  
		 }
		 
		 class BitmapWorkerTask_BigSize extends BitmapWorkerTask{

		  public BitmapWorkerTask_BigSize(ImageView imageView) {
		   super(imageView);
		   // TODO Auto-generated constructor stub
		  }
		  
		  @Override
		  protected Bitmap doInBackground(String... params) {
		   final Bitmap bitmap = decodeSampledBitmapFromUri(params[0], 200, 200);

		   if( getBitmapFromMemCache_BigSize(String.valueOf(params[0])) == null){
		    mMemoryCache_BigSize.put(String.valueOf(params[0]), bitmap); 
		   }
		         
		         return bitmap;
		  }
		  
		 }
		    
		    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
		     Bitmap bm = null;
		     
		     // First decode with inJustDecodeBounds=true to check dimensions
		     final BitmapFactory.Options options = new BitmapFactory.Options();
		     options.inJustDecodeBounds = true;
		     BitmapFactory.decodeFile(path, options);
		      
		     // Calculate inSampleSize
		     options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		      
		     // Decode bitmap with inSampleSize set
		     options.inJustDecodeBounds = false;
		     bm = BitmapFactory.decodeFile(path, options); 
		     
		     return bm; 
		    }
		    
		    public int calculateInSampleSize(
		      BitmapFactory.Options options, int reqWidth, int reqHeight) {
		     // Raw height and width of image
		     final int height = options.outHeight;
		     final int width = options.outWidth;
		     int inSampleSize = 1;
		     
		     if (height > reqHeight || width > reqWidth) {
		      if (width > height) {
		       inSampleSize = Math.round((float)height / (float)reqHeight);  
		      } else {
		       inSampleSize = Math.round((float)width / (float)reqWidth);  
		      }  
		     }
		     
		     return inSampleSize;  
		    }
}
