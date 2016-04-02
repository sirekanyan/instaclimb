package me.vadik.instaclimb.routes.example;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.vadik.instaclimb.routes.R;

public class MyFragment extends Fragment {

    public MyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View a = inflater.inflate(R.layout.fragment_one, container, false);
        TextView textView = (TextView) a.findViewById(R.id.fragment_content);
        textView.setText(getArguments().getString("content"));
        return a;
    }


//    interface FragmentContent {
//        String getText();
//    }
}