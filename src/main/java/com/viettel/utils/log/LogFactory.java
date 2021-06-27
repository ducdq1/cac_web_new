package com.viettel.utils.log;

public abstract interface LogFactory {

    public abstract Logger getLogger(String paramString);

    public abstract Logger getLogger(Class paramClass);
}

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.common.util.log.LogFactory
 * JD-Core Version:    0.6.2
 */