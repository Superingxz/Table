package kingteller.com.table.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import kingteller.com.table.R;

/**
 * Created by Administrator on 17-3-28.
 */

public class PreviewAdapter extends BaseAdapter {
    private List<Bitmap> bitmaps;
    private LayoutInflater inflater;
    private Context context;

    public PreviewAdapter(List<Bitmap> bitmaps, Context context) {
        this.bitmaps = bitmaps;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return bitmaps.size();
    }

    @Override
    public Object getItem(int position) {
        return bitmaps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_image, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (bitmaps != null && bitmaps.size() > 0) {
            holder.image.setImageBitmap(bitmaps.get(position));
        }
        return convertView;
    }

    class ViewHolder{
        ImageView image;
    }
}
