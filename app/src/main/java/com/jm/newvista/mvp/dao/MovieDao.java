package com.jm.newvista.mvp.dao;

import android.content.ContentValues;

import com.jm.newvista.bean.MovieEntity;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Johnny on 2/5/2018.
 */

public class MovieDao implements IDao<MovieEntity> {
    @Override
    public boolean save(MovieEntity entity) {
        return entity.save();
    }

    public void saveAll(List<MovieEntity> entities) {
        DataSupport.saveAll(entities);
    }

    @Override
    public int update(MovieEntity entity) {
        return updateById(entity);
    }

    private int updateById(MovieEntity entity) {
        int id = entity.getId();
        return entity.update(id);
    }

    public int updatePosterStrByTitle(MovieEntity entity) {
        ContentValues values = new ContentValues();
        values.put("posterstr", entity.getPosterStr());
        List<MovieEntity> movieEntities = DataSupport.where("title=?", entity.getTitle()).find(MovieEntity.class);
        MovieEntity movieEntity = movieEntities.get(0);
        DataSupport.update(MovieEntity.class, values, movieEntity.getId());
        return 0;
    }

    public List<MovieEntity> queryByGenre(MovieEntity entity) {
        return DataSupport.where("genre LIKE ?", "%" + entity.getGenre() + "%").find(MovieEntity.class);
    }

    public List<MovieEntity> queryByTitle(MovieEntity entity) {
        return DataSupport.where("title LIKE ?", "%" + entity.getTitle() + "%").find(MovieEntity.class);
    }

    @Override
    public MovieEntity queryById(MovieEntity entity) {
        return null;
    }

    @Override
    public int delete(MovieEntity entity) {
        return 0;
    }

    @Override
    public int deleteAll() {
        return DataSupport.deleteAll(MovieEntity.class);
    }

    @Override
    public List<MovieEntity> getAll() {
        return DataSupport.findAll(MovieEntity.class);
    }

    @Override
    public boolean isEmpty() {
        if (DataSupport.count("movieentity") > 0) {
            return false;
        } else {
            return true;
        }
    }

    public int recordCount() {
        return DataSupport.count("movieentity");
    }
}
