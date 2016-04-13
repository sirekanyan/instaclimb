package me.vadik.instaclimb.users;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
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

        final Route route = (Route) mDataset.get(position);

        final Context context = holder.root.getContext();

        holder.firstLine.setText(route.getName());
        holder.date.setText(route.getDate());
//        holder.secondLine.setText("");
//        holder.permissions.setText("");
//        holder.image.setImageResource(R.drawable.me);
//        ((TextView) holder.root.findViewById(R.id.generictext)).setText("трасса");

        setMarkerColor(context, holder.root.findViewById(R.id.marker1), route.getColor1(), R.drawable.rect_dashed);
        setMarkerColor(context, holder.root.findViewById(R.id.marker2), route.getColor2(), R.drawable.rect_invisible);
        setMarkerColor(context, holder.root.findViewById(R.id.marker3), route.getColor3(), R.drawable.rect_invisible);

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RouteActivity.class);
                intent.putExtra(RouteActivity.ARG_ROUTE_ID, String.valueOf(route.getId()));
                intent.putExtra(RouteActivity.ARG_ROUTE_NAME, String.valueOf(route.getName()));
                context.startActivity(intent);
            }
        });
    }

    private void setMarkerColor(Context context, View view, int color, int defaultResId) {
        TypedArray colors = context.getResources().obtainTypedArray(R.array.colors);
        if (color == 0) {
            view.setBackgroundResource(defaultResId);
        } else {
            view.setBackgroundResource(colors.getResourceId(color, 0));
        }
        colors.recycle();
    }
}