package me.vadik.instaclimb.routes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.routes.dummy.DummyItem;
import me.vadik.instaclimb.routes.dummy.DummyItemsHelper;

/**
 * User: vadik
 * Date: 4/7/16
 */

public class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final FragmentManager fragmentManager;
    private final List<DummyItem> mValues;
    private final boolean mTwoPane;

    public SimpleItemRecyclerViewAdapter(Context context, FragmentManager fragmentManager, List<DummyItem> items, boolean mTwoPane) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.mValues = items;
        this.mTwoPane = mTwoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.route_list_content, parent, false);
        return new ViewHolder(view);
    }

    private void setMarkerColor(View view, String colorName) {
        Map<String, Integer> colors = DummyItemsHelper.COLORS;
        if (colors.containsKey(colorName)) {
            view.setBackgroundResource(colors.get(colorName));
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        DummyItem item = mValues.get(position);
        holder.mItem = item;
        holder.routeNameView.setText(item.name);
        holder.routeGradeView.setText(item.grade);

        setMarkerColor(holder.marker1View, item.getFirstColor());
        setMarkerColor(holder.marker2View, item.getSecondColor());
        setMarkerColor(holder.marker3View, item.getThirdColor());

        if (item.isArchived() || item.isDraft()) {
            holder.routeNameView.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        } else {
            holder.routeNameView.setTextColor(context.getResources().getColor(android.R.color.black));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(RouteFragment.ARG_ITEM_ID, holder.mItem.id);
                    RouteFragment fragment = new RouteFragment();
                    fragment.setArguments(arguments);
                    fragmentManager.beginTransaction()
                            .replace(R.id.route_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, RouteActivity.class);
                    intent.putExtra(RouteFragment.ARG_ITEM_ID, holder.mItem.id);

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView routeNameView;
        public final TextView routeGradeView;
        private final View marker1View;
        private final View marker2View;
        private final View marker3View;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            routeNameView = (TextView) view.findViewById(R.id.route_name);
            routeGradeView = (TextView) view.findViewById(R.id.route_grade);
            marker1View = view.findViewById(R.id.marker1);
            marker2View = view.findViewById(R.id.marker2);
            marker3View = view.findViewById(R.id.marker3);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + routeNameView.getText() + "'";
        }
    }
}