package ir.joorjens.common.email;

import java.util.HashMap;
import java.util.Map;

public enum EmailType {

    TEXT(10, "text"), //
    HTML(11, "text/html;"), //
    HTML_UTF8(12, "text/html; charset=utf-8"), //
    OTHER(999, "other");

    private static final Map<Integer, EmailType> MAP = new HashMap<>();

    static {
        for (EmailType et : EmailType.values()) {
            MAP.put(et.getId(), et);
        }
    }

    private final int id;
    private final String name;

    EmailType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public static EmailType get(int id) {
        EmailType et = MAP.get(id);
        if (et == null)
            et = OTHER;
        return et;
    }
}