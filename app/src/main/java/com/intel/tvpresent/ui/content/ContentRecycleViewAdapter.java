package com.intel.tvpresent.ui.content;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.intel.tvpresent.R;
import com.intel.tvpresent.data.model.GameLevel;
import com.intel.tvpresent.data.model.UserWrapper;

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
    private int mSelectdPos = 1;

    public ContentRecycleViewAdapter(GameLevel gameLevel, List<UserWrapper> userWrappers) {
        UserWrapper[] converter = new UserWrapper[userWrappers.size()];
        this.userWrappers = userWrappers.toArray(converter);
        this.gameLevel = gameLevel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (TYPE_HEADER == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_header,parent,false);
            return new ViewHolder(view);
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
            holder.mName.setText(userWrappers[position].getNickName());
            holder.mOrder.setText(String.valueOf(userWrappers[position].getRank()));
            Glide.with(holder.itemView.getContext())
                    .load(userWrappers[position].getAvatarUrl())
                    .error(mDefaultCardImage)
                    .into(holder.mPhoto);
            holder.mBackground.setSelected(mSelectdPos == position);
        } else {
            Header holder = (Header) viewHolder;
            holder.title.setText(gameLevel.getDesp());
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
        return userWrappers[mSelectdPos];
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.user_photo)
        CircleImageView mPhoto;

        @Bind(R.id.user_order)
        TextView mOrder;

        @Bind(R.id.user_name)
        TextView mName;

        @Bind(R.id.background)
        View mBackground;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class Header extends RecyclerView.ViewHolder {
        @Bind(R.id.title)
        TextView title;

        public Header(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
