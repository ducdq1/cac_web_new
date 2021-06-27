package com.viettel.core.base.model;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.AbstractListModel;

public abstract class AbstractPagingListModel<T> extends AbstractListModel {

    /**
     *
     */
    private static final long serialVersionUID = 6613208067174831719L;

    //internal use only
    protected List<T> _items = new ArrayList<>();

    public AbstractPagingListModel() {
        super();
    }

    @Override
    public Object getElementAt(int index) {
        return _items.get(index);
    }

    @Override
    public int getSize() {
        return _items.size();
    }
}
