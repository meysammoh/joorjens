package ir.joorjens.dao.manager;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Repository<PK, T> {
    void addCommonQuery(StringBuilder query, Map<String, Object> map
            , long id, Boolean block);

    Optional<T> getByKey(PK key) throws JoorJensException;

    T persist(T entity) throws JoorJensException;

    int persistBatch(List<T> entity) throws JoorJensException;

    T update(T entity) throws JoorJensException;

    int updateBatch(List<T> entityList) throws JoorJensException;

    void delete(T entity) throws JoorJensException;

    FetchResult<T> search(String q, Map<String, Object> map, int max, int offset) throws JoorJensException;

    @SuppressWarnings("unchecked")
    FetchResult<T> search(String q, Map<String, Object> map, String orderBy, int max, int offset) throws JoorJensException;

    FetchResult<Object> searchAdvanced(String q, String fields, Map<String, Object> map, String orderBy, int max, int offset) throws JoorJensException;

    boolean block(long id, boolean block) throws JoorJensException;

    int update(String q, Map<String, Object> map) throws JoorJensException;

    List<Object> findAllBy(String q, Map<String, Object> map) throws JoorJensException;

    List<Object> findAllBy(String q, Map<String, Object> map, int max, int offset) throws JoorJensException;

    Object findBy(String q, Map<String, Object> map) throws JoorJensException;

    @SuppressWarnings("unchecked")
    long count(String q, Map<String, Object> map) throws JoorJensException;

    int executeQueries(String... queries) throws JoorJensException;

    int executeNative(String q) throws JoorJensException;
}