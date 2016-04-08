package me.vadik.instaclimb.routes;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import me.vadik.instaclimb.R;

/**
 * User: vadik
 * Date: 4/5/16
 */
public class FilterDialog extends DialogFragment {

    private OnFilterPickedListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFilterPickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFilterPickedListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select grade").setItems(
                R.array.grades, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onFilterPicked(which);
                    }
                });
        return builder.create();
    }

    public interface OnFilterPickedListener {

        void onFilterPicked(int which);
    }
}
