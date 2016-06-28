package com.seniorzhai.gank;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.seniorzhai.gank.model.BenefitModel;
import com.seniorzhai.gank.network.GankApi;
import com.seniorzhai.gank.weiget.OnRcvScrollListener;
import com.seniorzhai.gank.weiget.RatioImageView;
import com.seniorzhai.gank.weiget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private boolean isLoad;
    private int mPage;
    private GankApi mApi;
    private List<BenefitModel.Benefit> mData;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApi = new GankApi();
        mData = new ArrayList<>();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recylerView);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new ImageAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(16));
        mSwipeRefreshLayout.setRefreshing(true);
        refresh();
        mRecyclerView.addOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onBottom() {
                loadMore();
            }
        });
    }

    private void refresh() {
        load(1);
    }

    private void loadMore() {
        if (isLoad) {
            return;
        }
        load(++mPage);
    }

    private void load(int page) {
        isLoad = true;
        mApi.getBenefit(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<BenefitModel>() {
            @Override
            public void call(BenefitModel benefitModels) {
                isLoad = false;
                mSwipeRefreshLayout.setRefreshing(false);
                if (!benefitModels.error) {
                    mData.addAll(benefitModels.results);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                isLoad = false;
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    private class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {
        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ImageHolder(getLayoutInflater().inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(final ImageHolder holder, int position) {
            Glide.with(holder.itemView.getContext())
                    .load(mData.get(position).url)
                    .centerCrop()
                    .crossFade()
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public void onViewDetachedFromWindow(ImageHolder holder) {
            Glide.clear(holder.imageView);
        }

        class ImageHolder extends RecyclerView.ViewHolder {

            RatioImageView imageView;

            ImageHolder(View itemView) {
                super(itemView);
                imageView = (RatioImageView) itemView;
                imageView.setOriginalSize(50, 50);
            }
        }
    }
}
