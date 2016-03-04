package com.soul.android.parser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.soul.android.data.NumberData;
import com.soul.android.utils.SQLiteHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sould on 2016-03-03.
 */
public class JsonParser implements ParserDefine{
	private String         	jsonStr = null;
	private String          log 	= getClass().getSimpleName();
	private Context 		context = null;
	private ProgressDialog 	pDialog = null;
	private NumberData 		lottoData;
	private SQLiteHelper	myDb;

	public JsonParser(ProgressDialog pDialog, Context context){
		this.pDialog = pDialog;
		this.context = context;
		myDb = SQLiteHelper.getInstance(context);
	}

	public void settingData() throws Exception{
		new GetLottoData().execute();
	}

	public class GetLottoData extends AsyncTask<Void, Integer, Void> {
		boolean isInserted;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			pDialog.setMessage("데이터베이스 저장중 ... " + values[0] + "/" + values[1]);
		}

		@Override
		protected Void doInBackground(Void... params) {
			String jsonStr = makeServiceCall(BASE_URL);
			String parserJsonStr = null;
			Log.d(log, "jsonData >> " + jsonStr);
//			myDb.deleteRecord(); 	// SQLite Data Delete
			//*
			if(jsonStr != null){
				try{
					JSONObject jsonObj   = new JSONObject(jsonStr);
					int number = jsonObj.getInt(LOTTODATE.DRWNO);
					if(myDb.selectAll().size() == 0){	// 데이터가 없을 경우 전체 데이터 주입
						for(int i=1; i<=number; i++){
							publishProgress(i,number);
							parserJsonStr = null;
							parserJsonStr = makeServiceCall(APPOINT_URL+i);
							dataInput(parserJsonStr);
						}
					}else{
						if(!myDb.isSelectData(number)){
							dataInput(jsonStr);
						}else{
//							Toast.makeText(context, "데이터베이스 정보가 최신상태 입니다.", Toast.LENGTH_LONG).show();
							Log.d(log, "데이터베이스 정보가 최신상태 입니다.");
						}
					}
				}catch (JSONException je){
					je.printStackTrace();
				}
			}
			//*/
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			pDialog.setMessage("데이터베이스 저장 완료.");
			if (pDialog.isShowing())
				pDialog.dismiss();
		}

		// JSON DATA DB INSERT
		private void dataInput(String url) throws JSONException{
			JSONObject insertObj   = new JSONObject(url);
			lottoData = new NumberData();
			lottoData.setBnusNo(insertObj.getInt(LOTTODATE.BNUSNO));
			lottoData.setFirstWinamnt(insertObj.getLong(LOTTODATE.FIRSTWINAMNT));
			lottoData.setTotSellamnt(insertObj.getLong(LOTTODATE.TOTSELLAMNT));
			lottoData.setDrwtNo1(insertObj.getInt(LOTTODATE.DRWTNO1));
			lottoData.setDrwtNo2(insertObj.getInt(LOTTODATE.DRWTNO2));
			lottoData.setDrwtNo3(insertObj.getInt(LOTTODATE.DRWTNO3));
			lottoData.setDrwtNo4(insertObj.getInt(LOTTODATE.DRWTNO4));
			lottoData.setDrwtNo5(insertObj.getInt(LOTTODATE.DRWTNO5));
			lottoData.setDrwtNo6(insertObj.getInt(LOTTODATE.DRWTNO6));
			lottoData.setDrwNo(insertObj.getInt(LOTTODATE.DRWNO));
			lottoData.setFirstPrzwnerCo(insertObj.getInt(LOTTODATE.FIRSTPRZWNERCO));
			lottoData.setDrwNoDate(insertObj.getString(LOTTODATE.DRWNODATE));
			isInserted = myDb.insertData(lottoData);
		}

		// URL에서 JSON DATA 추출 메서드
		private String makeServiceCall(String urlString){
			StringBuffer sb = new StringBuffer("");
			HttpURLConnection connection = null;
			try{
				URL url = new URL(urlString);
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestProperty("Accept-Language","UTF-8");
				connection.setRequestMethod("GET");
				connection.connect();

				InputStream inputStream = connection.getInputStream();
				BufferedReader buffRead = new BufferedReader(new InputStreamReader(inputStream));

				String line = "";
				while((line = buffRead.readLine())!= null){
					sb.append(line);
				}
			}
			catch (UnsupportedEncodingException e){
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}catch (IOException e){
				e.printStackTrace();
			}finally{
				connection.disconnect();
			}
			return sb.toString();
		}
	}
}