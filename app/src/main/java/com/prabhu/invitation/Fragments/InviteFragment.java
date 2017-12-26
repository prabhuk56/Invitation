package com.prabhu.invitation.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prabhu.invitation.R;
import com.prabhu.invitation.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class InviteFragment extends BaseFragment {


    @Override @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

}
