package ru.markin.vkutils.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpDialogFragment;


public abstract class BaseDialogFragment extends MvpDialogFragment implements FragmentContent {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getFragmentId(), container, getAttachToRoot());
        initializeView(rootView, savedInstanceState);
        return rootView;
    }

    protected abstract void initializeView(View rootView, Bundle savedInstanceState);
}
