package me.japanesestudy.app.wordremember.frame.model;

import org.xutils.ex.DbException;

import java.util.List;

import me.japanesestudy.app.wordremember.datasource.dao.BookDAO;
import me.japanesestudy.app.wordremember.datasource.data.AbstractWordUnit;
import me.japanesestudy.app.wordremember.datasource.entity.BookEntity;
import me.japanesestudy.app.wordremember.datasource.entity.UnitEntity;

/**
 * Created by guyu on 2018/1/10.
 */

public class BookModel extends BaseModel<BookEntity> {
    BookDAO bookDAO = new BookDAO();
    private static BookModel instance = new BookModel();
    public static BookModel getInstance() {
        return instance;
    }
    public List<BookEntity> getAllBookList() {
        setMethod();
        if(isClear()){
            try {
                cache = bookDAO.findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        return cache;
    }
    public BookEntity getBookById(int bookId) {
        try {
            return bookDAO.findById(bookId);
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }
    public AbstractWordUnit injectBookName(AbstractWordUnit abstractWordUnit){
        if(abstractWordUnit instanceof UnitEntity) {
            UnitEntity temp = (UnitEntity) abstractWordUnit;
            try {
                temp.setBookName(bookDAO.findById(temp.getBookId()).getName());
                temp.setNewName(temp.getBookName() + ":" + temp.getName());
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        return abstractWordUnit;
    }
}
