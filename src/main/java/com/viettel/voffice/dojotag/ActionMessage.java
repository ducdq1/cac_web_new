package com.viettel.voffice.dojotag;

public abstract interface ActionMessage {

    public static final String ERROR = "ERROR";
    public static final String WARNING = "WARNING";
    public static final String INFO = "INFO";

    public abstract String toJSONContent();

    public abstract void setFieldErrors(Object paramObject);

    public abstract void setContent(String paramString);

    public abstract void setType(String paramString);
}

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.dojoTag.ActionMessage
 * JD-Core Version:    0.6.2
 */