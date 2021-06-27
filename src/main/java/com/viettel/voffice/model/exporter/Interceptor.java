/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.model.exporter;

/**
 *
 * @author ChucHV
 * @param <T>
 */
public interface Interceptor<T> {

    /**
     * Called before rendering
     *
     * @param target
     */
    public void beforeRendering(T target);

    /**
     * Called after rendering
     *
     * @param target
     */
    public void afterRendering(T target);
}
