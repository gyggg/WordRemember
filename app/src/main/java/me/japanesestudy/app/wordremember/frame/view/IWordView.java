package me.japanesestudy.app.wordremember.frame.view;

import java.util.List;

import me.japanesestudy.app.wordremember.datasource.entity.WordEntity;

/**
 * Created by guyu on 2018/1/22.
 */

public interface IWordView {
    void initWordListAdapter(List<WordEntity> wordEntities);
    void onCollected(WordEntity wordEntity, int itemPosition);
}

////View接口
//interface DemoView{
//    //声明用于Presenter与Activity/Fragment通信的方法
//}
//
////Activity类，实现对应的View接口
//class DemoActivity implements DemoView{
//    DemoPresenter demoPresenter = new DemoPresenter(this);
//    //用于操作界面的方法/实现View接口的方法
//}
//
////Presenter基类
//class BasePresenter<T> {
//    T view;
//    public BasePresenter(T view) {
//        this.view = view;
//    }
//    //所有Presenter的通用方法
//}
//
//class DemoPresenter extends BasePresenter<DemoView>{
//    DemoModel demoModel = new DemoModel();
//    public DemoPresenter(DemoView view) {
//        super(view);
//    }
//    //用于进行业务逻辑的方法
//}
//
//class DemoModel{
//    //用于对数据库操作的方法
//}