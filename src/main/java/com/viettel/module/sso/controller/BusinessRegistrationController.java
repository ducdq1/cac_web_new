package com.viettel.module.sso.controller;

import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;

public class BusinessRegistrationController extends CasBaseController {
	private static final long serialVersionUID = 1767952829949435022L;

	@Wire public Textbox tbRegisEmail;
	@Wire public Textbox tbNameVN;
	@Wire public Textbox tbTaxCode;
	@Wire public Textbox tbNameEN;
	@Wire public Textbox tbNumberInLaw;
	@Wire public Textbox tbSortName;
	@Wire public Combobox cbTinh;
	@Wire public Textbox tbAddress;
	@Wire public Textbox tbFax;
	@Wire public Combobox cbQuan;
	@Wire public Textbox tbPhone;
	@Wire public Textbox tbSite;
	@Wire public Combobox cbXa;
	@Wire public Textbox tbEmail;
	@Wire public Textbox tbFactorName;
	@Wire public Combobox cbFactorPosition;
	@Wire public Textbox tbFactorEmail;
	@Wire public Textbox tbFactorPhone;
	@Wire public Textbox tbFactorNameVN;
	@Wire public Textbox tbFactorAddressVN;
	@Wire public Textbox tbFactorEmailVN;
	@Wire public Textbox tbFactorPhoneVN;
	
	@Listen("onClick=#btnSave")
	public void onSave() {
		
	}
}
