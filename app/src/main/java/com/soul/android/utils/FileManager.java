package com.soul.android.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by sould on 2016-03-07.
 */
public class FileManager implements FilePathDefine{

	private static final String FILE_NAME = "lottodata.json";
	private Context context = null;

	public FileManager(Context context){
		this.context = context;
	}

	// SD_CARD의 영역에 JSON Data를 저장한다.
	public void save(String strData){
		if(strData == null || strData.isEmpty() == true)
			return;

		ObjectOutputStream oos = null;
		FileOutputStream fosJson = null;
		try{
			fosJson = new FileOutputStream(SD_PATH + FILE_NAME);
			fosJson.write(strData.getBytes());
			fosJson.close();
			Log.d("Success !! ", "파일저장완료");
		}catch (FileNotFoundException ffe){	ffe.printStackTrace();
		}catch (IOException ie){ie.printStackTrace();}
	}

	public String load(){
		try{
			FileInputStream fisJson = context.openFileInput(SD_PATH+FILE_NAME);
			byte[] lottoData = new byte[fisJson.available()];

			while(fisJson.read(lottoData) != -1){}

			fisJson.close();

			return new String(lottoData);
		}catch (Exception e){ e.printStackTrace(); }

		return "";
	}

	public boolean delete(){
		boolean fileExist = false;
		File deleteFile = new File(DB_PATH+FILE_NAME);
		deleteFile.delete();
		fileExist = deleteFile.exists();

		return fileExist;
//		context.deleteFile(SD_PATH+FILE_NAME);
	}
}
