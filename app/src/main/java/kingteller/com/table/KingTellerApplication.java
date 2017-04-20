package kingteller.com.table;

import java.io.File;
import java.util.Stack;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * KingTellerApplication全局
 * @author 王定波
 *
 */
public class KingTellerApplication extends Application {

	private static KingTellerApplication application;
	private SharedPreferences preferences;
	
    private static Stack<Activity> activityLists = new Stack<>();
    
	@Override
	public void onCreate() {
		super.onCreate();
		
		application = this;
		
		// 初始化缓存目录
		initData();
		
	}

    public static final KingTellerApplication getApplication() {
        return application;
    }
	
	private void initData() {
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		

		
		//初始化异常捕获
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(getApplicationContext());
	}

	



	

	

	
	/** 
     * Activity关闭时，删除Activity列表中的Activity对象
     */  
    public static void removeActivity(Activity a){  
    	if(a != null){
    		activityLists.remove(a);
    	}
    }  
      
    /** 
     * 向Activity列表中添加Activity对象
     */  
    public static void addActivity(Activity a){  
    	if(activityLists == null){
    		activityLists = new Stack<>();
    	}
    	activityLists.add(a);  
    }  
    
    /** 
     * 获得当前栈顶Activity
     */ 
     public static Activity currentActivity(){
    	 if(activityLists.size() > 0){
             return activityLists.lastElement();
         }
         return null;
     }
  
    /** 
     * 关闭Activity列表中的所有Activity
     */  
    public static void finishActivity(){  
        for (Activity activity : activityLists) {    
            if (null != activity) {    
                activity.finish();    
            }    
        }
        
       //杀死该应用进程  
       android.os.Process.killProcess(android.os.Process.myPid());    
    }  

}