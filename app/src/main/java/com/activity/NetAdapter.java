package com.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activity.greendao.SuiDao;
import com.frpc.R;

import java.util.List;

public class NetAdapter extends RecyclerView.Adapter {

    private List<SuiDao> mList;
    private ClickEventListener listener;

    public void setData(List<SuiDao> list) {
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_suidao, parent, false);
        return new TileHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        SuiDao suiDao = mList.get(position);
        if (holder instanceof TileHolder) {
            ((TileHolder) holder).nameView.setText(suiDao.getTime());
            ((TileHolder) holder).timeView.setText(suiDao.getName() + "  " + suiDao.getUser() + ":" + suiDao.getLink());
            ((TileHolder) holder).infoView.setText(suiDao.getIp());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    public void setClickEvent(ClickEventListener listener) {
        this.listener = listener;
    }

    public interface ClickEventListener{

        void onClick(int position);

    }

    private static class TileHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        TextView timeView;
        TextView infoView;

        public TileHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.name);
            timeView = itemView.findViewById(R.id.time);
            infoView = itemView.findViewById(R.id.info);
        }
    }
}
