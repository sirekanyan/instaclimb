package me.vadik.instaclimb.users;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.common.RecyclerViewAdapter;
import me.vadik.instaclimb.model.Route;
import me.vadik.instaclimb.routes.RouteActivity;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class UserRoutesAdapter extends RecyclerViewAdapter {
    public UserRoutesAdapter(List<Route> myDataset) {
        super(myDataset);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Route r = (Route) mDataset.get(position);

        final Context context = holder.root.getContext();

        holder.firstLine.setText(r.getName());
        holder.date.setText(r.getDate());
//        holder.secondLine.setText("");
//        holder.permissions.setText("");
//        holder.image.setImageResource(R.drawable.me);
        ((TextView) holder.root.findViewById(R.id.generictext)).setText("трасса");
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RouteActivity.class);
                intent.putExtra(RouteActivity.ARG_ROUTE_ID, String.valueOf(r.getId()));
                context.startActivity(intent);
            }
        });
    }
}