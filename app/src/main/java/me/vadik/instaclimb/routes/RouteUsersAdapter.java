package me.vadik.instaclimb.routes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.common.RecyclerViewAdapter;
import me.vadik.instaclimb.model.Route;
import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.users.UserActivity;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class RouteUsersAdapter extends RecyclerViewAdapter {
    private Route route;

    public RouteUsersAdapter() {
        super(new ArrayList<User>());
    }

    public void setRoute(Route route) {
        this.route = route;
        this.notifyDataSetChanged();
    }

    private static class CardViewHolder extends RecyclerView.ViewHolder {

        public TextView routeGrade;
        public TextView routeDate;
        public TextView routeAuthor;
        public View color1;
        public View color2;
        public View color3;
        public TextView numClimbed;

        public CardViewHolder(CardView view) {
            super(view);
            routeGrade = (TextView) view.findViewById(R.id.card_view_route_grade);
            routeDate = (TextView) view.findViewById(R.id.card_view_route_created_when);
            routeAuthor = (TextView) view.findViewById(R.id.card_view_route_author);
            color1 = view.findViewById(R.id.card_view_route_marker1);
            color2 = view.findViewById(R.id.card_view_route_marker2);
            color3 = view.findViewById(R.id.card_view_route_marker3);
            numClimbed = (TextView) view.findViewById(R.id.card_view_route_num_climbed);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {

        if (position == 0) {
            if (route != null) {
                CardViewHolder cardViewHolder = (CardViewHolder) viewHolder;
                cardViewHolder.routeGrade.setText(route.getName() + " / " + route.getGrade());
                cardViewHolder.routeDate.setText(route.getDate());
                cardViewHolder.routeAuthor.setText("Автор: " + route.getAuthor().getName());
                RouteHelper.setMarkerColor(cardViewHolder.color1, route.getColor1(), R.drawable.rect_dashed);
                RouteHelper.setMarkerColor(cardViewHolder.color2, route.getColor2());
                RouteHelper.setMarkerColor(cardViewHolder.color3, route.getColor3());
                int count = super.getItemCount();
                if (count > 0) {
                    String countStr = String.valueOf(count);
                    if (count >= 5 && count <= 20) {
                        cardViewHolder.numClimbed.setText("Трассу пролезли " + countStr + " человек");
                    } else if (countStr.endsWith("1")) {
                        cardViewHolder.numClimbed.setText("Трассу пролез " + countStr + " человек");
                    } else if (countStr.endsWith("2") || countStr.endsWith("3") || countStr.endsWith("4")) {
                        cardViewHolder.numClimbed.setText("Трассу пролезло " + countStr + " человека");
                    } else {
                        cardViewHolder.numClimbed.setText("Трассу пролезли " + countStr + " человек");
                    }
                }
            }
            return;
        }

        ViewHolder holder = (ViewHolder) viewHolder;

        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final User user = (User) mDataset.get(position - 1);

        final Context context = holder.root.getContext();

        holder.firstLine.setText(user.getName());
        holder.date.setText(user.getDate());
//        holder.secondLine.setText("");
//        holder.permissions.setText("");

        String url = null;

        if (user.hasPicture()) {
            url = "https://vadik.me/userpic/" + String.valueOf(user.getId()) + ".jpg";
        }

        ImageLoader mImageLoader = VolleySingleton.getInstance(context).getImageLoader();
        holder.image.setDefaultImageResId(R.drawable.blackface);
        holder.image.setImageUrl(url, mImageLoader);

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserActivity.class);
                intent.putExtra(UserActivity.ARG_USER_ID, user.getId());
                intent.putExtra(UserActivity.ARG_USER_NAME, user.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_card, parent, false);
                return new CardViewHolder((CardView) v);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }
}