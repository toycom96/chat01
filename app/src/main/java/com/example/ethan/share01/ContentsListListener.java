package com.example.ethan.share01;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by ethan on 16. 6. 16..
 */
public class ContentsListListener extends RecyclerView.OnScrollListener {
    StaggeredGridLayoutManager mLayoutManager;
    public int lastVisibleItemPosition = 0;        //화면에 리스트의 마지막 아이템이 보여지는지 체크
    public int visibleThreshold = 0;
    public View view;
    private Context mContext = null;
    MainActivity mActivity;
    private ContentsListObject mContentsList;

    public ContentsListListener(MainActivity activity, StaggeredGridLayoutManager layoutManager) {
        this.mActivity = activity;
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * mLayoutManager.getSpanCount();
        //Toast.makeText(getActivity().getApplicationContext(), "msg msg", Toast.LENGTH_SHORT).show();
        //Toast.makeText(AppCompatActivity(), "msg msg", Toast.LENGTH_SHORT).show();
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            }
            else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int[] lastVisibleItemPositions = mLayoutManager.findLastVisibleItemPositions(null);
        lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        int visibleItemCount = view.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        // same code as before

        if (lastVisibleItemPositions[0] + 2 >= totalItemCount) {
            int offset = this.mActivity.mContentsList.get(totalItemCount -1).getId();

            this.mActivity.mContentsLoader.LoadFromApi(offset,1);
            //this.mActivity.items.get(totalItemCount);
            //Toast.makeText(view.getContext(), "aaaa", 1).show();
        }

    }
}
