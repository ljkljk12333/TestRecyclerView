package com.nightmare.jli.testrecyclerview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Li on 2016/1/10.
 */
public class MainPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mInflater;

    /**
     * 分页内容View链表
     */
    private List<View> contents;

    public MainPagerAdapter(Context tempContext){
        mContext=tempContext;
        mInflater=LayoutInflater.from(mContext);
        contents=setPagerContent();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = contents.get(position);
        if (position == 0) {
            final RecyclerView recyclerView_Items =(RecyclerView)view.findViewById(R.id.recyclerView_Drag);
            recyclerView_Items.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView_Items.setItemAnimator(new DefaultItemAnimator());

            final MyRefreshAdapter myAdapter=new MyRefreshAdapter(mContext,recyclerView_Items);


            recyclerView_Items.setAdapter(myAdapter);

            Button button_AddItem=(Button)view.findViewById(R.id.button_AddItem);
            button_AddItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerView_Items.smoothScrollToPosition(0);
                    myAdapter.add(0);
                }
            });
            Button button_RemoveItem=(Button)view.findViewById(R.id.button_RemoveItem);
            button_RemoveItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                if(myAdapter.getContentCount()>0){
                    myAdapter.remove(0);
//                }
                }
            });

        }else if(position==1){
            RecyclerView recyclerView_Items =(RecyclerView)view.findViewById(R.id.recyclerView_Normal);
            recyclerView_Items.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView_Items.setItemAnimator(new DefaultItemAnimator());

            MyAdapter myAdapter=new MyAdapter(mContext);


            recyclerView_Items.setAdapter(myAdapter);

            final SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
            //设置刷新动画变换颜色
            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                    android.R.color.holo_orange_light, android.R.color.holo_red_light);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Toast.makeText(mContext, "正在刷新", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);

                }
            });

        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return contents.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(contents.get(position));
    }

    /**
     * 设置各分页内容
     *
     * @return 设置过布局后的各分页链表
     */
    private List<View> setPagerContent() {
        List<View> contents = new ArrayList<>();

        View view;

        view = mInflater.inflate(R.layout.viewpager_drag, null);
        contents.add(view);

        view = mInflater.inflate(R.layout.viewpager_normal, null);
        contents.add(view);

        return contents;
    }


}
