package com.example.citydetective.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.citydetective.R;
import com.example.citydetective.webservice.DatabaseHandler;
import com.example.citydetective.webservice.ServerMessage;

public class ServerMessages extends Fragment {
	public static ListView lv;
	DatabaseHandler db;
	TextView tv;
	ArrayList<ServerMessage> lst;
	public ServerMessages(){}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.fragment_servermessages, container, false);
		db = new DatabaseHandler(getActivity());
//		db.resetTablesPN();
		try{
			lst = db.getMessageDetails();
			tv = (TextView)rootView.findViewById(R.id.textView1);  
			
	        lv = (ListView)rootView.findViewById(R.id.listView1);
	        lv.setAdapter(new CustomListAdapter(lst,
					getActivity()));

		}catch(Exception x){
			Toast.makeText(getActivity(), "Error!: "+ x.getMessage(), Toast.LENGTH_LONG).show();
		}
        return rootView;
    }
}
