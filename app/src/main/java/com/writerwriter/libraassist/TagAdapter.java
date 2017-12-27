package com.writerwriter.libraassist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry on 2017/12/24.
 */

public class TagAdapter extends BaseSwipeAdapter{
    private List<TagUnit> list;
    private Context mContext;

    public TagAdapter(Context mContext, List<TagUnit> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.tag_item;
    }

    //ATTENTION: Never bind listener or fill values in generateView.
    //           You have to do that in fillValues method.
    @Override
    public View generateView(int position, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tag_item, null);
        return view;
    }

    @Override
    public void fillValues(final int position, View convertView) {
        TextView t = (TextView)convertView.findViewById(R.id.tag_text);
        t.setText(list.get(position).getTag_text());
        TextView t2 = (TextView)convertView.findViewById(R.id.tag_pages);
        t2.setText(list.get(position).getTag_pages());
        final SwipeLayout swipeLayout = (SwipeLayout)convertView.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, convertView.findViewById(R.id.bottom_wrapper));
        Button remove_btn = (Button)convertView.findViewById(R.id.remove_btn);
        Button edit_btn = (Button)convertView.findViewById(R.id.edit_btn);
        remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //list.remove(position);
                //notifyDataSetChanged();
                Sub3BorrowFragment.Instance.UpdateBookmark(list.get(position).getTag_text(), "");
                swipeLayout.close();
            }
        });
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(mContext)
                        .title("Edit tag")
                        .positiveText("Apply")
                        .negativeText("Cancel")
                        .inputType(InputType.TYPE_CLASS_NUMBER)
                        .input("Enter pages:", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                //list.get(position).setTag_pages(input.toString());
                                //notifyDataSetChanged();
                                Sub3BorrowFragment.Instance.UpdateBookmark(list.get(position).getTag_text(), input.toString());
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
