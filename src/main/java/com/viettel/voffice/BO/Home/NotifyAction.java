/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.Home;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HaVM2
 */
@Entity 
@Table(name = "NOTIFY_ACTION") 
@XmlRootElement 
@NamedQueries({ 
    @NamedQuery(name = "NotifyAction.findAll", query = "SELECT n FROM NotifyAction n"),
    @NamedQuery(name = "NotifyAction.findByNotifyActionId", query = "SELECT n FROM NotifyAction n WHERE n.notifyActionId = :notifyActionId"),
    @NamedQuery(name = "NotifyAction.findByNotifyId", query = "SELECT n FROM NotifyAction n WHERE n.notifyId = :notifyId"),
    @NamedQuery(name = "NotifyAction.findByActionName", query = "SELECT n FROM NotifyAction n WHERE n.actionName = :actionName"),
    @NamedQuery(name = "NotifyAction.findByActionUrl", query = "SELECT n FROM NotifyAction n WHERE n.actionUrl = :actionUrl")})
public class NotifyAction implements Serializable {
    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "NOTIFY_ACTION_SEQ", sequenceName = "NOTIFY_ACTION_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFY_ACTION_SEQ")
    @Column(name = "NOTIFY_ACTION_ID")
    private Long notifyActionId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NOTIFY_ID")
    private long notifyId;
    @Size(max = 100)
    @Column(name = "ACTION_NAME")
    private String actionName;
    @Size(max = 500)
    @Column(name = "ACTION_URL")
    private String actionUrl;

    public NotifyAction() {
    }

    public NotifyAction(Long notifyActionId) {
        this.notifyActionId = notifyActionId;
    }

    public NotifyAction(Long notifyActionId, long notifyId) {
        this.notifyActionId = notifyActionId;
        this.notifyId = notifyId;
    }

    public Long getNotifyActionId() {
        return notifyActionId;
    }

    public void setNotifyActionId(Long notifyActionId) {
        this.notifyActionId = notifyActionId;
    }

    public long getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(long notifyId) {
        this.notifyId = notifyId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notifyActionId != null ? notifyActionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof NotifyAction)) {
            return false;
        }
        NotifyAction other = (NotifyAction) object;
        if ((this.notifyActionId == null && other.notifyActionId != null) || (this.notifyActionId != null && !this.notifyActionId.equals(other.notifyActionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Home.NotifyAction[ notifyActionId=" + notifyActionId + " ]";
    }
    
}
