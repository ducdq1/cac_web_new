package com.viettel.voffice.DAO.DocIn;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.utils.Constants;
import com.viettel.utils.DateTimeUtils;
import com.viettel.voffice.BO.Document.DocumentPublish;
import com.viettel.voffice.DAOHE.DocumentDAOHE;
import com.viettel.voffice.model.DocumentSearchModel;

public class DocInChooseDocOutComposer extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6668811520102737976L;

	@WireVariable
	private DocumentDAOHE docDaoHe;
	private DocumentSearchModel searchModel;
	@Wire
	Listbox docDraftListBox;
	@Wire
	Window wdChooseDocOut;
	Groupbox fullSearchGbx;

	// Component form tìm kiếm
	@Wire("#incSearchDocOut window #lbDocType")
	private Listbox lbDocType;
	@Wire("#incSearchDocOut window #tbDocumentAbstract")
	private Textbox tbDocumentAbstract;
	@Wire("#incSearchDocOut window #tbDocumentCode")
	private Textbox tbDocumentCode;
	@Wire("#incSearchDocOut window #dbCreateFrom")
	private Datebox dbCreateFrom;
	@Wire("#incSearchDocOut window #dbCreateTo")
	private Datebox dbCreateTo;
	@Wire
	private Listbox lbDocDraft;
	@Wire
	private Paging pagingTop, pagingBottom;

	private Window parentWindow;

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);

		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions
				.getCurrent().getArg();
		parentWindow = (Window) arguments.get("windowParent");
		onSearch();
	}

	@Listen("onPaging = #userPagingTop, #userPagingBottom")
	public void onPaging(Event event) {
		final PagingEvent pe = (PagingEvent) event;
		if (pagingTop.equals(pe.getTarget())) {
			pagingBottom.setActivePage(pagingTop.getActivePage());
		} else {
			pagingTop.setActivePage(pagingBottom.getActivePage());
		}
		loadDocOut(true);
	}

	@Listen("onClick = #incSearchDocOut window #btnSearch")
	public void onSearch() {
		loadDocOut(false);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void loadDocOut(boolean isPaging) {
		searchModel = createSearchModel();
	
			DocumentDAOHE documentDAOHE = new DocumentDAOHE();
			List result = documentDAOHE.onSearchReplyForDocOut(searchModel,
					getDeptId(),
					pagingTop.getActivePage() * pagingTop.getPageSize() + 1,
					pagingTop.getPageSize(), false);
			ListModelList model = new ListModelList(result);
			model.setMultiple(true);
			lbDocDraft.setModel(model);
			lbDocDraft.renderAll();

			// Chuyển trang thì không cần tính lại số văn bản
			if (!isPaging) {
				Long count = (Long) documentDAOHE
						.onSearchReplyForDocOut(
								searchModel,
								getDeptId(),
								pagingTop.getActivePage()
										* pagingTop.getPageSize() + 1,
								pagingTop.getPageSize(), true).get(0);
				pagingTop.setTotalSize(count.intValue());
				pagingBottom.setTotalSize(count.intValue());
			}
		
	}

	@Listen("onClick = #btnSelect")
	public void onSelectDocOut() {
		Set<Listitem> selItems = lbDocDraft.getSelectedItems();
		List<DocumentPublish> listDocOut = new ArrayList<>();
		for (Listitem item : selItems) {
			listDocOut.add((DocumentPublish) item.getValue());
		}
		Events.sendEvent("onSelectedDocOut", parentWindow, listDocOut);
		wdChooseDocOut.onClose();
	}

	private DocumentSearchModel createSearchModel() {
		if (searchModel == null)
			searchModel = new DocumentSearchModel();

		// Loại văn bản
		if (lbDocType.getSelectedIndex() >= 0) {
			Category doctype = lbDocType.getSelectedItem().getValue();
			searchModel.setDocumentTypeId(doctype.getCategoryId());
		} else {
			searchModel.setDocumentTypeId(null);
		}

		// Trích yếu văn bản
		String docAbstract = tbDocumentAbstract.getText().trim();
		if (!"".equals(docAbstract)) {
			searchModel.setDocumentAbstract(docAbstract);
		} else {
			searchModel.setDocumentAbstract(null);
		}

		// Mã văn bản
		String docCode = tbDocumentCode.getText().trim();
		if (!"".equals(docCode)) {
			searchModel.setDocumentCode(docCode);
		} else {
			searchModel.setDocumentCode(null);
		}

		// Ngày tạo
		Date dateFrom = DateTimeUtils.setStartTimeOfDate(dbCreateFrom
				.getValue());
		searchModel.setDateCreateFrom(dateFrom);
		Date dateTo = DateTimeUtils.setEndTimeOfDate(dbCreateTo.getValue());
		searchModel.setDateCreateTo(dateTo);

		// Đơn vị ban hành
		searchModel.setCreateDeptId(getDeptId());

		// Tìm những văn bản đã ban hành
		searchModel.setStatus(Constants.DOCUMENT_STATUS.PUBLISH);

		return searchModel;
	}

	@Listen("onClick = #btnClose")
	public void onClose() {
		wdChooseDocOut.onClose();
	}

	@Listen("onClick = #btnSelect")
	public void onSelect() {
		List<DocumentPublish> selectedItems = new ArrayList<>();
		for (Listitem item : lbDocDraft.getSelectedItems()) {
			selectedItems.add((DocumentPublish) item.getValue());
		}
		Events.sendEvent("onSelectedDocOut", parentWindow, selectedItems);
		wdChooseDocOut.onClose();
	}

	public DocumentSearchModel getSearchModel() {
		return searchModel;
	}

	public void setSearchModel(DocumentSearchModel searchModel) {
		this.searchModel = searchModel;
	}
}
