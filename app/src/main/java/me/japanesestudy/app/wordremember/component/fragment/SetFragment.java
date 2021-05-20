package me.japanesestudy.app.wordremember.component.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import me.japanesestudy.app.wordremember.R;
import me.japanesestudy.app.wordremember.frame.presenter.HistoryPresenter;

/**
 * Created by guyu on 2018/1/9.
 */
@ContentView(R.layout.fragment_set)
public class SetFragment extends Fragment {

    Fragment preferenceFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =  x.view().inject(this, inflater, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPreferenceFragment();
    }

    public void initPreferenceFragment() {
        preferenceFragment = new AppPreferenceFragment();
        FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
        beginTransaction.add(R.id.preference_container, preferenceFragment);
        beginTransaction.show(preferenceFragment);
        beginTransaction.commit();
    }
}
