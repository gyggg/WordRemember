package me.japanesestudy.app.wordremember.datasource.entity;

import android.support.annotation.NonNull;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by guyu on 2018/1/9.
 */
@Table(name = "book")
public class BookEntity implements IBaseEntity, Comparable<BookEntity> {

    @Column(name = "id",isId = true,autoGen = true,property = "NOT NULL")
    private Integer id;
    @Column(name = "name")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object getKey() {
        return getId();
    }

    @Override
    public int compareTo(@NonNull BookEntity bookEntity) {
        return this.getId() - bookEntity.getId();
    }
}
