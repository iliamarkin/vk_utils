package ru.markin.vkutils.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;

public abstract class BaseFragment extends MvpAppCompatFragment implements FragmentContent{

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(getFragmentId(), container, getAttachToRoot());
        initializeView(rootView, savedInstanceState);
        return rootView;
    }

    protected abstract void initializeView(View rootView, Bundle savedInstanceState);
}
