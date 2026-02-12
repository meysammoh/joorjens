package ir.joorjens.dao.interfaces;

import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.ConfigField;

import java.util.List;

public interface ConfigFieldRepository extends Repository<Long, ConfigField> {

    FetchResult<ConfigField> search(long id, String name, float valueFrom, float valueTo, boolean like, int max, int offset) throws JoorJensException;

    List<Pair<Long, String>> getAllPairs(long id, String name, float valueFrom, float valueTo, boolean like) throws JoorJensException;

    List<ConfigField> findAll() throws JoorJensException;
}