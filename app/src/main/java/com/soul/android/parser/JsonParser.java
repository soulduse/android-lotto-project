package com.soul.android.parser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.soul.android.data.NumberData;
import com.soul.android.utils.FileManager;
import com.soul.android.utils.SQLiteHelper;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
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
import java.util.Map;

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
	private ObjectMapper objectMapper;
	private List<Map<String,Object>> dataList = null;
	private FileManager fileManager = null;

	public JsonParser(ProgressDialog pDialog, Context context){
		this.pDialog = pDialog;
		this.context = context;
		myDb = SQLiteHelper.getInstance(context);
		objectMapper = new ObjectMapper();
		dataList = new ArrayList<>();
		fileManager = new FileManager(context);
	}

	public void settingData() throws Exception{
		new GetLottoData().execute();
	}

	public class GetLottoData extends AsyncTask<Void, Integer, Void> {
		boolean isInserted;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			myDb.createDatabase();
			myDb.openDataBase();
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
//			myDb.initalize();
			//*
			if(jsonStr != null){
				try{
					int number = objectMapper.readValue(jsonStr, NumberData.class).getDrwNo();
					/*
					// 데이터가 없을 경우 전체 데이터 주입
					// But, Assets Folder에 데이터 db를 미리 넣고 호출하기 때문에 필요없어짐.
					if(myDb.selectAll().size() == 0){
						for(int i=1; i<=number; i++){
							publishProgress(i,number);
							parserJsonStr = makeServiceCall(APPOINT_URL + i);
							dataInput(parserJsonStr);
						}
					}
					//*/

					// SQLite에 현재 추첨 번호가 있는지 확인 후, 없다면 후 DB에 저장된 가장 최신 값부터 현재 추첨번호 까지 저장
					if(!myDb.isSelectData(number)){
						int currentNum = myDb.getMaxDrwNo();
						for(int i=currentNum; i<=number; i++){
							publishProgress(i,number);
							parserJsonStr = makeServiceCall(APPOINT_URL + i);
							dataInput(parserJsonStr);
						}
					}else{
						Log.d(log, "데이터베이스 정보가 최신상태 입니다.");
					}
//					getListPrint(); // 데이터 출력
				}catch (IOException ie){
					ie.printStackTrace();
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
		private boolean dataInput(String jsonVal) throws IOException{
			//insert success - true
			//fail - false
			lottoData = objectMapper.readValue(jsonVal, NumberData.class);
			// 하나의 로또 Map 데이터
			Map<String,Object> m = objectMapper.readValue(jsonVal, new TypeReference<Map<String, Object>>(){});
			dataList.add(m);
			isInserted = myDb.insertData(lottoData);

			return isInserted;
		}

		// ObjectMapper를 활용하여 List에 모든 JSON 데이터를 담고 JSON 화 하였다.
		public void getListPrint() throws IOException {
			String dataAll = objectMapper.writeValueAsString(dataList);
			fileManager.save(dataAll);
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