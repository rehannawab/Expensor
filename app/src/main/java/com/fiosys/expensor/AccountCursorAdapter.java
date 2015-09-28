package com.fiosys.expensor;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.zip.Inflater;

/**
 * Created by root on 21/9/15.
 */
public class AccountCursorAdapter extends /*Resource*/CursorAdapter {

    LayoutInflater mInflater;

    public AccountCursorAdapter(Context c, int layout, Cursor cursor, int flags){

        super(c, cursor, flags);//, layout, cursor, flags);

        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.test, parent, false);

        View buttonBox = v.findViewById(R.id.button_box);
        ((LinearLayout.LayoutParams) buttonBox.getLayoutParams()).bottomMargin = -50;
        buttonBox.setVisibility(View.GONE);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView balance;
        TextView name;

        name = (TextView) view.findViewById(R.id.account_name);

        name.setText(cursor.getString(1));
        balance = (TextView) view.findViewById(R.id.account_balance);

        balance.setText(new BigDecimal(cursor.getDouble(2)).toPlainString());

    }

    //    private int getItemViewType(Cursor cursor) {
//        String type = cursor.getString(cursor.getColumnIndex("type"));
//        if (type.equals("1")) {
//            return 0;
//        } else {
//            return 1;
//        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        Cursor cursor = (Cursor) getItem(position);
//        return getItemViewType(cursor);
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }
//
//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//        ViewHolder holder = (ViewHolder) view.getTag();
//        holder.textView
//                .setText(cursor.getString(cursor.getColumnIndex("body")));
//    }
//
//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//        ViewHolder holder = new ViewHolder();
//        View v = null;
//
//        if (cursor.getString(cursor.getColumnIndex("type")).equals("1")) {
//            v = mInflater.inflate(R.layout.message1, parent, false);
//            holder.textView = (TextView) v
//                    .findViewById(R.id.textView1);
//        } else {
//            v = mInflater.inflate(R.layout.message2, parent, false);
//            holder.textView = (TextView) v
//                    .findViewById(R.id.textView2);
//        }
//
//        v.setTag(holder);
//        return v;
//    }
//
//    public static class ViewHolder {
//        public TextView textView;
//    }
}
