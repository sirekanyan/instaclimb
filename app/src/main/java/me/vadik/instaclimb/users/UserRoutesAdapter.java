package me.vadik.instaclimb.users;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.common.RecyclerViewAdapter;
import me.vadik.instaclimb.model.Route;
import me.vadik.instaclimb.routes.RouteActivity;
import me.vadik.instaclimb.routes.RouteHelper;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class UserRoutesAdapter extends RecyclerViewAdapter {
    public UserRoutesAdapter() {
        super(new ArrayList<Route>());
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        ViewHolder holder = (ViewHolder) viewHolder;

        final Route route = (Route) mDataset.get(position);

        final Context context = holder.root.getContext();

        holder.firstLine.setText(route.getName());
        holder.date.setText(route.getDate());
        holder.secondLine.setText(route.isFlash() ? "flash" : "");
//        holder.permissions.setText("");

        RouteHelper.setMarkerColor(holder.root.findViewById(R.id.marker1), route.getColor1(), R.drawable.rect_dashed);
        RouteHelper.setMarkerColor(holder.root.findViewById(R.id.marker2), route.getColor2());
        RouteHelper.setMarkerColor(holder.root.findViewById(R.id.marker3), route.getColor3());

        holder.permissions.setText(route.getGrade());

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RouteActivity.class);
                intent.putExtra(RouteActivity.ARG_ROUTE_ID, route.getId());
                intent.putExtra(RouteActivity.ARG_ROUTE_NAME, route.getName());
                context.startActivity(intent);
            }
        });
    }
}