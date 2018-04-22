package com.intel.tvpresent.ui.content;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hanks.htextview.line.LineTextView;
import com.intel.tvpresent.R;
import com.intel.tvpresent.data.model.GameLevel;
import com.intel.tvpresent.data.model.UserWrapper;
import com.intel.tvpresent.ui.custom.FocusedTrue4TV;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by henryalps on 2017/7/31.
 */

public class ContentRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private GameLevel gameLevel;
    private UserWrapper[] userWrappers;
    private Drawable mDefaultCardImage;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private int mSelectdPos = 0;

    public ContentRecycleViewAdapter(GameLevel gameLevel, List<UserWrapper> userWrappers) {
        UserWrapper[] converter = new UserWrapper[userWrappers.size()];
        this.userWrappers = userWrappers.toArray(converter);
        this.gameLevel = gameLevel;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (TYPE_HEADER == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_header,parent,false);
            return new Header(view);
        } else {
            mDefaultCardImage = ContextCompat.getDrawable(parent.getContext(), R.drawable.default_avatar);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user,parent,false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.mName.setText(userWrappers[position - 1].getNickName());
            holder.mOrder.setText(String.format("第%s名", String.valueOf(userWrappers[position - 1].getRank())));
            holder.mScore.setText(String.valueOf(userWrappers[position - 1].getPlayRecordWrapper().getScore()));
            Glide.with(holder.itemView.getContext())
                    .load(userWrappers[position - 1].getAvatarUrl())
                    .error(mDefaultCardImage)
                    .into(holder.mPhoto);
            holder.mBackground.setSelected(mSelectdPos == position);
            if (mSelectdPos == position) {
                holder.mBackground.callOnClick();
                holder.mName.setTextColor(Color.BLACK);
                holder.mOrder.setTextColor(Color.BLACK);
                holder.mScore.setTextColor(Color.YELLOW);
            } else {
                holder.mName.setTextColor(Color.WHITE);
                holder.mOrder.setTextColor(Color.WHITE);
                holder.mScore.setTextColor(Color.WHITE);
            }
        } else {
            Header holder = (Header) viewHolder;
            holder.title.animateText(gameLevel.getName());
        }
    }

    @Override
    public int getItemCount() {
        return userWrappers.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }


    private boolean isPositionHeader(int position)
    {
        return position == 0;
    }

    public int getmSelectdPos() {
        return mSelectdPos;
    }

    public void setmSelectdPos(int mSelectdPos) {
        this.mSelectdPos = mSelectdPos;
        notifyDataSetChanged();
    }

    public UserWrapper getSelectedUserWrapper() {
        return userWrappers[mSelectdPos - 1];
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.user_photo)
        CircleImageView mPhoto;

        @Bind(R.id.user_order)
        TextView mOrder;

        @Bind(R.id.user_name)
        FocusedTrue4TV mName;

        @Bind(R.id.background)
        View mBackground;

        @Bind(R.id.score)
        TextView mScore;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class Header extends RecyclerView.ViewHolder {
        @Bind(R.id.title)
        LineTextView title;

        public Header(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
