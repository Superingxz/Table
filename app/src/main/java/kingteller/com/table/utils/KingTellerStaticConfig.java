package kingteller.com.table.utils;

import android.os.Environment;

/**
 * 通用参数设置
 * @author Administrator
 *
 */
public class KingTellerStaticConfig {
	

	
	//默认图片宽度
	public static final int DefaultImgMaxWidth =600;
	//默认图片高度
	public static final int DefaultImgMaxHeight =600;

//	public static final int NUM_PAGE = 10;
//	public static final String WATIDOFILE = "waitdo";
//	


	/**数据存储 参数配置 */
    public final static class SHARED_PREFERENCES {
        public final static String USER = "user";

        public final static String COOKIE = "cookie";

        public final static String CONFIG = "config";

        public final static String ADDRESS = "address";

        public final static String DEFAULTUSER = "defaultuser";
    }

	/**缓存文件夹路径 参数配置 */
    public final static class CACHE_PATH {

        public final static String SD_DATA = Environment
                .getExternalStorageDirectory().getAbsolutePath()
                + "/kingteller/data";
        //文件存储位置
        public final static String SD_DOWNLOAD = Environment
                .getExternalStorageDirectory().getAbsolutePath()
                + "/kingteller/download";
        //日志存储位置
        public final static String SD_LOG = Environment
                .getExternalStorageDirectory().getAbsolutePath()
                + "/kingteller/log";
        //图片存储位置
        public final static String SD_KTIMAGE = Environment
                .getExternalStorageDirectory().getAbsolutePath()
                + "/kingteller/ktimage";
        //图片缓存位置
        public final static String SD_IMAGECACHE = Environment
                .getExternalStorageDirectory().getAbsolutePath()
                + "/kingteller/imagecache";
    }

    public static String address; //蓝牙地址
	/** 获取屏幕尺寸 */
	public final static class SCREEN {
		public static int Width = 0;
		public static int Height = 0;
	}

}
