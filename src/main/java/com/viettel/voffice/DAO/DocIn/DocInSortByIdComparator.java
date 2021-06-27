package com.viettel.voffice.DAO.DocIn;

import java.util.Comparator;

import com.viettel.voffice.BO.Document.DocumentReceive;

public class DocInSortByIdComparator implements Comparator<DocumentReceive> {

	@Override
	public int compare(DocumentReceive doc1, DocumentReceive doc2) {
		Long value = doc1.getDocumentReceiveId() - doc2.getDocumentReceiveId();
		return value < 0 ? -1 : value == 0 ? 0 : 1;
	}

}
