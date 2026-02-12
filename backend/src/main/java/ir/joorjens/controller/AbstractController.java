package ir.joorjens.controller;

import ir.joorjens.aaa.AAA;
import ir.joorjens.background.LogTask;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseCode;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.model.entity.Log;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.util.TypeEnumeration;
import org.eclipse.jetty.util.HostMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);

    //-----------------------------------------------------------------------------------------
    public static void checkPermission(Request req) throws JoorJensException {
        if (!AAA.hasPermission(req)) {
            throw new JoorJensException(ExceptionCode.OP_FORBIDDEN.getMessage(), ExceptionCode.OP_FORBIDDEN);
        }
    }
    //-----------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public static ResponseMessage delete(Request req, Class clazz, long id) throws JoorJensException {
        final long customerId = AAA.getCustomerIdForLog(req);
        final String className = clazz.getSimpleName();
        final RepositoryImpAbstract repository = RepositoryManager.getByEntity(clazz);
        final Optional optional = repository.getByKey(id);
        final AbstractModel model;
        if (!optional.isPresent()) {
            throw new JoorJensException(String.format(ExceptionCode.NOT_FOUND_ID.getMessage(), id), ExceptionCode.NOT_FOUND_ID);
        } else {
            model = (AbstractModel) optional.get();
            repository.delete(model);
        }

        LogTask.addLog(new Log(customerId, TypeEnumeration.ACTION_DELETE.getId(), TypeEnumeration.ACTION_STATUS_OK.getId()
                , className, String.format("id=%d, url=%s", id, Utility.getRequestInfo(req, true))
                , Utility.toJson(optional.get())));
        LOGGER.info(String.format("DELETE(%d) request was done: %s", id, Utility.getRequestInfo(req, false)));
        return new ResponseMessage(String.format(ResponseCode.DONE_DELETE.getMessage(), model.getEntityName()));
    }

    @SuppressWarnings("unchecked")
    public static AbstractModel upsert(Request req, Class clazz, AbstractModel entity, boolean update) throws JoorJensException {
        entity.isValid();
        final long customerId = AAA.getCustomerIdForLog(req);
        final String className = clazz.getSimpleName();
        final RepositoryImpAbstract repository = RepositoryManager.getByEntity(clazz);
        final TypeEnumeration action;
        if (update && entity.getId() > 0) {
            action = TypeEnumeration.ACTION_UPDATE;
            repository.update(entity);
        } else if (!update && entity.getId() == 0) {
            action = TypeEnumeration.ACTION_INSERT;
            repository.persist(entity);
        } else {
            throw new JoorJensException(ExceptionCode.OP_FORBIDDEN);
        }

        LogTask.addLog(new Log(customerId, action.getId(), TypeEnumeration.ACTION_STATUS_OK.getId()
                , className, String.format("id=%d, url=%s", entity.getId(), Utility.getRequestInfo(req, true))
                , Utility.toJson(entity)));
        LOGGER.info(String.format("%s(%d) request was done: %s", action.getEn(), entity.getId(), Utility.getRequestInfo(req, false)));
        return entity;
    }

    public static ResponseMessage block(final Request req, final Class clazz, final long id, final boolean block) throws JoorJensException {
        final long customerId = AAA.getCustomerIdForLog(req);
        final String className = clazz.getSimpleName();
        final RepositoryImpAbstract repository = RepositoryManager.getByEntity(clazz);
        if (!repository.block(id, block)) {
            throw new JoorJensException(String.format(ExceptionCode.NOT_FOUND_ID.getMessage(), id), ExceptionCode.NOT_FOUND_ID);
            //throw new JoorJensException(ExceptionCode.OP_FORBIDDEN);
        }
        final TypeEnumeration action;
        final ResponseCode responseCode;
        if (block) {
            action = TypeEnumeration.ACTION_BLOCK;
            responseCode = ResponseCode.DONE_BLOCK;
        } else {
            action = TypeEnumeration.ACTION_UNBLOCK;
            responseCode = ResponseCode.DONE_UNBLOCK;
        }

        LogTask.addLog(new Log(customerId, action.getId(), TypeEnumeration.ACTION_STATUS_OK.getId()
                , className, String.format("id=%d, url=%s", id, Utility.getRequestInfo(req, true))
                , "" + block));
        LOGGER.info(String.format("%s(%d) request was done: %s", action.getEn(), id, Utility.getRequestInfo(req, false)));
        return new ResponseMessage(responseCode);
    }
    //-----------------------------------------------------------------------------------------

    public static String getIp(Request req) {
        String ip = null;
        try {
            ip = req.headers("X-Real-IP");
            if (Utility.isEmpty(ip)) {
                ip = req.headers("X-Forwarded-For");
            }
            if (Utility.isEmpty(ip)) {
                ip = req.ip();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ip;
    }

    public static Map<String, String> getQueryMap(Request req, boolean header) throws JoorJensException {
        Map<String, String> result = new HostMap<>();
        for (String key : req.queryParams()) {
            result.put(key.toLowerCase().trim(), Utility.decode(req.queryParams(key).trim()));
        }
        if (header) {
            req.headers().stream()
                    .filter(key -> req.headers(key) != null)
                    .forEach(key -> result.put(key.toLowerCase().trim(), Utility.decode(req.headers(key).trim())));
        }
        return result;
    }

    static String getQueryString(Request req, Set<String> exceptions) throws JoorJensException {
        final StringBuilder queryString = new StringBuilder();
        int i = 0;
        for (String key : req.queryParams()) {
            if (exceptions != null && exceptions.contains(key)) {
                continue;
            }
            if (++i > 1) {
                queryString.append('&');
            }
            queryString.append(key).append('=').append(req.queryParams(key));
        }
        if (i == 0)
            return null;
        return queryString.toString();
    }

    //-----------------------------------------------------------------------------------------
}