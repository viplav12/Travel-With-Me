package com.rail.travelwithme.ui.webui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.rail.travelwithme.R;
import com.rail.travelwithme.UrlWebViewActivity;

public class WebUIFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.web_view,
                container, false);

        Intent intent = new Intent(getActivity(), UrlWebViewActivity.class);
        startActivity(intent);

        return root;
    }
}