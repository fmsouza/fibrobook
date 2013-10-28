/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fibrobook.viewpager.custom;

import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fibrobook.MainActivity;
import com.fibrobook.model.DaySummary;
import com.fibrobook.model.DaySummaryDAO;
import com.fibrobook.model.Disease;
import com.fibrobook.model.DiseaseDAO;

public class CardFragment extends Fragment {

	private static final String ARG_POSITION = "position";
	public static Dialog ratingDialog;
	public static List<DaySummary> ds;
	public static List<Disease> symphtoms;
	public static DaySummary ads;

	private int position;

	public static CardFragment newInstance(int position) {
		CardFragment f = new CardFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prepareEnvironment();

		DaySummaryDAO dao = new DaySummaryDAO(getActivity());
		ds = dao.getDailySummary(MainActivity.date);
		dao.close();

		DiseaseDAO ddao = new DiseaseDAO(getActivity());
		symphtoms = ddao.getList();
		ddao.close();

		position = getArguments().getInt(ARG_POSITION);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);

		FrameLayout fl = new FrameLayout(getActivity());
		fl.setLayoutParams(params);

		final int margin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
						.getDisplayMetrics());

		LinearLayout l = new LinearLayout(getActivity());
		params.setMargins(margin, margin, margin, margin);
		l.setLayoutParams(params);
		l.setGravity(Gravity.CENTER);
		l.setBackgroundResource(R.drawable.background_card);

		switch (position) {
		case 0:
			symphtomsView(l);
			break;

		case 1:
			dayView(l);
			break;

		case 2:
			plotsView(l);
			break;
		}

		fl.addView(l);
		return fl;
	}

	public void symphtomsView(LinearLayout l) {
		ListView symphtomList = new ListView(getActivity());
		ArrayAdapter<Disease> adapter = new ArrayAdapter<Disease>(
				getActivity(), android.R.layout.simple_list_item_1, symphtoms);
		symphtomList.setAdapter(adapter);
		symphtomList.setClickable(true);

		symphtomList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View v,
					int position, long id) {
				ratingDialog = new Dialog(getActivity(),
						com.fibrobook.R.style.FullHeightDialog);
				ratingDialog
						.setContentView(com.fibrobook.R.layout.rating_dialog);
				ratingDialog.setCancelable(true);
				RatingBar ratingBar = (RatingBar) ratingDialog
						.findViewById(com.fibrobook.R.id.dialog_ratingbar);

				int i = 0;
				boolean exists = false;
				while (i < ds.size()) {
					if (symphtoms.get(position).getId() == ds.get(i)
							.getDisease().getId()) {
						ads = ds.get(i);
						ratingBar.setRating(ads.getIntensity());
						exists = true;
						break;
					}
					i++;
				}
				if (!exists)
					ads = new DaySummary(MainActivity.user, symphtoms
							.get(position), MainActivity.date);

				Button updateButton = (Button) ratingDialog
						.findViewById(com.fibrobook.R.id.rank_dialog_button);
				updateButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						RatingBar ratingBar = (RatingBar) ratingDialog
								.findViewById(com.fibrobook.R.id.dialog_ratingbar);
						ads.setIntensity(ratingBar.getRating());
						DaySummaryDAO dao = new DaySummaryDAO(getActivity());
						dao.save(ads);
						ds = dao.getDailySummary(MainActivity.date);
						dao.close();
						ratingDialog.dismiss();
					}
				});
				ratingDialog.show();
			}

		});

		l.addView(symphtomList);
	}

	public void dayView(LinearLayout l) {
		TextView t = new TextView(getActivity());
		t.setText("Here you'll find your daily events list");
		l.addView(t);

	}

	public void plotsView(LinearLayout l) {
		TextView t = new TextView(getActivity());
		t.setText("Here you'll find the plot options");
		l.addView(t);

	}

	private void prepareEnvironment() {
		DiseaseDAO ddao = new DiseaseDAO(getActivity());
		symphtoms = ddao.getList();
		if (symphtoms.isEmpty())
			ddao.firstRun();
		ddao.close();
	}

}