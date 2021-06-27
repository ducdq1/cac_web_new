/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;

import org.docx4j.Docx4J;
import org.docx4j.Docx4jProperties;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.convert.out.html.AbstractHtmlExporter;
import org.docx4j.convert.out.html.AbstractHtmlExporter.HtmlSettings;
import org.docx4j.convert.out.html.HtmlExporterNG2;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.samples.AbstractSample;

/**
 *
 * @author giangpn
 */
public class ConvertFileUtils extends AbstractSample {

	public static String docxToHtml(String inputfilepath) throws Docx4JException, Exception {
		InputStream is = new FileInputStream(new File(inputfilepath));
		WordprocessingMLPackage wordMLPackage = Docx4J.load(is);
		OutputStream os = new ByteArrayOutputStream();
		Docx4jProperties.setProperty("docx4j.Log4j.Configurator.disabled", true);
		Docx4jProperties.setProperty("docx4j.Convert.Out.HTML.OutputMethodXML", false);
		Docx4J.toHTML(getHtmlSetting(wordMLPackage, inputfilepath), os, Docx4J.FLAG_EXPORT_PREFER_NONXSL);
		os.flush();
		String result = ((ByteArrayOutputStream) os).toString();

		return result;
	}

	public static void docxToHtmlFile(String inputString, String fileName) {
		try {
			InputStream is = new FileInputStream(new File(inputString));
			String folderPath = inputString.substring(0, inputString.lastIndexOf("\\"));
			WordprocessingMLPackage wordMLPackage = Docx4J.load(is);
			OutputStream out = new FileOutputStream(new File(folderPath + "\\" + fileName));
			Docx4J.toHTML(getHtmlSetting(wordMLPackage, inputString), out, Docx4J.FLAG_EXPORT_PREFER_NONXSL);

		} catch (Docx4JException | IOException e) {
			LogUtils.addLog(e);
		}
	}

	public static void deleteFile(String oldFilePath) {
		File oldFile = new File(oldFilePath);

		// Make sure the file or directory exists and isn't write protected
		if (!oldFile.exists()) {
			throw new IllegalArgumentException("Delete: no such file or directory: " + oldFilePath);
		}

		if (!oldFile.canWrite()) {
			throw new IllegalArgumentException("Delete: write protected: " + oldFilePath);
		}

		// If it is a directory, make sure it is empty
		if (oldFile.isDirectory()) {
			String[] files = oldFile.list();
			if (files.length > 0) {
				throw new IllegalArgumentException("Delete: directory not empty: " + oldFilePath);
			}
		}

		// Attempt to delete it
		boolean success = oldFile.delete();

		if (!success) {
			throw new IllegalArgumentException("Delete: deletion failed");
		}
	}

	private static HTMLSettings getHtmlSetting(WordprocessingMLPackage wordMLPackage, String inputPath) {
		HTMLSettings htmlSettings = Docx4J.createHTMLSettings();
		htmlSettings.setImageDirPath(inputPath + "_files");
		htmlSettings
				.setImageTargetUri("/Share/upload/" + inputPath.substring(inputPath.lastIndexOf("\\") + 1) + "_files");
		htmlSettings.setWmlPackage(wordMLPackage);

		return htmlSettings;
	}

	public static String fileToString(String filePath) {
		return fileToString(new File(filePath));
	}

	public static String fileToString(File file) {
		byte[] fileBytes = new byte[0];
		try {
			byte[] buffer = new byte[4096];
			ByteArrayOutputStream outs = new ByteArrayOutputStream();
			InputStream ins = new FileInputStream(file);

			int read ;
			while ((read = ins.read(buffer)) != -1) {
				outs.write(buffer, 0, read);
			}

			ins.close();
			outs.close();
			fileBytes = outs.toByteArray();

		} catch (IOException e) {
			LogUtils.addLog(e);
		}

		return new String(fileBytes);
	}

	public static String docxToString(String inputPath) throws Docx4JException, Exception {
		WordprocessingMLPackage docx = WordprocessingMLPackage.load(new File(inputPath));
		AbstractHtmlExporter exporter = new HtmlExporterNG2();
		// Use file system, so there is somewhere to save images (if any)
		OutputStream os = new ByteArrayOutputStream();
		HtmlSettings htmlSettings = new HtmlSettings();
		htmlSettings.setImageDirPath(inputPath + "_files");
		htmlSettings
				.setImageTargetUri("/Share/upload/" + inputPath.substring(inputPath.lastIndexOf("\\") + 1) + "_files");
		javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(os);
		exporter.html(docx, result, htmlSettings);
		// Now after all that, we have XHTML we can convert
		// docx.getMainDocumentPart().getContent().addAll(xHTMLImporter.convert(
		// new File(inputfilepath + ".html"), null) );

		return ((ByteArrayOutputStream) os).toString();
	}

	public static void htmlToDocx(String html, String docXPath)
			throws InvalidFormatException, JAXBException, Docx4JException {
		// WordprocessingMLPackage wordMLPackage =
		// WordprocessingMLPackage.createPackage();
		//
		// NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
		// wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
		// ndp.unmarshalDefaultNumbering();
		//
		// wordMLPackage.getMainDocumentPart().getContent()
		// .addAll(XHTMLImporter.convert(html, null, wordMLPackage));
		//
		// System.out.println(XmlUtils.marshaltoString(wordMLPackage
		// .getMainDocumentPart().getJaxbElement(), true, true));
		//
		// wordMLPackage.save(new java.io.File(docXPath));
	}

}
