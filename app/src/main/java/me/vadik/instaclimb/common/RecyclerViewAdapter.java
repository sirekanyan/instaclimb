package me.vadik.instaclimb.common;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import me.vadik.instaclimb.R;

/**
 * User: vadik
 * Date: 4/13/16
 */
public abstract class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected List mDataset;

    public void addAll(List myDataset) {
        mDataset.clear();
        mDataset.addAll(myDataset);
        this.notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public RelativeLayout root;
        public TextView firstLine;
        public TextView secondLine;
        public TextView date;
        public TextView permissions;
        public NetworkImageView image;

        public ViewHolder(RelativeLayout v) {
            super(v);
            root = v;
            firstLine = (TextView) v.findViewById(R.id.firstline);
            secondLine = (TextView) v.findViewById(R.id.secondLine);
            date = (TextView) v.findViewById(R.id.date);
            permissions = (TextView) v.findViewById(R.id.permis);
            image = (NetworkImageView) v.findViewById(R.id.cicon);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapter(List myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder((RelativeLayout) v);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}