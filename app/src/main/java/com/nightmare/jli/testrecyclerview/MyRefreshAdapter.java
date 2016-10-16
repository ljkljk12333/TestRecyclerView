package com.nightmare.jli.testrecyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
public class MyRefreshAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_TYPE_HEADER=0;
    public static final int ITEM_TYPE_CONTENT=1;
    public static final int ITEM_TYPE_FOOTER=2;

    private final Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<String> mDatas;

    private int mHeaderCount=0;
    private int mFooterCount=0;

    RecyclerView mRecyclerView;

    private int mFirstVisibleItemPosition;
    private int mLastVisibleItemPosition;

    public MyRefreshAdapter(Context context,RecyclerView tempRecyclerView) {
        List<String> datas = new ArrayList<>();
        if (datas == null) {
            datas = new ArrayList<>();
        }
        mDatas = datas;
        initData();
        mContext = context;
        mRecyclerView=tempRecyclerView;
        mLayoutInflater=LayoutInflater.from(context);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE: {
                        Log.i(MainActivity.TAG, "onScrollStateChanged: SCROLL_STATE_IDLE");

                        if (isHeaderView(((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition())) {
                            //刷新数据
                            Toast.makeText(mContext, "正在刷新", Toast.LENGTH_SHORT).show();
                            //移除HeaderView
                            if (mHeaderCount > 0) {
                                mHeaderCount = 0;
                                notifyItemRemoved(0);
                            }
                        } else {
                            //移除HeaderView
                            if (mHeaderCount > 0) {
                                mHeaderCount = 0;
                                notifyItemRemoved(0);
                            }
                        }
                        if (isFooterView(((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition())) {
                            //加载更多
                            Toast.makeText(mContext, "正在加载更多", Toast.LENGTH_SHORT).show();
                            //移除FooterView
                            if (mFooterCount > 0) {
                                mFooterCount = 0;
                                notifyItemRemoved(getItemCount());
                            }
                        }else {
                            //移除FooterView
                            if (mFooterCount > 0) {
                                mFooterCount = 0;
                                notifyItemRemoved(getItemCount());
                            }
                        }

                        break;
                    }
                    case RecyclerView.SCROLL_STATE_SETTLING: {
                        Log.i(MainActivity.TAG, "onScrollStateChanged: SCROLL_STATE_SETTLING");

                        break;
                    }
                    case RecyclerView.SCROLL_STATE_DRAGGING: {
                        Log.i(MainActivity.TAG, "onScrollStateChanged: SCROLL_STATE_DRAGGING");

                        if (mFirstVisibleItemPosition == 0) {
                            if(!isHeaderView(mFirstVisibleItemPosition)){
                                //添加HeaderView
                                if (mHeaderCount == 0) {
                                    mHeaderCount = 1;
                                    notifyItemInserted(0);
                                }
                            }
                        }
                        if (mLastVisibleItemPosition + 1 >= getItemCount()) {
                            if(!isFooterView(mLastVisibleItemPosition)){
                                //添加FooterView
                                if (mFooterCount == 0) {
                                    mFooterCount = 1;
                                    notifyItemInserted(getItemCount());
                                }
                            }
                        }

                        break;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i(MainActivity.TAG, "onScrolled");

                mFirstVisibleItemPosition=((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                mLastVisibleItemPosition=((LinearLayoutManager)mRecyclerView.getLayoutManager()).findLastVisibleItemPosition();

                if (mFirstVisibleItemPosition == 0) {
                    if(isHeaderView(mFirstVisibleItemPosition)){
                        int tempFirstCompletelyVisibleItemPosition=((LinearLayoutManager) mRecyclerView.getLayoutManager()).
                                findFirstCompletelyVisibleItemPosition();
                        if (isHeaderView(tempFirstCompletelyVisibleItemPosition)) {
                            Log.i(MainActivity.TAG, "onScrolled: 松开开始刷新");
                            if(!((HeaderViewHolder)mRecyclerView.findViewHolderForLayoutPosition(mFirstVisibleItemPosition))
                                    .mTextView.getText().equals("松开开始刷新")) {
                                ((HeaderViewHolder) mRecyclerView.findViewHolderForLayoutPosition(tempFirstCompletelyVisibleItemPosition))
                                        .mTextView.setText("松开开始刷新");
                            }
                        }else {
                            Log.i(MainActivity.TAG, "onScrolled: 下拉刷新");
                            if(!((HeaderViewHolder)mRecyclerView.findViewHolderForLayoutPosition(mFirstVisibleItemPosition))
                                    .mTextView.getText().equals("下拉刷新")) {
                                ((HeaderViewHolder) mRecyclerView.findViewHolderForLayoutPosition(mFirstVisibleItemPosition))
                                        .mTextView.setText("下拉刷新");
                            }
                        }
                    }
                }
                if (mLastVisibleItemPosition + 1 == getItemCount()) {
                    if(isFooterView(mLastVisibleItemPosition)){
                        int tempLastCompletelyVisibleItemPosition=((LinearLayoutManager) mRecyclerView.getLayoutManager()).
                                findLastCompletelyVisibleItemPosition();

                        if (isFooterView(tempLastCompletelyVisibleItemPosition)) {
                            Log.i(MainActivity.TAG, "onScrolled: 松开开始加载");

                            if(!((FooterViewHolder)mRecyclerView.findViewHolderForLayoutPosition(tempLastCompletelyVisibleItemPosition))
                                    .mTextView.getText().equals("松开开始加载")) {
                                ((FooterViewHolder) mRecyclerView.findViewHolderForLayoutPosition(tempLastCompletelyVisibleItemPosition))
                                        .mTextView.setText("松开开始加载");
                            }
                        }else {
                            Log.i(MainActivity.TAG, "onScrolled: 上拉加载更多");

                            if(!((FooterViewHolder)mRecyclerView.findViewHolderForLayoutPosition(mLastVisibleItemPosition))
                                    .mTextView.getText().equals("上拉加载更多")) {
                                ((FooterViewHolder) mRecyclerView.findViewHolderForLayoutPosition(mLastVisibleItemPosition))
                                        .mTextView.setText("上拉加载更多");
                            }
                        }
                    }
                }
            }
        });
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
        MyRefreshAdapter mAdapter;

        MyViewHolder(View view, MyRefreshAdapter adapter) {
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

        HeaderViewHolder(View view, MyRefreshAdapter adapter) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.text_view);

        }

    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder{

        TextView mTextView;

        FooterViewHolder(View view, MyRefreshAdapter adapter) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.text_view);

        }

    }

}
