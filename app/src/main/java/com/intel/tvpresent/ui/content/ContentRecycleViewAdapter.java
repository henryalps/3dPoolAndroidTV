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
import com.intel.tvpresent.data.model.User;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by henryalps on 2017/7/31.
 */

public class ContentRecycleViewAdapter extends RecyclerView.Adapter<ContentRecycleViewAdapter.ViewHolder> {
    private User[] users;
    private Drawable mDefaultCardImage;

    public ContentRecycleViewAdapter(List<User> users) {
        User[] converter = new User[users.size()];
        this.users = users.toArray(converter);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mDefaultCardImage = ContextCompat.getDrawable(parent.getContext(), R.drawable.default_avatar);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mName.setText(users[position].name);
        holder.mOrder.setText(String.valueOf(users[position].order));
        Glide.with(holder.itemView.getContext())
                .load(users[position].photoUrl)
                .error(mDefaultCardImage)
                .into(holder.mPhoto);
    }

    @Override
    public int getItemCount() {
        return users.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.user_photo)
        CircleImageView mPhoto;

        @Bind(R.id.user_order)
        TextView mOrder;

        @Bind(R.id.user_name)
        TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
