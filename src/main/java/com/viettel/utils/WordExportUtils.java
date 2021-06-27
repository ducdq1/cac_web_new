/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.utils;

//import com.viettel.ws.client.Converter_Service;
//import com.viettel.ws.client.PdfDocxFile;
import java.io.ByteArrayOutputStream;
import java.io.File;
///
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamResult;

import org.docx4j.XmlUtils;
import org.docx4j.convert.out.html.AbstractHtmlExporter;
import org.docx4j.convert.out.html.AbstractHtmlExporter.HtmlSettings;
import org.docx4j.convert.out.html.HtmlExporterNG2;
import org.docx4j.jaxb.Context;
import org.docx4j.model.structure.SectionWrapper;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.FldChar;
import org.docx4j.wml.FooterReference;
import org.docx4j.wml.Ftr;
import org.docx4j.wml.Hdr;
import org.docx4j.wml.HdrFtrRef;
import org.docx4j.wml.HeaderReference;
import org.docx4j.wml.Jc;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.R;
import org.docx4j.wml.STFldCharType;
import org.docx4j.wml.SectPr;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Filedownload;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.module.ca.SignErrorCode;
import com.viettel.module.phamarcy.BO.VOrderDetail;
import com.viettel.utils.model.GroupModel;

///
/**
 *
 * @author Administrator
 */
@SuppressWarnings({ "deprecation", "serial" })
public class WordExportUtils extends BaseComposer {

	private static final ObjectFactory objectFactory = new ObjectFactory();
	@SuppressWarnings("unused")
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(WordExportUtils.class);

	public static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
		List<Object> result = new ArrayList<>();
		if (obj instanceof JAXBElement) {
			obj = ((JAXBElement<?>) obj).getValue();
		}
		if (obj.getClass().equals(toSearch)) {
			result.add(obj);
		} else if (obj instanceof ContentAccessor) {
			List<?> children = ((ContentAccessor) obj).getContent();
			for (Object child : children) {
				result.addAll(getAllElementFromObject(child, toSearch));
			}
		}
		return result;
	}

	/**
	 * duc
	 * 
	 * @param template
	 * @param name
	 * @param placeholder
	 */
	public void replacePlaceholder(WordprocessingMLPackage template, String name, String placeholder) {// xx
		try {
			List<Object> rs = getAllElementFromObject(template.getMainDocumentPart(), P.class);
			ObjectFactory factory = Context.getWmlObjectFactory();
			for (Object r : rs) {
				P run = (P) r;
				List<Object> texts = getAllElementFromObject(run, Text.class);
				for (Object text : texts) {
					try {
						Text textElement = (Text) text;
						if (textElement.getValue() != null) {
							if (textElement.getValue().equals(placeholder)) {
								if (name != null) {
									if (name.contains("\n")) {
										String[] chars = name.split("\n");
										textElement.setValue("");
										for (int i = 0; i < chars.length; i++) {
											run.getContent().remove(run);
											run.getContent().add(App.createR(factory, chars[i].toString()));
											if (i < chars.length - 1) {
												PPr ppr = run.getPPr();
												Jc jc = Context.getWmlObjectFactory().createJc();
												ppr.setJc(jc);
												jc.setVal(org.docx4j.wml.JcEnumeration.LEFT);
												run.getContent().add(App.createBr(factory));

											}
										}
									} else {
										textElement.setValue(name);
										String value = SuperScript.convertToSuperSrcipt((String) name);
										if (name.contains("^(") == true || name.contains("_(") == true) {
											textElement.setValue("");
											run.getContent().add(App.createParagraph(factory, name));
										} else {
											textElement.setValue(value);
										}
									}
								} else {
									textElement.setValue("");
								}
							}
						}
					} catch (NullPointerException en) {
						Logger.getLogger(WordExportUtils.class.getName()).log(Level.SEVERE, null, en);
					}
				}
			}
		} catch (NullPointerException ex) {
			Logger.getLogger(WordExportUtils.class.getName()).log(Level.SEVERE, null, ex);

		}
	}

	public void replacePlaceholderWithFontSize(WordprocessingMLPackage template, String name, String placeholder,
			int fontSize) {// xx
		try {
			List<Object> rs = getAllElementFromObject(template.getMainDocumentPart(), P.class);
			ObjectFactory factory = Context.getWmlObjectFactory();
			for (Object r : rs) {
				P run = (P) r;
				List<Object> texts = getAllElementFromObject(run, Text.class);
				for (Object text : texts) {
					try {
						Text textElement = (Text) text;
						if (textElement.getValue() != null) {
							if (textElement.getValue().equals(placeholder)) {
								if (name != null) {
									if (name.contains("\n")) {
										String[] chars = name.split("\n");
										textElement.setValue("");
										for (int i = 0; i < chars.length; i++) {
											run.getContent().remove(run);
											run.getContent().add(
													App.createRWithFontSize(factory, chars[i].toString(), fontSize));
											if (i < chars.length - 1) {
												PPr ppr = run.getPPr();
												Jc jc = Context.getWmlObjectFactory().createJc();
												ppr.setJc(jc);
												jc.setVal(org.docx4j.wml.JcEnumeration.LEFT);
												run.getContent().add(App.createBr(factory));
											}
										}
									} else {
										textElement.setValue(name);
										String value = SuperScript.convertToSuperSrcipt((String) name);
										if (name.contains("^(") == true || name.contains("_(") == true) {
											textElement.setValue("");
											run.getContent()
													.add(App.createParagraphWithFontSize(factory, name, fontSize));
										} else {
											textElement.setValue(value);
										}
									}
								} else {
									textElement.setValue("");
								}
							}
						}
					} catch (NullPointerException en) {
						Logger.getLogger(WordExportUtils.class.getName()).log(Level.SEVERE, null, en);
					}
				}
			}
		} catch (NullPointerException ex) {
			Logger.getLogger(WordExportUtils.class.getName()).log(Level.SEVERE, null, ex);

		}
	}

	public void replacePlaceholder(WordprocessingMLPackage template, Boolean check, String placeholder) {
		try {
			List<Object> rs = getAllElementFromObject(template.getMainDocumentPart(), P.class);
			ObjectFactory factory = Context.getWmlObjectFactory();
			for (Object r : rs) {
				P run = (P) r;
				List<Object> texts = getAllElementFromObject(run, Text.class);
				String fieldName = "";
				boolean bAdd = false;
				for (Object text : texts) {
					try {
						Text textElement = (Text) text;
						if (bAdd) {
							fieldName += textElement.getValue();
						} else {
							fieldName = textElement.getValue();
						}
						if (fieldName.startsWith("${")) {
							if (!fieldName.endsWith("}")) {
								textElement.setValue("");
								bAdd = true;
								continue;
							}
						} else {
							bAdd = false;
							continue;
						}

						if (fieldName != null) {
							fieldName = fieldName.replace("${", "").replace("}", "");
							if (fieldName.equals(placeholder)) {
								textElement.setValue("");
								run.getContent().remove(textElement);

								if (check != null) {
									if (check == true) {
										run.getContent().add(App.createCheckedbox(factory));
									} else {
										run.getContent().add(App.createUnCheckbox(factory));
									}
								}
							}
						}
					} catch (NullPointerException en) {
						Logger.getLogger(WordExportUtils.class.getName()).log(Level.SEVERE, null, en);
					}
				}
			}
		} catch (NullPointerException ex) {
			Logger.getLogger(WordExportUtils.class.getName()).log(Level.SEVERE, null, ex);

		}
	}

	public void createHeaderPart(WordprocessingMLPackage wordprocessingMLPackage, String footerContent)
			throws InvalidFormatException {
		HeaderPart headerPart = new HeaderPart();

		headerPart.setJaxbElement(getHdr(footerContent));
		Relationship relationship = wordprocessingMLPackage.getMainDocumentPart().addTargetPart(headerPart);

		SectPr sectPr = objectFactory.createSectPr();

		HeaderReference headerReference = objectFactory.createHeaderReference();
		headerReference.setId(relationship.getId());
		headerReference.setType(HdrFtrRef.DEFAULT);
		sectPr.getEGHdrFtrReferences().add(headerReference);// add header or

		wordprocessingMLPackage.getMainDocumentPart().addObject(sectPr);
	}

	public void createFooterPart(WordprocessingMLPackage wordprocessingMLPackage, String footerContent)
			throws InvalidFormatException {

		FooterPart footerPart = new FooterPart();

		footerPart.setJaxbElement(getFtrWithPageNumber(footerContent));
		Relationship relationship = wordprocessingMLPackage.getMainDocumentPart().addTargetPart(footerPart);

		SectPr sectPr = objectFactory.createSectPr();

		FooterReference footerReference = objectFactory.createFooterReference();
		footerReference.setId(relationship.getId());
		footerReference.setType(HdrFtrRef.DEFAULT);
		sectPr.getEGHdrFtrReferences().add(footerReference);// add header or

		wordprocessingMLPackage.getMainDocumentPart().addObject(sectPr);
	}

	public Hdr getHdr(String footerContent) {

		Hdr hdr = objectFactory.createHdr();

		hdr.getEGBlockLevelElts().add(getP(footerContent));
		return hdr;

	}

	public Ftr getFtr(String footerContent) {

		Ftr ftr = objectFactory.createFtr();

		ftr.getEGBlockLevelElts().add(getP(footerContent));
		return ftr;

	}

	public Ftr getFtrWithPageNumber(String footerContent) {
		Ftr ftr = objectFactory.createFtr();
		P paragraph = objectFactory.createP();
		P paragraphL = objectFactory.createP();

		addFooterContent(paragraphL, footerContent);
		addFieldBegin(paragraph);
		addPageNumberField(paragraph);
		addFieldEnd(paragraph);

		ftr.getContent().add(paragraphL);
		ftr.getContent().add(paragraph);
		return ftr;
	}

	private static void addFooterContent(P paragraph, String footerContent) {
		R run = objectFactory.createR();
		Text txt = new Text();
		txt.setSpace("preserve");
		txt.setValue(footerContent);
		PPr ppr = objectFactory.createPPr();
		Jc jc = objectFactory.createJc();
		jc.setVal(JcEnumeration.LEFT);
		ppr.setJc(jc);
		paragraph.setPPr(ppr);

		JAXBElement<Text> object = objectFactory.createRInstrText(txt);
		run.getContent().add(object);
		paragraph.getContent().add(run);
	}

	private static void addPageNumberField(P paragraph) {
		R run = objectFactory.createR();
		Text txt = new Text();
		txt.setSpace("preserve");
		txt.setValue(" PAGE   \\* MERGEFORMAT ");
		PPr ppr = objectFactory.createPPr();
		Jc jc = objectFactory.createJc();
		jc.setVal(JcEnumeration.RIGHT);
		ppr.setJc(jc);
		paragraph.setPPr(ppr);

		JAXBElement<Text> object = objectFactory.createRInstrText(txt);
		run.getContent().add(object);

		paragraph.getContent().add(run);

	}

	private static void addFieldBegin(P paragraph) {
		R run = objectFactory.createR();
		FldChar fldchar = objectFactory.createFldChar();
		fldchar.setFldCharType(STFldCharType.BEGIN);
		run.getContent().add(fldchar);
		paragraph.getContent().add(run);
	}

	private static void addFieldEnd(P paragraph) {
		FldChar fldcharend = objectFactory.createFldChar();
		fldcharend.setFldCharType(STFldCharType.END);
		R run3 = objectFactory.createR();
		run3.getContent().add(fldcharend);
		paragraph.getContent().add(run3);
	}

	public P getP(String footerContent) {
		P headerP = objectFactory.createP();
		R run1 = objectFactory.createR();
		Text text = objectFactory.createText();
		text.setValue(footerContent);
		run1.getRunContent().add(text);
		headerP.getParagraphContent().add(run1);
		return headerP;
	}

	public void createFooter(WordprocessingMLPackage wmp) {
		try {
			Relationship relationship = createFooterPart();
			createHeaderReference(wmp, relationship);
		} catch (InvalidFormatException ex) {
			LogUtils.addLog(ex);
		}
	}

	public Relationship createFooterPart(WordprocessingMLPackage wordprocessingMLPackage) throws Exception {

		FooterPart footerPart = new FooterPart();
		Relationship rel = wordprocessingMLPackage.getMainDocumentPart().addTargetPart(footerPart);

		// After addTargetPart, so image can be added properly
		footerPart.setJaxbElement(getFtr());

		return rel;
	}

	public void createHeaderReference(WordprocessingMLPackage wordprocessingMLPackage, Relationship relationship)
			throws InvalidFormatException {
		List<SectionWrapper> sections = wordprocessingMLPackage.getDocumentModel().getSections();

		SectPr sectPr = sections.get(sections.size() - 1).getSectPr();
		// There is always a section wrapper, but it might not contain a sectPr
		if (sectPr == null) {
			sectPr = objectFactory.createSectPr();
			wordprocessingMLPackage.getMainDocumentPart().addObject(sectPr);
			sections.get(sections.size() - 1).setSectPr(sectPr);
		}

		HeaderReference headerReference = objectFactory.createHeaderReference();
		headerReference.setId(relationship.getId());
		headerReference.setType(HdrFtrRef.DEFAULT);
		sectPr.getEGHdrFtrReferences().add(headerReference);// add header or
		// footer references

	}

	public Ftr getFtr() throws Exception {

		Ftr hdr = objectFactory.createFtr();

		hdr.getContent().add("sss");
		return hdr;

	}

	public void replacePlaceholder(WordprocessingMLPackage template, HashMap<String, Object> map) {
		try {
			List<Object> rs = getAllElementFromObject(template.getMainDocumentPart(), P.class);
			ObjectFactory factory = Context.getWmlObjectFactory();
			for (Object r : rs) {
				P run = (P) r;
				List<Object> texts = getAllElementFromObject(run, Text.class);
				boolean bAdd = false;
				String fieldName = "";
				for (Object text : texts) {
					try {
						Text textElement = (Text) text;
						if (bAdd) {
							fieldName += textElement.getValue();
						} else {
							fieldName = textElement.getValue();
						}
						if (fieldName.endsWith("${createForm.signDate}")) {
							System.out.println("havm debug");
						}
						if (fieldName.startsWith("${")) {
							if (!fieldName.endsWith("}")) {
								textElement.setValue("");
								bAdd = true;
								continue;
							}
						} else {
							bAdd = false;
							continue;
						}
						if (textElement.getValue() != null) {
							if (fieldName.startsWith("${") && fieldName.endsWith("}")) {
								String attribute = fieldName.replace("${", "").replace("}", "");
								String[] lstAttribute = attribute.split("\\.");
								Object mainObj = map.get(lstAttribute[0]);
								for (int i = 1; i < lstAttribute.length; i++) {
									mainObj = getAttributeObjByName(mainObj, lstAttribute[i]);
								}
								if (mainObj != null) {
									String source = String.valueOf(mainObj);
									if (mainObj instanceof Date) {
										source = DateTimeUtils.convertDateToString((Date) mainObj);
									}
									if (source.contains("\n")) {
										String[] chars = source.split("\n");
										textElement.setValue("");
										for (int i = 0; i < chars.length; i++) {
											String value = SuperScript.convertToSuperSrcipt(chars[i]);
											source = value;
											if (source.contains("^(") == true || source.contains("_(") == true) {
												run.getContent().remove(run);
												run.getContent().add(App.createR(factory, source));
											} else {
												run.getContent().remove(run);
												run.getContent().add(App.createR(factory, source));
												if (i < chars.length - 1) {
													PPr ppr = run.getPPr();
													Jc jc = Context.getWmlObjectFactory().createJc();
													ppr.setJc(jc);
													jc.setVal(org.docx4j.wml.JcEnumeration.LEFT);
													run.getContent().add(App.createBr(factory));
												}
												// textElement.setValue(value);
											}
										}
									} else {
										String value = SuperScript.convertToSuperSrcipt(source);
										if (source.contains("^(") == true || source.contains("_(") == true) {
											textElement.setValue("");
											run.getContent().remove(run);
											run.getContent().add(App.createR(factory, source));
										} else {
											textElement.setValue(value);
										}
									}
								} else {
									textElement.setValue("");
								}
							}
						} else {
							System.out.println("null");
						}
					} catch (NullPointerException ex) {
						LogUtils.addLog(ex);
					}
				}
			}
		} catch (NullPointerException ex) {
			Logger.getLogger(WordExportUtils.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void replaceTable(String[] placeholders, List<HashMap<String, String>> textToAdd,
			WordprocessingMLPackage template) throws Docx4JException, JAXBException {
		List<Object> tables = getAllElementFromObject(template.getMainDocumentPart(), Tbl.class);

		// 1. find the table
		Tbl tempTable = getTemplateTable(tables, placeholders[0]);
		List<Object> rows = getAllElementFromObject(tempTable, Tr.class);

		// first row is header, second row is content
		if (rows.size() == 2) {
			// this is our template row
			Tr templateRow = (Tr) rows.get(1);

			for (HashMap<String, String> replacements : textToAdd) {
				// 2 and 3 are done in this method
				addRowToTable(tempTable, templateRow, replacements);
			}

			// 4. remove the template row
			tempTable.getContent().remove(templateRow);
		}
	}

	@SuppressWarnings("rawtypes")
	public void replaceTable(WordprocessingMLPackage template, int iTable, List lstReplacement)
			throws Docx4JException, JAXBException {
		List<Object> tables = getAllElementFromObject(template.getMainDocumentPart(), Tbl.class);

		// 1. find the table
		Tbl tempTable = getTemplateTable(tables, iTable);
		List<Object> rows = getAllElementFromObject(tempTable, Tr.class);
		Tr trTongCong = (Tr) rows.get(4);
		Tr temp=(Tr) rows.get(2);
		tempTable.getContent().remove((Tr) rows.get(1));
		tempTable.getContent().remove((Tr) rows.get(3));
		
		addRowToTable(tempTable, temp, trTongCong, lstReplacement);
	}
	
	@SuppressWarnings("rawtypes")
	public void replaceTableKT(WordprocessingMLPackage template, int iTable, List lstReplacement)
			throws Docx4JException, JAXBException {
		List<Object> tables = getAllElementFromObject(template.getMainDocumentPart(), Tbl.class);

		// 1. find the table
		Tbl tempTable = getTemplateTable(tables, iTable);
		List<Object> rows = getAllElementFromObject(tempTable, Tr.class);
		addRowToTableKT(tempTable, (Tr) rows.get(rows.size()-1), lstReplacement);
	}

	public void replaceTableDoanhThu(WordprocessingMLPackage template, int iTable, List<VOrderDetail> lstReplacement)
			throws Docx4JException, JAXBException {
		List<Object> tables = getAllElementFromObject(template.getMainDocumentPart(), Tbl.class);

		// 1. find the table
		Tbl tempTable = getTemplateTable(tables, iTable);
		List<Object> rows = getAllElementFromObject(tempTable, Tr.class);

		Tr templateRow = (Tr) rows.get(4);
		Tr trPhatSinh = (Tr) rows.get(6);
		Tr trCuoiKy = (Tr) rows.get(7);
		tempTable.getContent().remove((Tr) rows.get(3));
		tempTable.getContent().remove((Tr) rows.get(5));
		addRowToTableDoanhThu(tempTable, templateRow, trPhatSinh, trCuoiKy, lstReplacement);
	}

	public void addRowToTableDoanhThu(Tbl reviewtable, Tr templateRow, Tr trPhatSinh, Tr trCuoiKy, List<VOrderDetail> replacements)
			throws IllegalArgumentException {
		try {
			if (replacements != null) {
				ObjectFactory factory = Context.getWmlObjectFactory();
				for (int i = 0; i < replacements.size(); i++) {
					Tr workingRow = (Tr) XmlUtils.deepCopy(templateRow);
					List<Object> colElements = getAllElementFromObject(workingRow, Tc.class);

					for (Object ob : colElements) {
						Tc col = (Tc) ob;
						List<?> textElements = getAllElementFromObject(col, Text.class);
						Object obj = replacements.get(i);
						List<Object> lst = col.getContent();

						for (Object object : textElements) {
							Text text = (Text) object;
							String attribute = text.getValue();

							String value = getAttributeOfObjByName(obj, attribute);
							if (value != null) {
								if (value.contains("^(") == true || value.contains("_(") == true) {
									col.getContent().clear();
									col.getContent().add(App.createParagraph(factory, value));
								} else {
									if (!value.contains("\r\n") && !value.contains("\n\r") && !value.contains("\n")) {
										text.setValue(value);
									} else {
										String[] lstContents = null;

										if (value.contains("\r\n")) {
											lstContents = value.split("\r\n");
										}
										if (value.contains("\n\r")) {
											lstContents = value.split("\n\r");
										}
										if (value.contains("\n")) {
											lstContents = value.split("\n");
										}

										List<?> lstP = getAllElementFromObject(col, P.class);
										P oldP = null;
										if (lstP != null && !lstP.isEmpty()) {
											oldP = (P) lstP.get(0);
										}
										for (String content : lstContents) {
											if (content.length() <= 1) {
												content = content + " ";
											}
											P p = App.createParagraph(factory, content);
											if (oldP != null) {
												p.setPPr(oldP.getPPr());
											}
											col.getContent().add(p);
										}
										col.getContent().remove(oldP);
									}

								}
							} else {
								text.setValue("");
							}

						}
					}

					reviewtable.getContent().add(workingRow);
				}
			}
			
			reviewtable.getContent().remove(templateRow);
			reviewtable.getContent().remove(trPhatSinh);
			reviewtable.getContent().remove(trCuoiKy);
			
			reviewtable.getContent().add(trPhatSinh);
			reviewtable.getContent().add(trCuoiKy);
			

		} catch (NullPointerException e) {
			LogUtils.addLog(e);
		}
	}

	public void deleteTable(WordprocessingMLPackage template, int iTable) throws Docx4JException, JAXBException {
		List<Object> tables = getAllElementFromObject(template.getMainDocumentPart(), Tbl.class);
		Tbl tempTable = getTemplateTable(tables, iTable);
		tempTable.getContent().clear();
		template.getMainDocumentPart().getContent().remove(tempTable);
	}

	public Tbl getTemplateTable(List<Object> tables, int i) throws Docx4JException, JAXBException {
		return (Tbl) tables.get(i);
	}

	public Tbl getTemplateTable(List<Object> tables, String templateKey) throws Docx4JException, JAXBException {
		for (Iterator<Object> iterator = tables.iterator(); iterator.hasNext();) {
			Object tbl = iterator.next();
			List<?> textElements = getAllElementFromObject(tbl, Text.class);
			for (Object text : textElements) {
				Text textElement = (Text) text;
				if (textElement.getValue() != null && textElement.getValue().equals(templateKey)) {
					return (Tbl) tbl;
				}
			}
		}
		return null;
	}

	public Object getAttributeObjByName(Object obj, String fieldName) {
		Object value = "";
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			value = field.get(obj);
		} catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
			/*
			 * bo ghi log loi k quan tam - binhnt53
			 * Logger.getLogger(WordExportUtils.class.getName()).log(Level.
			 * SEVERE, null, ex);
			 */
			LogUtils.addLog(e);
		}
		return value;
	}

	public String getAttributeOfObjByName(Object obj, String fieldName) {
		String value = "";
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			Object ob = field.get(obj);
			if (ob == null) {
				value = "";
			} else if (ob instanceof String) {
				value = (String) ob;
			} else {
				value = String.valueOf(field.get(obj));
			}
		} catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
			LogUtils.addLog(e);
		}
		return value;
	}

	public void createGroupTable(WordprocessingMLPackage template, int iTable, List<GroupModel> lstGroup)
			throws Docx4JException, JAXBException {
		List<Object> tables = getAllElementFromObject(template.getMainDocumentPart(), Tbl.class);

		Tbl tempTable = getTemplateTable(tables, iTable);
		List<Object> rows = getAllElementFromObject(tempTable, Tr.class);
		addGroupToTable(tempTable, (Tr) rows.get(1), (Tr) rows.get(2), lstGroup);
	}

	public void addGroupToTable(Tbl reviewtable, Tr templateGroupRow, Tr templateContentRow, List<GroupModel> lstGroup)
			throws IllegalArgumentException {
		if (lstGroup != null) {
			List<?> textElements;
			ObjectFactory factory = Context.getWmlObjectFactory();
			for (int i = 0; i < lstGroup.size(); i++) {
				GroupModel item = lstGroup.get(i);
				Tr headerRow = (Tr) XmlUtils.deepCopy(templateGroupRow);
				//
				// add header
				//
				List<Object> colElements = getAllElementFromObject(headerRow, Tc.class);
				Tc headerCol = (Tc) colElements.get(0);
				textElements = getAllElementFromObject(headerCol, Text.class);
				if (textElements != null && textElements.size() > 0) {
					Text text = (Text) textElements.get(0);
					text.setValue(item.getGroupName());
				}
				reviewtable.getContent().add(headerRow);
				//
				// add content
				//
				for (int j = 0; j < item.getLstItems().size(); j++) {
					Tr workingRow = (Tr) XmlUtils.deepCopy(templateContentRow);
					colElements = getAllElementFromObject(workingRow, Tc.class);
					for (Object ob : colElements) {
						Tc col = (Tc) ob;
						textElements = getAllElementFromObject(col, Text.class);
						Object obj = item.getLstItems().get(j);
						for (Object object : textElements) {
							Text text = (Text) object;
							String attribute = text.getValue();
							if (("index").equals(attribute)) {
								text.setValue(String.valueOf(j + 1));
							} else {
								String value = getAttributeOfObjByName(obj, attribute);
								if (value != null) {
									if (value.contains("^(") == true || value.contains("_(") == true) {
										// text.setValue("");
										col.getContent().clear();
										col.getContent().add(App.createParagraph(factory, value));
									} else if (("true").equals(value)) {
										// text.setValue("");
										col.getContent().clear();
										col.getContent().add(App.createCheckedbox(factory));
									} else if (("false").equals(value)) {
										// text.setValue("");
										col.getContent().clear();
										col.getContent().add(App.createUnCheckbox(factory));
									} else {
										if (!value.contains("\r\n")) {
											text.setValue(value);
										} else {
											String[] lstContents = value.split("\r\n");
											List<?> lstP = getAllElementFromObject(col, P.class);
											P oldP = null;
											if (lstP != null && !lstP.isEmpty()) {
												oldP = (P) lstP.get(0);
											}
											for (String content : lstContents) {
												P p = App.createParagraph(factory, content);
												if (oldP != null) {
													p.setPPr(oldP.getPPr());
												}
												col.getContent().add(p);
											}
											col.getContent().remove(oldP);
										}
									}
								} else {
									text.setValue("");
								}
							}
						}
					}
					reviewtable.getContent().add(workingRow);
				}
			}
		}
		reviewtable.getContent().remove(templateGroupRow);
		reviewtable.getContent().remove(templateContentRow);
	}

	@SuppressWarnings("rawtypes")
	public void addRowToTable(Tbl reviewtable, Tr templateRow, Tr tongCongRow, List replacements)
			throws IllegalArgumentException {
		try {
			if (replacements != null) {
				ObjectFactory factory = Context.getWmlObjectFactory();
				for (int i = 0; i < replacements.size(); i++) {
					Tr workingRow = (Tr) XmlUtils.deepCopy(templateRow);
					List<Object> colElements = getAllElementFromObject(workingRow, Tc.class);
					for (Object ob : colElements) {
						Tc col = (Tc) ob;
						List<?> textElements = getAllElementFromObject(col, Text.class);
						Object obj = replacements.get(i);
						for (Object object : textElements) {
							Text text = (Text) object;
							String attribute = text.getValue();
							if (("index").equals(attribute)) {
								text.setValue(String.valueOf(i + 1));
							} else {
								String value = getAttributeOfObjByName(obj, attribute);
								if (value != null) {
									if (value.contains("^(") == true || value.contains("_(") == true) {

										col.getContent().clear();
										col.getContent().add(App.createParagraph(factory, value));
									} else if (("true").equals(value)) {

										col.getContent().clear();
										col.getContent().add(App.createCheckedbox(factory));
									} else if (("false").equals(value)) {
										// text.setValue("");
										col.getContent().clear();
										col.getContent().add(App.createUnCheckbox(factory));
									} else {
										if (!value.contains("\r\n") && !value.contains("\n\r")
												&& !value.contains("\n")) {
											text.setValue(value);
										} else {
											String[] lstContents = null;

											if (value.contains("\r\n")) {
												lstContents = value.split("\r\n");
											}
											if (value.contains("\n\r")) {
												lstContents = value.split("\n\r");
											}
											if (value.contains("\n")) {
												lstContents = value.split("\n");
											}

											List<?> lstP = getAllElementFromObject(col, P.class);
											P oldP = null;
											if (lstP != null && !lstP.isEmpty()) {
												oldP = (P) lstP.get(0);
											}
											for (String content : lstContents) {
												if (content.length() <= 1) {
													content = content + " ";
												}
												P p = App.createParagraph(factory, content);
												if (oldP != null) {
													p.setPPr(oldP.getPPr());
												}
												col.getContent().add(p);
											}
											col.getContent().remove(oldP);
										}

									}
								} else {
									text.setValue("");
								}
							}
						}
					}

					reviewtable.getContent().add(workingRow);
				}
			}
			reviewtable.getContent().remove(templateRow);
			reviewtable.getContent().remove(tongCongRow);
			reviewtable.getContent().add(tongCongRow);
		} catch (NullPointerException e) {
			LogUtils.addLog(e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void addRowToTableKT(Tbl reviewtable, Tr templateRow,  List replacements)
			throws IllegalArgumentException {
		try {
			if (replacements != null) {
				ObjectFactory factory = Context.getWmlObjectFactory();
				for (int i = 0; i < replacements.size(); i++) {
					Tr workingRow = (Tr) XmlUtils.deepCopy(templateRow);
					List<Object> colElements = getAllElementFromObject(workingRow, Tc.class);
					for (Object ob : colElements) {
						Tc col = (Tc) ob;
						List<?> textElements = getAllElementFromObject(col, Text.class);
						Object obj = replacements.get(i);
						for (Object object : textElements) {
							Text text = (Text) object;
							String attribute = text.getValue();
							if (("index").equals(attribute)) {
								text.setValue(String.valueOf(i + 1));
							} else {
								String value = getAttributeOfObjByName(obj, attribute);
								if (value != null) {
									if (value.contains("^(") == true || value.contains("_(") == true) {

										col.getContent().clear();
										col.getContent().add(App.createParagraph(factory, value));
									} else if (("true").equals(value)) {

										col.getContent().clear();
										col.getContent().add(App.createCheckedbox(factory));
									} else if (("false").equals(value)) {
										// text.setValue("");
										col.getContent().clear();
										col.getContent().add(App.createUnCheckbox(factory));
									} else {
										if (!value.contains("\r\n") && !value.contains("\n\r")
												&& !value.contains("\n")) {
											text.setValue(value);
										} else {
											String[] lstContents = null;

											if (value.contains("\r\n")) {
												lstContents = value.split("\r\n");
											}
											if (value.contains("\n\r")) {
												lstContents = value.split("\n\r");
											}
											if (value.contains("\n")) {
												lstContents = value.split("\n");
											}

											List<?> lstP = getAllElementFromObject(col, P.class);
											P oldP = null;
											if (lstP != null && !lstP.isEmpty()) {
												oldP = (P) lstP.get(0);
											}
											for (String content : lstContents) {
												if (content.length() <= 1) {
													content = content + " ";
												}
												P p = App.createParagraph(factory, content);
												if (oldP != null) {
													p.setPPr(oldP.getPPr());
												}
												col.getContent().add(p);
											}
											col.getContent().remove(oldP);
										}

									}
								} else {
									text.setValue("");
								}
							}
						}
					}

					reviewtable.getContent().add(workingRow);
				}
			}
			reviewtable.getContent().remove(templateRow);
		
		} catch (NullPointerException e) {
			LogUtils.addLog(e);
		}
	}
	
	

	public void addRowToTable(Tbl reviewtable, Tr templateRow, HashMap<String, String> replacements) {
		Tr workingRow = (Tr) XmlUtils.deepCopy(templateRow);
		List<?> textElements = getAllElementFromObject(workingRow, Text.class);
		for (Object object : textElements) {
			Text text = (Text) object;
			String replacementValue = (String) replacements.get(text.getValue());
			replacementValue = SuperScript.convertToSuperSrcipt(replacementValue);
			if (replacementValue != null) {
				text.setValue(replacementValue);
			}
		}
		reviewtable.getContent().add(workingRow);
	}

	public void writeDocxToStream(WordprocessingMLPackage template, HttpServletResponse res)
			throws IOException, Docx4JException {
		Date newDate = new Date();
		String fileName = newDate.getTime() + ".docx";
		res.setContentType("application/vnd.ms-word");

		res.setHeader("Content-Disposition", "attachment; filename=report_" + fileName);
		res.setHeader("Content-Type", "application/vnd.ms-word");

		String path = ResourceBundleUtil.getString("dir_upload");
		FileUtil.mkdirs(path);
		//
		// Ghi ra file theo duong dan
		//
		path = path + File.separator + fileName;

		File file = new File(path);

		template.save(file);
		File tempFile = FileUtil.createTempFile(file, fileName);
		Filedownload.save(tempFile, path);

	}

	@SuppressWarnings("unused")
	public byte[] readAttachLabel(Long fileId, byte[] inputData, String fileCode, byte[] barCode,
			Boolean flagReadLabels) {
		try {
			// VoAttachsDAOHE daoHe = new VoAttachsDAOHE();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();

			// Add label page
			// step 1
			Document document = new Document(PageSize.A4);
			PdfWriter writer = PdfWriter.getInstance(document, outStream);

			// Add footer
			HeaderFooter event = new HeaderFooter("MA HO SO: " + fileCode, barCode);
			writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
			writer.setPageEvent(event);

			// step 3
			document.open();
			PdfContentByte cb = writer.getDirectContent();
			PdfImportedPage page;

			PdfReader reader = null;
			if (inputData != null && inputData.length > 0) {
				// Add current pages
				reader = new PdfReader(inputData);
				int pageNumberFound = reader.getNumberOfPages();
				for (int i = 1; i <= pageNumberFound; i++) {
					page = writer.getImportedPage(reader, i);
					document.newPage();
					cb.addTemplate(page, 0, 0);
				}
			}

			document.close();
			byte[] a = outStream.toByteArray();
			outStream.close();
			if (reader != null) {
				reader.close();
			}

			writer.close();
			return a;

		} catch (IOException | DocumentException ex) {
			Logger.getLogger(WordExportUtils.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	/*
	 * // This method returns a buffered image with the contents of an image
	 * public static BufferedImage toBufferedImage(java.awt.Image image) { if
	 * (image instanceof BufferedImage) { return (BufferedImage) image; }
	 * 
	 * // This code ensures that all the pixels in the image are loaded image =
	 * new ImageIcon(image).getImage();
	 * 
	 * // Create a buffered image with a format that's compatible with the
	 * screen BufferedImage bimage = null; GraphicsEnvironment ge =
	 * GraphicsEnvironment.getLocalGraphicsEnvironment(); try { // Determine the
	 * type of transparency of the new buffered image int transparency =
	 * Transparency.OPAQUE;
	 * 
	 * // Create the buffered image GraphicsDevice gs =
	 * ge.getDefaultScreenDevice(); GraphicsConfiguration gc =
	 * gs.getDefaultConfiguration(); bimage = gc.createCompatibleImage(
	 * image.getWidth(null), image.getHeight(null), transparency); } catch
	 * (HeadlessException e) { System.out.println(
	 * "The system does not have a screen"); }
	 * 
	 * if (bimage == null) { int type = BufferedImage.TYPE_INT_RGB; bimage = new
	 * BufferedImage(image.getWidth(null), image.getHeight(null), type); }
	 * 
	 * // Copy image to buffered image Graphics g = bimage.createGraphics();
	 * 
	 * // Paint the image onto the buffered image g.drawImage(image, 0, 0,
	 * null); g.dispose();
	 * 
	 * return bimage; }
	 */

	public void writePDFToStream(WordprocessingMLPackage template, HttpServletResponse res, Long fileId,
			String fileCode, byte[] barCode, Boolean flagReadLabels) throws IOException, Docx4JException {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		String PATH = rb.getString("ConvertService");
		FileUtil.mkdirs(PATH);
		Date newDate = new Date();
		String fileName = newDate.getTime() + ".docx";
		File f = new File(PATH + File.separatorChar + fileName);
		template.save(f);
	}

	public String writePDFToStream(String namefile, WordprocessingMLPackage template, boolean bdownload)
			throws IOException, Docx4JException {
		String outPut = "";
		try {
			ResourceBundle rb = ResourceBundle.getBundle("config");
			String PATH = rb.getString("ConvertService");
			FileUtil.mkdirs(PATH);
			File fd = new File(PATH);
			if (!fd.exists()) {
				fd.mkdirs();
			}
			Date newDate = new Date();
			String fileName = newDate.getTime() + ".docx";
			File f = new File(PATH + File.separatorChar + fileName);
			template.save(f);

			/*
			 * PdfDocxFile input = new PdfDocxFile();
			 * input.setContent(HandleFile.getByteFromFile(f));
			 * input.setFilename(f.getName()); input.setConvertType("docx2pdf");
			 * 
			 * Converter_Service service = new Converter_Service();
			 * com.viettel.convert.service.Converter con =
			 * service.getConverterPort(); output = con.convert(input);
			 */

			String path = f.getPath();
			outPut = path.substring(0, path.lastIndexOf("\\")) + "\\" + namefile + "_" + newDate.getTime() + ".pdf";
			boolean isConvertSuccess = convert2pdf(f.getPath(), outPut);
			// output = new PdfDocxFile();

			if (isConvertSuccess) {
				// output.setContent(HandleFile.getByteFromFile(new
				// File(outPut)));
			}

			if (bdownload) {
				// Filedownload.save(output.getContent(), "application/pdf",
				// output.getFilename());
			}

			if (f.exists())
				f.delete();

		} catch (Exception ex) {
			SignErrorCode sec = new SignErrorCode();
			showDialogWithNoEvent(sec.SI_025, sec.SI_026);
			Logger.getLogger(WordExportUtils.class.getName()).log(Level.SEVERE, null, ex);
		}

		return outPut;
	}

	public boolean convert2pdf(String input, String output) {
		HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
		String path = request.getRealPath("/WEB-INF/template/OfficeToPDF.exe");

		String[] params = new String[3];
		params[0] = path;
		params[1] = input;
		params[2] = output;
		try {
			Runtime.getRuntime().exec(params).waitFor();
			return new File(output).exists();
		} catch (IOException e) {
			LogUtils.addLog(e);
			return false;
		} catch (InterruptedException e) {
			LogUtils.addLog(e);
			return false;
		}
	}

	public void toHTML(WordprocessingMLPackage template, HttpServletResponse res)
			throws IOException, Docx4JException, Exception {
		Date newDate = new Date();
		String fileName = newDate.getTime() + ".docx";
		res.setContentType("application/vnd.ms-word");

		res.setHeader("Content-Disposition", "attachment; filename=report_" + fileName);
		res.setHeader("Content-Type", "application/vnd.ms-word");
		// File f = new
		// File(getRequest().getRealPath("/WEB-INF/template/download/")+fileName);
		// template.save(f);
		AbstractHtmlExporter exporter = new HtmlExporterNG2();
		ResourceBundle rb = ResourceBundle.getBundle("config");
		String inputFilePath = rb.getString("tempDirectory");

		HtmlSettings htmlSettings = new HtmlSettings();
		htmlSettings.setImageDirPath(inputFilePath + "_files");
		htmlSettings.setImageTargetUri(inputFilePath + "_files");
		OutputStream outputStream = res.getOutputStream();

		StreamResult result = new StreamResult(outputStream);
		exporter.html(template, result, htmlSettings);

		// (new SaveToZipFile(template)).save(res.getOutputStream());
	}

	public void toHTML(String inputPath, String path) throws IOException, Docx4JException, Exception {
		// File f = new
		// File(getRequest().getRealPath("/WEB-INF/template/download/")+fileName);
		// template.save(f);
		AbstractHtmlExporter exporter = new HtmlExporterNG2();
		WordprocessingMLPackage wmp = WordprocessingMLPackage.load(new FileInputStream(inputPath));
		String inputFilePath = path;

		HtmlSettings htmlSettings = new HtmlSettings();
		htmlSettings.setImageDirPath(inputFilePath + "_files");
		htmlSettings.setImageTargetUri(inputFilePath + "_files");
		OutputStream outputStream = new java.io.FileOutputStream(inputFilePath + ".html");

		StreamResult result = new StreamResult(outputStream);
		exporter.html(wmp, result, htmlSettings);

		// (new SaveToZipFile(template)).save(res.getOutputStream());
	}

	public void htmlToDocx(String html, String docXPath) throws InvalidFormatException, JAXBException, Docx4JException {
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();

		NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
		wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
		ndp.unmarshalDefaultNumbering();
		// linhdx
		// wordMLPackage.getMainDocumentPart().getContent()
		// .addAll(XHTMLImporter.convert(html, wordMLPackage));

		// System.out.println(XmlUtils.marshaltoString(wordMLPackage
		// .getMainDocumentPart().getJaxbElement(), true, true));
		wordMLPackage.save(new java.io.File(docXPath));
	}

	// private PdfDocxFile convert(PdfDocxFile input) {
	// Converter_Service service = new Converter_Service();
	// com.viettel.ws.client.Converter port = service.getConverterPort();
	// return port.convert(input);
	// }
	private Relationship createFooterPart() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * Inner class to add a header and a footer.
	 */
	class HeaderFooter extends PdfPageEventHelper {

		/**
		 * Alternating phrase for the header.
		 */
		Phrase[] header = new Phrase[2];
		byte[] imageBarCode;
		/**
		 * Current page number (will be reset for every chapter).
		 */
		int pagenumber;

		private HeaderFooter(String fileCode, byte[] barCode) {
			header[1] = new Phrase(fileCode);
			imageBarCode = barCode;
		}

		/**
		 * Initialize one of the headers.
		 *
		 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
		 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
		 */
		public void onOpenDocument(PdfWriter writer, Document document) {
			// header[1] = new Phrase("Movie history");
		}

		/**
		 * Initialize one of the headers, based on the chapter title; reset the
		 * page number.
		 *
		 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onChapter(
		 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document,
		 *      float, com.itextpdf.text.Paragraph)
		 */
		public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) {
			header[1] = new Phrase(title.getContent());
			pagenumber = 1;
		}

		/**
		 * Increase the page number.
		 *
		 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onStartPage(
		 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
		 */
		public void onStartPage(PdfWriter writer, Document document) {
			pagenumber++;
		}

		/**
		 * Adds the header and the footer.
		 *
		 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
		 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
		 */
		public void onEndPage(PdfWriter writer, Document document) {

			Rectangle rect = writer.getBoxSize("art");
			/*
			 * switch(writer.getPageNumber()) { case 0:
			 * ColumnText.showTextAligned(writer.getDirectContent(),
			 * Element.ALIGN_RIGHT, header[0], rect.getRight(), rect.getTop(),
			 * 0); break; case 1:
			 * ColumnText.showTextAligned(writer.getDirectContent(),
			 * Element.ALIGN_LEFT, header[1], rect.getLeft(), rect.getBottom() -
			 * 18, 0); break; }
			 * 
			 * ColumnText.showTextAligned(writer.getDirectContent(),
			 * Element.ALIGN_CENTER, new Phrase(String.format("Trang %d",
			 * pagenumber)), (rect.getLeft() + rect.getRight()) / 2,
			 * rect.getBottom() - 18, 0);
			 */

			ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, header[1], rect.getLeft(),
					rect.getBottom() - 18, 0);

			ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
					new Phrase(String.format("Trang %d", pagenumber)), rect.getRight() - 25, rect.getBottom() - 18, 0);

			try {
				// Add Image
				if (imageBarCode != null && imageBarCode.length > 0) {
					PdfPTable foot = new PdfPTable(1);
					// Rectangle page = document.getPageSize();
					Image jpg = Image.getInstance(imageBarCode);
					jpg.setAlignment(Image.ALIGN_CENTER);
					jpg.scaleAbsolute(60, 60);
					jpg.setBorder(0);
					jpg.setWidthPercentage(200);

					foot.addCell(jpg);
					foot.setTotalWidth(60);
					foot.writeSelectedRows(0, -1, 0, document.getPageSize().getHeight(), writer.getDirectContent());
					/*
					 * PdfPCell cell = new PdfPCell();
					 * cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					 * cell.setBorder(0); foot.addCell(cell);
					 * 
					 * foot.addCell("This place is to write your footer text ");
					 * foot.setTotalWidth(page.getWidth() -
					 * document.leftMargin() - document.rightMargin());
					 * 
					 * foot.writeSelectedRows(0, -1, document.leftMargin(),
					 * document.bottomMargin(), writer.getDirectContent());
					 */
				}
			} catch (BadElementException | IOException ex) {
				Logger.getLogger(WordExportUtils.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}

	public static void resolveFragmentText(WordprocessingMLPackage wmp) throws Docx4JException {
		List<Object> rs = getAllElementFromObject(wmp.getMainDocumentPart(), P.class);
		for (Object r : rs) {
			P run = (P) r;
			List<Object> texts = getAllElementFromObject(run, Text.class);
			int i = 0;
			while (i < texts.size()) {
				Object text = texts.get(i);
				i++;
				try {
					Text textElement = (Text) text;
					String content = textElement.getValue();
					if (content != null) {
						int start = content.indexOf("${");
						if (start < 0) {
							//
							// Khong co dau ngoac trong nay nen bo qua
							//
							continue;
						}
						int end = content.indexOf("}", start);
						if (end >= 0) {
							//
							// co du bo ngoac trong text nay, bo qua
							//
							continue;
						}
						do {
							Text nextText = (Text) texts.get(i);
							i++;
							content = content + nextText.getValue();
							nextText.setValue("");
							// run.getContent().remove(texts.get(i));
							if (content.indexOf("}", start) > 0) {
								textElement.setValue(content);
								break;
							}
						} while (true);
					} else {
						System.out.println("null");
					}
				} catch (NullPointerException en) {
					LogUtils.addLog(en);
				}
			}
		}
	}
}
