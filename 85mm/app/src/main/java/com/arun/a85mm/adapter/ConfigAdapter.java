package com.arun.a85mm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arun.a85mm.R;

import java.util.List;

/**
 * Created by wy on 2017/5/12.
 */

public class ConfigAdapter extends BaseListAdapter<String> {

    public ConfigAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_text_config, parent, false);
            holder = new TextHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (TextHolder) convertView.getTag();
        }
        String text = getItem(position);
        holder.text_config.setText(text);
        return convertView;
    }

    public static class TextHolder {
        public TextView text_config;

        public TextHolder(View rootView) {
            this.text_config = (TextView) rootView.findViewById(R.id.text_config);
        }
    }
}
