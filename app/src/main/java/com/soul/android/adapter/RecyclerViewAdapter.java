package com.soul.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soul.android.data.NumberData;
import com.soul.android.designlibdemo.R;

import java.util.List;

/**
 * Created by sould on 2016-03-03.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

	private final TypedValue mTypedValue = new TypedValue();
	private int mBackground;
	private List<NumberData> dataList;

	public RecyclerViewAdapter(Context context, List<NumberData> dataList){
		context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
		mBackground = mTypedValue.resourceId;
		this.dataList = dataList;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.listview_item, parent, false);
		view.setBackgroundResource(mBackground);
		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {

		String numbersStr =
				dataList.get(position).getDrwNo()+"회 : "+
				dataList.get(position).getDrwtNo1()+", "+
				dataList.get(position).getDrwtNo2()+", "+
				dataList.get(position).getDrwtNo3()+", "+
				dataList.get(position).getDrwtNo4()+", "+
				dataList.get(position).getDrwtNo5()+", "+
				dataList.get(position).getDrwtNo6();
		holder.lottoText.setText(numbersStr);
	}

	@Override
	public int getItemCount() {
		return dataList.size();
	}

	public class MyViewHolder extends RecyclerView.ViewHolder{
		public final View mView;
		public final TextView lottoText;

		public MyViewHolder(View itemView) {
			super(itemView);
			mView = itemView;
			lottoText = (TextView)mView.findViewById(R.id.lotto_numbers);
		}

		@Override
		public String toString() {
			return super.toString()+" '"+lottoText.getText();
		}
	}
}
