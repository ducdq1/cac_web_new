/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zkoss.zul;

import java.util.Date;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;

/**
 *
 * @author hoangnv28
 */
public class DateConstraint implements Constraint {

    private final Datebox datebox;
    private final String constraint;

    public DateConstraint(Datebox datebox, String constraint) {
        this.datebox = datebox;
        this.constraint = constraint;
    }

    @Override
    public void validate(Component comp, Object value) throws WrongValueException {
        if (comp instanceof Datebox && value != null && this.datebox.getValue() != null) {
            if (null != constraint) {
                if (constraint.contains("before")) {
                    if (((Date) value).after(this.datebox.getValue())) {
                        throw new WrongValueException(comp, ((Datebox) comp).getName() + " không được sau " + this.datebox.getName());
                    }
                }

                if (constraint.contains("after")) {
                    if (((Date) value).before(this.datebox.getValue())) {
                        throw new WrongValueException(comp, ((Datebox) comp).getName() + " không được trước " + this.datebox.getName());
                    }
                }
            }
        }
    }

}
