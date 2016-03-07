package com.soul.android.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.soul.android.data.NumberData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sould on 2016-03-03.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	private Context context;
	public static SQLiteHelper sqLiteHelper = null;
	public static final String DATABASE_LOCATION = "data/data/com.soul.android/databases/";
	public static final String DATABASE_NAME = "NumberData.db";
	public static final String TABLE_NAME = "Lotto_table";
	public static final int DB_VERSION = 1;
	public static final String IDX 	 = "IDX";
	public static final String COL_0 = "DRWNO";
	public static final String COL_1 = "DRWTNO1";
	public static final String COL_2 = "DRWTNO2";
	public static final String COL_3 = "DRWTNO3";
	public static final String COL_4 = "DRWTNO4";
	public static final String COL_5 = "DRWTNO5";
	public static final String COL_6 = "DRWTNO6";
	public static final String COL_7 = "BNUSNO";
	public static final String COL_8 = "FIRSTPRZWNERCO";
	public static final String COL_9 = "FIRSTWINAMNT";
	public static final String COL_10 = "TOTSELLAMNT";
	public static final String COL_11 = "DRWNODATE";

	private SQLiteDatabase db;

	public static SQLiteHelper getInstance(Context context){
		if(sqLiteHelper == null){
			sqLiteHelper = new SQLiteHelper(context);
		}

		return sqLiteHelper;
	}

	private SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DB_VERSION);
		this.context = context;
		db = this.getWritableDatabase();
		String packageName = context.getPackageName();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_NAME + " ("
				+ IDX + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COL_0 + " INTEGER, "
				+ COL_1 + " INTEGER, "
				+ COL_2 + " INTEGER, "
				+ COL_3 + " INTEGER, "
				+ COL_4 + " INTEGER, "
				+ COL_5 + " INTEGER, "
				+ COL_6 + " INTEGER, "
				+ COL_7 + " INTEGER, "
				+ COL_8 + " INTEGER, "
				+ COL_9 + " INTEGER, "
				+ COL_10 + " INTEGER, "
				+ COL_11 + " TEXT"
				+ ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	public void initalize(){
		File folder = new File(DATABASE_LOCATION);
		if(folder.exists()){}else{folder.mkdirs();}
		AssetManager assetManager = context.getResources().getAssets();
		File outFile = new File(DATABASE_LOCATION+"sample.sqlite");
		InputStream is = null;
		FileOutputStream fo = null;
		long fileSize = 0;
		try{
			is = assetManager.open("sample.sqlite", AssetManager.ACCESS_BUFFER);
			fileSize = is.available();
			if(outFile.length() <= 0){
				byte[] tempdata = new byte[(int)fileSize];
				is.read(tempdata);
				is.close();
				outFile.createNewFile();
				fo = new FileOutputStream(outFile);
				fo.write(tempdata);
				fo.close();
			}else{}
		}catch (IOException ie){
			ie.printStackTrace();
		}
	}

	// 데이터 주입
	public boolean insertData(NumberData numberData){
//		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(COL_0, numberData.getDrwNo());
		contentValues.put(COL_1, numberData.getDrwtNo1());
		contentValues.put(COL_2, numberData.getDrwtNo2());
		contentValues.put(COL_3, numberData.getDrwtNo3());
		contentValues.put(COL_4, numberData.getDrwtNo4());
		contentValues.put(COL_5, numberData.getDrwtNo5());
		contentValues.put(COL_6, numberData.getDrwtNo6());
		contentValues.put(COL_7, numberData.getBnusNo());
		contentValues.put(COL_8, numberData.getFirstPrzwnerCo());
		contentValues.put(COL_9, numberData.getFirstWinamnt());
		contentValues.put(COL_10, numberData.getTotSellamnt());
		contentValues.put(COL_11, numberData.getDrwNoDate());
		long result = db.insert(TABLE_NAME, null, contentValues);
		if(result == -1)
			return false;
		else
			return true;
	}

	// 데이터의 존재 유무 확인
	public boolean isSelectData(int drwno){
		boolean isData = false;
		String sql = "select * from "+TABLE_NAME+" where "+COL_0+" = "+drwno+";";
		Cursor result = db.rawQuery(sql, null);

		// result(Cursor)객체가 비어있으면 false 리턴
		if(result.moveToFirst()){
			isData = true;
		}
		result.close();
		return isData;
	}

	// 원하는 회차 조회
	public NumberData selectData(int drwno){
		NumberData lottoData = null;
		String sql = "select * from "+TABLE_NAME+" where "+COL_0+" = "+drwno+";";
		Cursor result = db.rawQuery(sql, null);

		// result(Cursor)객체가 비어있으면 false 리턴
		if(result.moveToFirst()){
			lottoData = new NumberData();
			lottoData.setDrwNo(result.getInt(1));
			lottoData.setDrwtNo1(result.getInt(2));
			lottoData.setDrwtNo2(result.getInt(3));
			lottoData.setDrwtNo3(result.getInt(4));
			lottoData.setDrwtNo4(result.getInt(5));
			lottoData.setDrwtNo5(result.getInt(6));
			lottoData.setDrwtNo6(result.getInt(7));
			lottoData.setBnusNo(result.getInt(8));
			lottoData.setFirstPrzwnerCo(result.getInt(9));
			lottoData.setFirstWinamnt(result.getLong(10));
			lottoData.setTotSellamnt(result.getLong(11));
			lottoData.setDrwNoDate(result.getString(12));
			result.close();
			return lottoData;
		}
		result.close();
		return lottoData;
	}

	// 전체 데이터 조회
	public List<NumberData> selectAll(){
		List<NumberData> dataResultList = new ArrayList<NumberData>();
		String sql = "select * from "+TABLE_NAME+" ORDER BY "+COL_0+" DESC;";
		Cursor results = db.rawQuery(sql, null);

		if(results.moveToFirst()){
			do{
				NumberData lottoData = new NumberData();
				lottoData.setDrwNo(results.getInt(1));
				lottoData.setDrwtNo1(results.getInt(2));
				lottoData.setDrwtNo2(results.getInt(3));
				lottoData.setDrwtNo3(results.getInt(4));
				lottoData.setDrwtNo4(results.getInt(5));
				lottoData.setDrwtNo5(results.getInt(6));
				lottoData.setDrwtNo6(results.getInt(7));
				lottoData.setBnusNo(results.getInt(8));
				lottoData.setFirstPrzwnerCo(results.getInt(9));
				lottoData.setFirstWinamnt(results.getLong(10));
				lottoData.setTotSellamnt(results.getLong(11));
				lottoData.setDrwNoDate(results.getString(12));
				dataResultList.add(lottoData);
			}while(results.moveToNext());
		}
		return dataResultList;
	}

	// 가장 최신 회차 번호 가져오기.
	public int getMaxDrwNo(){
		String []columns = new String[]{COL_0};
		int maxDrwNo = 0;
		Cursor result = db.query(TABLE_NAME, columns, null, null, null, null, COL_0 + " DESC", "1");
		if(result.moveToFirst()){
			maxDrwNo = result.getInt(0);
			result.close();
			return maxDrwNo;
		}
		result.close();
		return maxDrwNo;
	}

	// DELETE
	public int deleteRecord(){
		int deleteRecordCnt = db.delete(TABLE_NAME, null, null);

		return deleteRecordCnt;
	}
}















