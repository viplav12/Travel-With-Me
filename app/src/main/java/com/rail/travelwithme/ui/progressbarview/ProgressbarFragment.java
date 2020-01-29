package com.rail.travelwithme.ui.progressbarview;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.rail.travelwithme.ProgressBarActivity;
import com.rail.travelwithme.R;

public class ProgressbarFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_progressbar, container, false);

        Intent intent = new Intent(getActivity(), ProgressBarActivity.class);
        startActivity(intent);
        return root;
    }
}