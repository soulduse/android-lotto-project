package com.soul.android.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soul.android.R;
import com.soul.android.data.NumberData;
import com.soul.android.adapter.RecyclerViewAdapter;

import java.util.List;

/**
 * Created by sould on 2016-03-03.
 */
public class OneFragment extends Fragment {

	private RecyclerView rv;
	private RecyclerViewAdapter rvAdapter;
	private List<NumberData> dataList;

	public OneFragment(){}

	public OneFragment(List<NumberData> dataList){
		this.dataList = dataList;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rvAdapter = new RecyclerViewAdapter(getActivity(), dataList);
		rv = (RecyclerView)inflater.inflate(R.layout.fragment_cheese_list, container, false);
		setupRectclerView(rv);
		return rv;
	}

	private void setupRectclerView(RecyclerView recyclerView){
		recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
		recyclerView.setAdapter(rvAdapter);
	}
}
