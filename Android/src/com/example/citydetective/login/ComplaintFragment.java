package com.example.citydetective.login;

import java.util.Random;

import com.example.citydetective.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

public class ComplaintFragment extends Fragment {

	private FragmentTabHost mTabHost;

	public ComplaintFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_find_people,
				container, false);

		mTabHost = (FragmentTabHost) rootView
				.findViewById(android.R.id.tabhost);
		mTabHost.setup(getActivity(), getChildFragmentManager(),
				R.id.realtabcontent);
		
		Random rand = new Random();

		int n = rand.nextInt(3) + 1;

		if (n>=1) {
			mTabHost.addTab(
					mTabHost.newTabSpec("fragmentb").setIndicator("Fragment A"),
					FragmentA.class, null);
		} 
		if (n>=2){
			mTabHost.addTab(
					mTabHost.newTabSpec("fragmentc").setIndicator("Fragment B"),
					FragmentB.class, null);
		}
		if (n>=3) {
			mTabHost.addTab(
					mTabHost.newTabSpec("fragmentd").setIndicator("Fragment C"),
					FragmentC.class, null);
		}

		return rootView;

	}
}
