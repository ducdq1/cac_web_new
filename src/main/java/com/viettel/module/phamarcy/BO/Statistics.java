package com.viettel.module.phamarcy.BO;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.viettel.core.base.DAO.BaseComposer;

/**
 * 
 * @author tuannt40
 *
 */
@Entity
@Table(name = "STATISTICS")
@XmlRootElement
public class Statistics extends BaseComposer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8823593201525222855L;

	@SequenceGenerator(name = "STATISTICS_SEQ", sequenceName = "STATISTICS_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STATISTICS_SEQ")
	@Basic(optional = false)
	@Column(name = "ID")
	private Long id;

	@Column(name = "PRODUCT_ID")
	private Long productId;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "CREATE_DATE")
	private Date createDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
