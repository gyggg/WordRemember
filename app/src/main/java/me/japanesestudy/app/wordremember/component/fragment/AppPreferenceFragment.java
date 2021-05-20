package me.japanesestudy.app.wordremember.component.fragment;

import android.os.Bundle;
import android.support.v7.preference.Preference;

import com.afollestad.materialdialogs.MaterialDialog;

import me.japanesestudy.app.wordremember.R;
import me.japanesestudy.app.wordremember.component.application.AppConfig;
import me.japanesestudy.app.wordremember.frame.presenter.PreferencePresenter;
import me.japanesestudy.app.wordremember.frame.view.IPreferenceView;

/**
 * Created by guyu on 2018/1/25.
 */

public class AppPreferenceFragment extends android.support.v14.preference.PreferenceFragment
        implements IPreferenceView, Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private PreferencePresenter mPresenter;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_preferences);
        mPresenter = new PreferencePresenter(this);
        AppConfig.loadAppPreference(getPreferenceManager().getSharedPreferencesName());
        findPreference(AppConfig.KEY_AUTO_CREATE_HISTORY_NAME).setOnPreferenceChangeListener(this);
        findPreference(AppConfig.KEY_IS_C2J).setOnPreferenceChangeListener(this);
        findPreference(AppConfig.KEY_TEST_HIDE_KANJI).setOnPreferenceChangeListener(this);
        findPreference(AppConfig.KEY_SKIP_TYPES).setOnPreferenceClickListener(this);
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case AppConfig.KEY_AUTO_CREATE_HISTORY_NAME:
                mPresenter.setAutoHistoryName((Boolean) newValue);
                break;
            case AppConfig.KEY_IS_C2J:
                mPresenter.setIsC2J((Boolean) newValue);
                break;
            case AppConfig.KEY_TEST_HIDE_KANJI:
                mPresenter.setHideKanji((Boolean) newValue);
                break;
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case AppConfig.KEY_SKIP_TYPES:
                    mPresenter.startSetSkipType();
                break;
        }
        return true;
    }

    @Override
    public void showSkipTypesDialog(String[] typeString, int[] typeId, Integer[] defType) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this.getActivity());
        builder.title("设置自动跳过的词性")
                .content("选中的词性在测试的过程中不会出现，直接跳过")
                .items(typeString)
                .itemsIds(typeId)
                .itemsCallbackMultiChoice(defType, (dialog, which, text) -> false)
                .negativeText("取消")
                .onNegative((dialog, which) -> {

                })
                .positiveText("确定")
                .onPositive((dialog, which) -> {
                    Integer []selected = dialog.getSelectedIndices();
                    mPresenter.stepSetSkipType(selected);
                });
        builder.show();
    }
}
