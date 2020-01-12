package com.umbrellaapps.urdu.keyboard.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umbrellaapps.urdu.keyboard.R;
import com.umbrellaapps.urdu.keyboard.models.Theme;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    private List<Theme> themesList;
    private Context mContext;
    public MyAdapter(List<Theme> themesList, Context context) {
        super();
        this.themesList = themesList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return themesList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        ViewHolder holder = null;
        Theme theme = themesList.get(i);
        if (view == null) {
            view = View.inflate(mContext, R.layout.custom_list, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) view.findViewById(R.id.image);
            holder.overlay = (RelativeLayout) view.findViewById(R.id.overlay);
            holder.selected = (ImageView) view.findViewById(R.id.selected);
            view.setTag(holder);
        }
        holder = (ViewHolder) view.getTag();
        ImageLoader.getInstance().displayImage("drawable://" + theme.getImageId(), holder.imageView);

                holder.imageView.setImageResource(theme.getImageId());
        if (theme.isSelected()) {
            holder.overlay.setVisibility(View.VISIBLE);
        } else {
            holder.overlay.setVisibility(View.INVISIBLE);
        }
        return view;
    }
    public class ViewHolder {
        ImageView imageView;
        ImageView selected;
        RelativeLayout overlay;
    }
}