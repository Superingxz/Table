package kingteller.com.table.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kingteller.com.table.R;
import kingteller.com.table.bean.TableBean;
import kingteller.com.table.bean.WorkOrderBean;

/**
 * Created by Administrator on 17-3-23.
 */

public class TableAdapter extends BaseAdapter {
    private WorkOrderBean workOrderBean;
    private List<String> workTypes;
    private List<TableBean> table;
    private Context context;
    private LayoutInflater inflater;

    public TableAdapter(WorkOrderBean workOrderBean, List<String> workTypes, Context context) {
        this.workOrderBean = workOrderBean;
        this.context = context;
        this.workTypes = workTypes;
        this.inflater = LayoutInflater.from(context);
    }
    public TableAdapter(WorkOrderBean workOrderBean, Context context) {
        this.workOrderBean = workOrderBean;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public TableAdapter(List<TableBean> table, Context context) {
        this.table = table;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        if (workOrderBean != null && workTypes == null) {
           return 7;
        }else if (workOrderBean != null && workTypes != null && workTypes.size() > 0) {
            return 7 + workTypes.size();
        } else if (table != null) {
            return table.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
       return workOrderBean;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContentViewHolder contentViewHolder = null;
        if (convertView == null) {
            contentViewHolder = new ContentViewHolder();
            convertView = inflater.inflate(R.layout.item_content, parent, false);
            contentViewHolder.title = (TextView) convertView.findViewById(R.id.title);
            contentViewHolder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(contentViewHolder);
        } else {
            contentViewHolder = (ContentViewHolder) convertView.getTag();
        }
        int mPosition = position;
        if (workOrderBean != null) {
            if (mPosition == 0) {
                contentViewHolder.title.setText("服务单号");
                contentViewHolder.content.setText(workOrderBean.getOrderNo());
            }
            if (mPosition == 1) {
                contentViewHolder.title.setText("服务日期");
                contentViewHolder.content.setText("2016-11-2");
            }
            if (mPosition == 2) {
                contentViewHolder.title.setText("客户简称");
                contentViewHolder.content.setText("滨州邮政梁才支局");
            }
            if (mPosition == 3) {
                contentViewHolder.title.setText("ATM号");
                contentViewHolder.content.setText("37005488");
            }
            if (workTypes != null && workTypes.size() > 0) {
                for (int i = 0; i < workTypes.size(); i++) {
                    if (mPosition == 4 + i) {
                        contentViewHolder.title.setText("服务类别" + (i + 1));
                        contentViewHolder.content.setText(workTypes.get(i));
                    }
                }
                if (mPosition == 3 + workTypes.size()) {
                    contentViewHolder.title.setText("是否更换备件");
                    contentViewHolder.content.setText("否");
                }
                if (mPosition == 4 + workTypes.size()) {
                    contentViewHolder.title.setText("服务工程师");
                    contentViewHolder.content.setText("张三/18313011111");
                }
            } else {
                if (mPosition == 4) {
                    contentViewHolder.title.setText("服务类别1");
                    contentViewHolder.content.setText("软件升级");
                }
                if (mPosition == 5) {
                    contentViewHolder.title.setText("是否更换备件");
                    contentViewHolder.content.setText("是/欧姆龙读卡器");
                }
                if (mPosition == 6) {
                    contentViewHolder.title.setText("服务工程师");
                    contentViewHolder.content.setText("张三/18313011111");
                }
            }
        } else if (table != null && table.size() > 0) {
            contentViewHolder.title.setText(table.get(position).getTitle());
            contentViewHolder.content.setText(table.get(position).getContent());
        }
        return convertView;
    }
    class ContentViewHolder{
        TextView title,content;
    }
}
