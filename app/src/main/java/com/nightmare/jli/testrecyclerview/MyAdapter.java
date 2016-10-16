package com.nightmare.jli.testrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Li on 2016/1/2.
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_TYPE_HEADER=0;
    public static final int ITEM_TYPE_CONTENT=1;
    public static final int ITEM_TYPE_FOOTER=2;

    private final Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<String> mDatas;

    private int mHeaderCount=0;
    private int mFooterCount=1;

    public MyAdapter(Context context) {
        List<String> datas = new ArrayList<>();
        if (datas == null) {
            datas = new ArrayList<>();
        }
        mDatas = datas;
        initData();
        mContext = context;
        mLayoutInflater=LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==ITEM_TYPE_HEADER){
            return new HeaderViewHolder(mLayoutInflater.inflate(R.layout.recyclerview_card_header,parent,false),this);
        }else if(viewType==ITEM_TYPE_FOOTER){
            return new FooterViewHolder(mLayoutInflater.inflate(R.layout.recyclerview_card_footer,parent,false),this);
        }else if(viewType==ITEM_TYPE_CONTENT){
            return new MyViewHolder(mLayoutInflater.inflate(R.layout.recyclerview_card_item,parent,false),this);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderViewHolder){

        }else if(holder instanceof FooterViewHolder){
            ((FooterViewHolder)holder).mTextView.setText("点击加载更多");
        }else if(holder instanceof MyViewHolder){
            ((MyViewHolder)holder).mTextView.setText("Test Item");
        }
    }

    @Override
    public int getItemCount() {
        return mHeaderCount+mDatas.size()+mFooterCount;
    }

    public int getContentCount() {
        return mDatas.size();
    }

    public boolean isHeaderView(int position){
        return mHeaderCount!=0&&position<mHeaderCount;
    }

    public boolean isFooterView(int position){
        return mFooterCount!=0&&position>=(mHeaderCount+getContentCount());
    }

    @Override
    public int getItemViewType(int position) {
        if(isHeaderView(position)){
            return ITEM_TYPE_HEADER;
        }else if(isFooterView(position)){
            return ITEM_TYPE_FOOTER;
        }else {
            return ITEM_TYPE_CONTENT;
        }
    }

    private void initData(){
        for(int i=0;i<10;i++){
            mDatas.add("");
        }
    }

    public void add(int index) {
        String data = "";
        mDatas.add(index, data);
        notifyItemInserted(mHeaderCount+index);
    }

    public void remove(int position) {
        if(position+1<=getContentCount()){
            mDatas.remove(position);
            notifyItemRemoved(mHeaderCount+position);
        }
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mTextView;
        MyAdapter mAdapter;

        MyViewHolder(View view, MyAdapter adapter) {
            super(view);
            mAdapter = adapter;
            mTextView = (TextView) view.findViewById(R.id.text_view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getLayoutPosition() == 2) {
                        mAdapter.remove(getLayoutPosition());
                    } else {
                        mAdapter.add(getLayoutPosition());
                    }
                }
            });
        }

    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder{

        TextView mTextView;

        HeaderViewHolder(View view, MyAdapter adapter) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.text_view);

        }

    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        FooterViewHolder(View view, final MyAdapter adapter) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.text_view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(adapter.mContext, "正在加载更多", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
