package com.soul.android.utils;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sould on 2016-03-07.
 */
public class FileManager {

	private static final String FILE_NAME = "LottoData.json";
	private Context context = null;

	public FileManager(Context context){
		this.context = context;
	}

	public void save(String strData){
		if(strData == null || strData.isEmpty() == true)
			return;

		FileOutputStream fosJson = null;
		try{
			fosJson = context.openFileOutput(FILE_NAME, context.MODE_PRIVATE);
			fosJson.write(strData.getBytes());
			fosJson.close();
			Log.d("Success !! ", "파일저장완료");
		}catch (FileNotFoundException ffe){	ffe.printStackTrace();
		}catch (IOException ie){ie.printStackTrace();}
	}

	public String load(){
		try{
			FileInputStream fisJson = context.openFileInput(FILE_NAME);
			byte[] lottoData = new byte[fisJson.available()];

			while(fisJson.read(lottoData) != -1){}

			fisJson.close();

			return new String(lottoData);
		}catch (Exception e){ e.printStackTrace(); }

		return "";
	}

	public void delete(){
		context.deleteFile(FILE_NAME);
	}
}
