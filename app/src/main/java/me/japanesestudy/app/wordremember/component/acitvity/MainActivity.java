package me.japanesestudy.app.wordremember.component.acitvity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import me.japanesestudy.app.wordremember.R;
import me.japanesestudy.app.wordremember.component.fragment.HistoryFragment;
import me.japanesestudy.app.wordremember.component.fragment.SetFragment;
import me.japanesestudy.app.wordremember.component.fragment.StudyFragment;
import me.japanesestudy.app.wordremember.component.fragment.TestWordFragment;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    @ViewInject(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;

    Fragment historyFragment;
    Fragment studyFragment;
    Fragment setFragment;
    Fragment testWordFragment;

    final public static int FRAG_STUDY = 0;
    final public static int FRAG_TEST_WORD = 1;
    final public static int FRAG_HISTORY = 2;
    final public static int FRAG_SET = 3;

    private int lastFragment = -1;

    Fragment expandableDraggableWithSectionExampleFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initViews();
        initFragment();
    }

    private void initViews() {
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.
                addItem(new BottomNavigationItem(R.drawable.process,"学习")).
                addItem(new BottomNavigationItem(R.drawable.survey,"测试")).
                addItem(new BottomNavigationItem(R.drawable.history,"历史")).
                addItem(new BottomNavigationItem(R.drawable.set, "设置")).initialise();
        bottomNavigationBar.setTabSelectedListener(this);
    }

    private void initFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.commit();
        switchFragment(FRAG_STUDY);
    }

    private void hideFragment() {
        if(lastFragment != -1) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            Fragment last = fm.findFragmentByTag("" + lastFragment);
            transaction.hide(last);
            transaction.commit();
        }
    }

    private void switchFragment(int tag) {
        hideFragment();
        lastFragment = tag;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment next;
        if(fm.findFragmentByTag(""+tag) == null) {
            next = getFragmentById(tag);
            transaction.add(R.id.main_fragment_pain, next, ""+tag);
        } else {
            next = fm.findFragmentByTag("" + tag);
        }
        transaction.show(next);
        transaction.commit();

    }

    public Fragment getFragmentById(int id) {
        switch(id){
            case FRAG_STUDY:
                return studyFragment != null ? studyFragment : new StudyFragment();
            case FRAG_TEST_WORD:
                return testWordFragment != null ? testWordFragment : new TestWordFragment();
            case FRAG_HISTORY:
                return historyFragment != null ? historyFragment : new HistoryFragment();
            case FRAG_SET:
                return setFragment != null ? setFragment : new SetFragment();
            default:
                return null;
        }
    }

    @Override
    public void onTabSelected(int position) {
        switch(position) {
            case 0:
                switchFragment(FRAG_STUDY);
                break;
            case 1:
                switchFragment(FRAG_TEST_WORD);
                break;
            case 2:
                switchFragment(FRAG_HISTORY);
                break;
            case 3:
                switchFragment(FRAG_SET);
                break;
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

}
