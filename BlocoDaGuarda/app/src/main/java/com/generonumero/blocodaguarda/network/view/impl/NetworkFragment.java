package com.generonumero.blocodaguarda.network.view.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.generonumero.blocodaguarda.R;
import com.generonumero.blocodaguarda.network.view.NetworkView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NetworkFragment extends Fragment implements NetworkView {

    @Bind(R.id.bdg_network_recycler)
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.network_frag, null);
        ButterKnife.bind(view);
        return view;
    }
}