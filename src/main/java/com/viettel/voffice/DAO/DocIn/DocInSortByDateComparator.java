package com.viettel.voffice.DAO.DocIn;

import java.util.Comparator;

import com.viettel.voffice.model.DocumentProcess;

public class DocInSortByDateComparator implements
		Comparator<DocumentProcess> {

	@Override
	public int compare(DocumentProcess doc1, DocumentProcess doc2) {
		int compareReceiveDate = doc1.getDocumentReceive().getReceiveDate()
				.compareTo(doc2.getDocumentReceive().getReceiveDate());
		if (compareReceiveDate != 0)
			return compareReceiveDate;

		int comparePublishDate = doc1.getDocumentReceive().getPublishDate()
				.compareTo(doc2.getDocumentReceive().getPublishDate());
		if (comparePublishDate != 0)
			return comparePublishDate;

		int compareDeadline = doc1.getDocumentReceive().getDeadlineByDate()
				.compareTo(doc2.getDocumentReceive().getDeadlineByDate());
		if (compareDeadline != 0)
			return compareDeadline;

		Long value = doc1.getDocumentReceive().getDocumentReceiveId()
				- doc2.getDocumentReceive().getDocumentReceiveId();
		return value < 0 ? -1 : value == 0 ? 0 : 1;
	}

}
