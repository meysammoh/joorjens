package ir.joorjens.dao.interfaces;

import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.Role;

import java.util.List;

public interface RoleRepository extends Repository<Long, Role> {

    FetchResult<Role> search(long id, String name, boolean like, Boolean block, int max, int offset) throws JoorJensException;

    List<Pair<Long, String>> getAllPairs(long id, String name, boolean like, Boolean block) throws JoorJensException;

    List<Role> findAll() throws JoorJensException;
}