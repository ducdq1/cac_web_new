/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.model;

import com.viettel.utils.Constants;
import com.viettel.utils.DateTimeUtils;
import com.viettel.voffice.BO.Document.DocumentPublish;
import com.viettel.voffice.BO.Document.DocumentReceive;
import com.viettel.core.workflow.BO.Process;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.joda.time.LocalDate;

/**
 *
 * @author ChucHV
 */
public class DocumentProcess {

	private DocumentReceive documentReceive;
	private DocumentPublish documentPublish;
	private List<Process> listProcess;

	private int normalProcess = 0;
	private int expiredProcess = 0;
	private int warningProcess = 0;

	public DocumentProcess() {
		this.listProcess = new ArrayList<>();
	}

	public DocumentProcess(DocumentReceive documentReceive) {
		this.documentReceive = documentReceive;
		this.listProcess = new ArrayList<>();
	}
	
	public DocumentProcess(DocumentPublish documentPublish) {
		this.documentPublish = documentPublish;
		this.listProcess = new ArrayList<>();
	}

	public void addProcess(Process process) {
		this.listProcess.add(process);
	}

	public void checkProcess() {
		LocalDate now = LocalDate.now();
		LocalDate twoDayLater = now.plusDays(2);
		for (Process p : listProcess) {

			if (p.getDeadline() != null) {
				LocalDate deadline = LocalDate.fromDateFields(p.getDeadline());
				if (p.getFinishDate() == null) {
					if (deadline.isBefore(now)) {
						if (Objects.equals(Constants.PROCESS_STATUS.NEW,
								p.getStatus())) {
							expiredProcess = 1;
						} else {
							expiredProcess = 2;
						}
					} else if (deadline.isBefore(twoDayLater)) {
						if (Objects.equals(Constants.PROCESS_STATUS.NEW,
								p.getStatus())) {
							warningProcess = 1;
						} else {
							warningProcess = 2;
						}
					} else {
						if (Objects.equals(Constants.PROCESS_STATUS.NEW,
								p.getStatus())) {
							normalProcess = 1;
						} else {
							normalProcess = 2;
						}
					}
				} else {
					LocalDate finishDate = LocalDate.fromDateFields(p
							.getFinishDate());
					if (deadline.isBefore(finishDate)) {
						if (Objects.equals(Constants.PROCESS_STATUS.NEW,
								p.getStatus())) {
							expiredProcess = 1;
						} else {
							expiredProcess = 2;
						}
					} else {
						if (Objects.equals(Constants.PROCESS_STATUS.NEW,
								p.getStatus())) {
							normalProcess = 1;
						} else {
							normalProcess = 2;
						}
					}
				}
			} else {
				if (Objects.equals(Constants.PROCESS_STATUS.NEW, p.getStatus())) {
					normalProcess = 1;
				} else {
					normalProcess = 2;
				}
			}
		}
	}

	public String getDeadlineStr() {
		Date deadline = documentReceive.getDeadlineByDate();
		for (Process p : listProcess) {
			if (deadline == null) {
				deadline = p.getDeadline();
				continue;
			}
			if (p.getDeadline() != null)
				if (p.getDeadline().before(deadline)) {
					deadline = p.getDeadline();
				}
		}
		if (deadline == null)
			return "";
		return DateTimeUtils.convertDateToString(deadline);
	}

	public DocumentReceive getDocumentReceive() {
		return documentReceive;
	}

	public void setDocumentReceive(DocumentReceive documentReceive) {
		this.documentReceive = documentReceive;
	}

	public DocumentPublish getDocumentPublish() {
		return documentPublish;
	}

	public void setDocumentPublish(DocumentPublish documentPublish) {
		this.documentPublish = documentPublish;
	}

	public List<Process> getListProcess() {
		return listProcess;
	}

	public void setListProcess(List<Process> listProcess) {
		this.listProcess = listProcess;
	}

	public int getNormalProcess() {
		return normalProcess;
	}

	public int getExpiredProcess() {
		return expiredProcess;
	}

	public int getWarningProcess() {
		return warningProcess;
	}
}
