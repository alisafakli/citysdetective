package com.example.citydetective.login;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONObject;

import com.example.citydetective.R;
import com.example.citydetective.webservice.ServerMessage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomListAdapter extends BaseAdapter{

	private ArrayList<ServerMessage> _data;
	Context _c;

	public CustomListAdapter (ArrayList<ServerMessage> program_detaylari, Context c){
		_data = program_detaylari;
		_c = c;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	ImageView image;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		if (v == null)
		{
			LayoutInflater vi = (LayoutInflater)_c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_row, null );
		}
		TextView message = (TextView)v.findViewById(R.id.list_message_text);
		TextView message_time = (TextView)v.findViewById(R.id.list_message_time);

		ServerMessage msg = _data.get(position);
		
		String str = msg.getName();
		message.setText(str);
		str = msg.getTime();
		message_time.setText(str);


		return v;	
	}


}
