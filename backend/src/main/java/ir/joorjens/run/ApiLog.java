package ir.joorjens.run;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import ir.joorjens.aaa.AAA;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.controller.AbstractController;
import ir.joorjens.jmx.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

abstract class ApiLog {

    private static final Logger logger = LoggerFactory.getLogger(ApiLog.class);
    //---------------------------------------------------------------------------------------------------
    private static final Timer TIMER_API = Config.METRICS.timer(MetricRegistry.name(ApiLog.class, "responseTime"));
    private static final Histogram HISTOGRAM_REQUEST = Config.METRICS.histogram(MetricRegistry.name(ApiLog.class, "requestSize"));
    private static final Histogram HISTOGRAM_RESPONSE = Config.METRICS.histogram(MetricRegistry.name(ApiLog.class, "responseSize"));
    //---------------------------------------------------------------------------------------------------
    private static final LoadingCache<Request, Timer.Context> REQUEST = CacheBuilder.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build(new CacheLoader<Request, Timer.Context>() {
                @Override
                public Timer.Context load(Request id) {
                    return null;
                }
            });
    //---------------------------------------------------------------------------------------------------

    static void beforeApiStarted(final Request request, final Response response) throws JoorJensException {
        if (log(request, request.body(), true)) {
            AbstractController.checkPermission(request);
        }
    }

    static void afterApiFinished(final Request request, final Response response) {
        response.header("Content-Encoding", "gzip");
        response.header("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.header("Access-Control-Max-Age", "1000");
        response.header("Access-Control-Allow-Headers",
                "origin, x-csrftoken, content-util, accept, withCredentials, Content-Type");
        response.header("Access-Control-Allow-Credentials", "true");
        if (request.headers("Origin") != null) {
            response.header("Access-Control-Allow-Origin", request.headers("Origin"));
        }
        if (response.type() == null) {
            response.type("application/json; charset=utf-8");
        }
        log(request, response.body(), false);

    }

    static void exception(final Exception e, final Request request, final Response response) {
        final ResponseMessage rm;
        final String message;
        if (e instanceof JoorJensException) {
            rm = ((JoorJensException) e).getResponseMessage();
            message = rm.getMessage();
        } else {
            rm = new ResponseMessage(ExceptionCode.EXCEPTION);
            message = e.getMessage();
        }
        final String messageLog = String.format("Exception on request %s from %d-%s. Message: %s, Url: %s"
                , request.requestMethod(), AAA.getCustomerIdForLog(request), AbstractController.getIp(request)
                , message, Utility.getRequestInfo(request, true));
        if (ExceptionCode.EXCEPTION.getHttpCode() != rm.getResponseCode()) {
            logger.debug(messageLog);
        } else {
            final String MessageError = String.format(", StackTrace: %s, Body: %s", Arrays.toString(e.getStackTrace()), request.body());
            logger.error(messageLog + MessageError);
        }
        response.status(rm.getResponseCode());
        response.body(Utility.toJson(rm));
        ApiLog.afterApiFinished(request, response);
    }

    private static boolean log(final Request request, final String body, final boolean startOrFinish) {
        final boolean logged = !request.requestMethod().equalsIgnoreCase("options");
        if (logged) {
            final int bodyLength;
            if (body != null) {
                bodyLength = body.getBytes().length;
            } else {
                bodyLength = 0;
            }
            final String message = String.format("%s request %s from %d-%s : %s with %d B"
                    , ((startOrFinish) ? "Started" : "Finished"), request.requestMethod()
                    , AAA.getCustomerIdForLog(request), AbstractController.getIp(request)
                    , Utility.getRequestInfo(request, false), bodyLength);
            if (startOrFinish) {
                HISTOGRAM_REQUEST.update(bodyLength);
                REQUEST.put(request, TIMER_API.time());
                logger.debug(message);
            } else {
                HISTOGRAM_RESPONSE.update(bodyLength);
                logger.info(message + String.format(" in %.3f ms", stopContext(request)));
            }
        }
        return logged;
    }

    private static float stopContext(final Request request) {
        final float nano;
        final Timer.Context time = REQUEST.getIfPresent(request);
        if(time != null) {
            REQUEST.invalidate(request);
            nano = (time.stop() / 1000000.0f);
        } else {
            nano = -1;
        }
        return nano;
    }
}