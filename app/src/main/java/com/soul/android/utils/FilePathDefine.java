package com.soul.android.utils;

import android.os.Environment;

/**
 * Created by sould on 2016-03-07.
 */
public interface FilePathDefine {
	public static final String DB_PATH   = "/data/data/com.soul.android/databases/";
	public static final String FILE_PATH = "/data/data/com.soul.android/files/";
	public static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/lottoapp/";
}
