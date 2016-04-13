package me.vadik.instaclimb.routes;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.common.RecyclerViewAdapter;
import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.routes.RouteActivity;
import me.vadik.instaclimb.users.UserActivity;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class RouteUsersAdapter extends RecyclerViewAdapter {
    public RouteUsersAdapter(List<User> myDataset) {
        super(myDataset);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final User user = (User) mDataset.get(position);

        final Context context = holder.root.getContext();

        holder.firstLine.setText(user.getName());
        holder.date.setText(user.getDate());
//        holder.secondLine.setText("");
//        holder.permissions.setText("");
//        holder.image.setImageResource(R.drawable.me);

        holder.image.setDefaultImageResId(R.drawable.blackface);
        if (user.hasPicture()) {
            ImageLoader mImageLoader = VolleySingleton.getInstance(context).getImageLoader();
            holder.image.setImageUrl("https://vadik.me/userpic/" + String.valueOf(user.getId()) + ".jpg", mImageLoader);
        }

//        ((TextView) holder.root.findViewById(R.id.generictext)).setText("юзверь");
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserActivity.class);
                intent.putExtra(UserActivity.ARG_USER_ID, String.valueOf(user.getId()));
                intent.putExtra(UserActivity.ARG_USER_NAME, String.valueOf(user.getName()));
                context.startActivity(intent);
            }
        });
    }
}