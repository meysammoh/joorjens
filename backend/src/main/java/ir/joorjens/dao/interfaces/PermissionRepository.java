package ir.joorjens.dao.interfaces;

import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.Permission;

import java.util.List;

public interface PermissionRepository extends Repository<Long, Permission> {
    Permission getByUrl(String url) throws JoorJensException;

    FetchResult<Permission> search(long id, String url, int max, int offset) throws JoorJensException;

    List<Pair<Long, String>> getAllPairs(long id, String url) throws JoorJensException;

    List<Permission> findAll() throws JoorJensException;
}