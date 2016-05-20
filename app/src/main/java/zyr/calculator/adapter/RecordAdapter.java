package zyr.calculator.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import zyr.calculator.R;
import zyr.calculator.bean.Record;

/**
 * 查询记录ListView的Adapter
 *
 * @author LTP 2015年11月25日
 */
public class RecordAdapter extends ArrayAdapter<Record> {

    public RecordAdapter(Context context, int resource, List<Record> objects) {
        super(context, resource, objects);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Record record = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.listview_item_bmi_record, null);
            viewHolder = new ViewHolder();
            viewHolder.bodyTextView = (TextView) convertView.findViewById(R.id.textView_body);
            viewHolder.bmiTextView = (TextView) convertView.findViewById(R.id.textView_bmi);
            viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.textView_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.bodyTextView.setText("身   高：" + record.getHeight() + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t体重：" + record.getWeight());
        viewHolder.bmiTextView.setText("BMI值：" + record.getBmi() + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t总评：" + record.getResult());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String date = sdf.format(record.getCurrentMillis());
        viewHolder.dateTextView.setText("查询时间：" + date);
        return convertView;
    }

    class ViewHolder {
        TextView bodyTextView;
        TextView bmiTextView;
        TextView dateTextView;

    }
}
