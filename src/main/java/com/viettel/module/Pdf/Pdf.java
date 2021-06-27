/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.module.Pdf;

/**
 *
 * @author hieptq
 */
public class Pdf {/*

    private byte[] pdfFile;
    private float lx = 0;
    private float ly = 0;
    private float rx = 0;
    private float ry = 0;

    *//**
     *
     * @param linkFileIn
     * @param linkFileOut
     * @param linkImage
     * @param typeSign
     * @throws IOException
     *//*
    public void insertImage(String linkFileIn, String linkFileOut, String linkImage, String typeSign) throws IOException {
        File data = new File(linkFileIn);
        try {
            pdfFile = read(data);
        } catch (IOException ex) {
            Logger.getLogger(Pdf.class.getName()).log(Level.SEVERE, null, ex);
        }
        int pageNumber = searchLocationToSign(pdfFile, null);
        try {
            if (typeSign.equals(Constants_CKS.TYPE_USER.LD)) {
                addSignatureImage(linkFileIn, linkFileOut, linkImage, lx + 60, ly + 15, Constants_CKS.TYPE_USER.LD);
            }
            if (typeSign.equals(Constants_CKS.TYPE_USER.VT)) {
                addSignatureImage(linkFileIn, linkFileOut, linkImage, lx, ly, Constants_CKS.TYPE_USER.VT);
            }
        } catch (DocumentException ex) {
          LogUtils.addLog(ex);
        }
    }

    *//**
     * Chen anh chu ki vao cong van
     *
     * @param linkFileIn
     * @param linkFileOut
     * @param linkImageSign
     * @param linkImageStamp
     * @throws IOException
     *//*
    public void insertImageAll(String linkFileIn, String linkFileOut, String linkImageSign, String linkImageStamp, String sToFind) throws IOException {
        File data = new File(linkFileIn);
        try {
            pdfFile = read(data);
        } catch (IOException ex) {
            Logger.getLogger(Pdf.class.getName()).log(Level.SEVERE, null, ex);
        }
        int pageNumber = searchLocationToSign(pdfFile, sToFind);
        try {
            addSignatureImageAll(linkFileIn, linkFileOut, linkImageSign, linkImageStamp, lx, ly, pageNumber);
        } catch (DocumentException ex) {
            Logger.getLogger(Pdf.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    *//**
     * Chen anh chu ki vao cong van
     *
     * @param inputFile
     * @param outputFile
     * @param imageFile
     * @param x
     * @param y
     * @param type
     * @throws IOException
     * @throws DocumentException
     *//*
    public void addSignatureImage(String inputFile, String outputFile, String imageFile, float x, float y, String type)
            throws IOException, DocumentException {
        PdfReader reader = new PdfReader(inputFile);
        FileOutputStream os = new FileOutputStream(outputFile);
        PdfStamper stamper = new PdfStamper(reader, os);
        PdfContentByte pdfContentByte = stamper.getOverContent(1);
        Image image = Image.getInstance(imageFile);
        image.setAbsolutePosition(x, y);
        if (type.equals(Constants_CKS.TYPE_USER.LD)) {
            image.scaleToFit(100, 100);
        }
        if (type.equals(Constants_CKS.TYPE_USER.VT)) {
            image.scaleToFit(100, 100);
        }
        pdfContentByte.addImage(image);
        stamper.close();
        os.close();
    }

    public void addSignatureImageAll(String inputFile, String outputFile, String imageFileSign, String imageFileStamp, float x, float y, int pageNum)
            throws IOException, DocumentException {
        PdfReader reader = new PdfReader(inputFile);
        FileOutputStream os = new FileOutputStream(outputFile);
        PdfStamper stamper = new PdfStamper(reader, os);
        PdfContentByte pdfContentByte = stamper.getOverContent(pageNum);
        Image imageSign = Image.getInstance(imageFileSign);
        Image imageStamp = Image.getInstance(imageFileStamp);
        imageSign.setAbsolutePosition(x + 60, y + 15);
        imageStamp.setAbsolutePosition(x, y);
        imageStamp.scaleToFit(100, 100);
        imageSign.scaleToFit(100, 100);
        pdfContentByte.addImage(imageSign);
        pdfContentByte.addImage(imageStamp);
        stamper.close();
        os.close();
    }

    public byte[] read(File file) throws IOException {
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if (ous != null) {
                    ous.close();
                }
            } catch (IOException e) {
            	LogUtils.addLog(e);
            }

            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
            	LogUtils.addLog(e);
            }
        }
        if (ous != null) {
            return ous.toByteArray();
        } else {
            return null;
        }
    }

    *//**
     * linhdx Sua ham them sToFind de tim xau se chen anh Mặc định là SI
     *
     * @param pdf
     * @param sToFind
     * @return
     * @throws IOException
     *//*

    public int searchLocationToSign(byte[] pdf, String sToFind) throws IOException {
        PdfReader reader = null;
        int pageNumberFound = 1;
        //PrintWriter out = new PrintWriter(new FileOutputStream(txt));
        String stringToFind = "<SI>";
        if (sToFind != null && !"".endsWith(sToFind)) {
            stringToFind = sToFind;
        }
        int step = 50;
        int repeat = 2000 / step;
        lx = 0;
        ly = 0;
        rx = step;
        ry = step;
        Rectangle rect = new Rectangle(lx, ly, rx, ry);
        TextExtractionStrategy strategy = null;
        RenderFilter filter = null;
        Boolean found = false;
        String content = "";
        // First Search
        while (!found && step > 0) {
            reader = new PdfReader(pdf);
            for (int y = 0; y < repeat; y++) {
                for (int x = 0; x < repeat; x++) {
                    filter = new RegionTextRenderFilter(rect);
                    for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                        strategy = new FilteredTextRenderListener(
                                new LocationTextExtractionStrategy(), filter);
                        content = PdfTextExtractor.getTextFromPage(reader, i, strategy);
                        if (!"".equals(content) && content.trim().contains(stringToFind)) {
                            //out.println(content.trim() + " | " + lx + " - " + ly + " - " + rx + " - " + ry);
                            pageNumberFound = i;
                            found = true;
                        }
                        if (found) {
                            break;
                        }
                    }
                    if (found) {
                        break;
                    }
                    lx += step;
                    rx += step;
                    rect = new Rectangle(lx, ly, rx, ry);
                }
                if (found) {
                    break;
                }
                lx = 0;
                rx = step;
                ly += step;
                ry += step;
            }
            reader.close();
            repeat = 2000 / step;
            step -= 10;
        }
        if (reader != null) {
            reader.close();
        }

        if (!found) {
            pageNumberFound = -1;
        }

        // Thu hep pham vi tim kiem 
        found = false;
        float tempValue = lx;
        lx += 10;
        reader = new PdfReader(pdf);
        while (lx <= rx) {
            rect = new Rectangle(lx, ly, rx, ry);
            filter = new RegionTextRenderFilter(rect);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                strategy = new FilteredTextRenderListener(
                        new LocationTextExtractionStrategy(), filter);
                content = PdfTextExtractor.getTextFromPage(reader, i, strategy);
                if (!"".equals(content) && content.trim().contains(stringToFind)) {
                    //out.println(content.trim() + " | " + lx + " - " + ly + " - " + rx + " - " + ry);
                    tempValue = lx;
                    found = true;
                }
            }
            lx += 10;
        }
        lx = tempValue;
        reader.close();

        found = false;
        tempValue = rx;
        rx -= 10;
        reader = new PdfReader(pdf);
        while (rx >= lx) {
            rect = new Rectangle(lx, ly, rx, ry);
            filter = new RegionTextRenderFilter(rect);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                strategy = new FilteredTextRenderListener(
                        new LocationTextExtractionStrategy(), filter);
                content = PdfTextExtractor.getTextFromPage(reader, i, strategy);
                if (!"".equals(content) && content.trim().contains(stringToFind)) {
                    //out.println(content.trim() + " | " + lx + " - " + ly + " - " + rx + " - " + ry);
                    tempValue = rx;
                    found = true;
                }
            }
            rx -= 10;
        }
        rx = tempValue;
        reader.close();

        found = false;
        tempValue = ly;
        ly += 10;
        reader = new PdfReader(pdf);
        while (ly <= ry) {
            rect = new Rectangle(lx, ly, rx, ry);
            filter = new RegionTextRenderFilter(rect);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                strategy = new FilteredTextRenderListener(
                        new LocationTextExtractionStrategy(), filter);
                content = PdfTextExtractor.getTextFromPage(reader, i, strategy);
                if (!"".equals(content) && content.trim().contains(stringToFind)) {
                    //out.println(content.trim() + " | " + lx + " - " + ly + " - " + rx + " - " + ry);
                    tempValue = ly;
                    found = true;
                }
            }
            ly += 10;
        }
        ly = tempValue;
        reader.close();

        found = false;
        tempValue = ry;
        ry -= 10;
        reader = new PdfReader(pdf);
        while (ry >= ly) {
            rect = new Rectangle(lx, ly, rx, ry);
            filter = new RegionTextRenderFilter(rect);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                strategy = new FilteredTextRenderListener(
                        new LocationTextExtractionStrategy(), filter);
                content = PdfTextExtractor.getTextFromPage(reader, i, strategy);
                if (!"".equals(content) && content.trim().contains(stringToFind)) {
                    //out.println(content.trim() + " | " + lx + " - " + ly + " - " + rx + " - " + ry);
                    tempValue = ry;
                    found = true;
                }
            }
            ry -= 10;
        }
        ry = tempValue;
        reader.close();
        return pageNumberFound;
    }

    //hieptq update 100615
    public String returnLocation(byte[] pdf, String sToFind) throws IOException {
        PdfReader reader = null;
        int pageNumberFound = 1;
        //PrintWriter out = new PrintWriter(new FileOutputStream(txt));
        String stringToFind = "<SI>";
        if (sToFind != null && !"".endsWith(sToFind)) {
            stringToFind = sToFind;
        }
        int step = 50;
        int repeat = 2000 / step;
        lx = 0;
        ly = 0;
        rx = step;
        ry = step;
        Rectangle rect = new Rectangle(lx, ly, rx, ry);
        TextExtractionStrategy strategy = null;
        RenderFilter filter = null;
        Boolean found = false;
        String content = "";
        // First Search
        while (!found && step > 0) {
            reader = new PdfReader(pdf);
            for (int y = 0; y < repeat; y++) {
                for (int x = 0; x < repeat; x++) {
                    filter = new RegionTextRenderFilter(rect);
                    for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                        strategy = new FilteredTextRenderListener(
                                new LocationTextExtractionStrategy(), filter);
                        content = PdfTextExtractor.getTextFromPage(reader, i, strategy);
                        if (!"".equals(content) && content.trim().contains(stringToFind)) {
                            //out.println(content.trim() + " | " + lx + " - " + ly + " - " + rx + " - " + ry);
                            pageNumberFound = i;
                            found = true;
                        }
                        if (found) {
                            break;
                        }
                    }
                    if (found) {
                        break;
                    }
                    lx += step;
                    rx += step;
                    rect = new Rectangle(lx, ly, rx, ry);
                }
                if (found) {
                    break;
                }
                lx = 0;
                rx = step;
                ly += step;
                ry += step;
            }
            reader.close();
            repeat = 2000 / step;
            step -= 10;
        }
        if (reader != null) {
            reader.close();
        }

        if (!found) {
            pageNumberFound = -1;
        }

        // Thu hep pham vi tim kiem 
        found = false;
        float tempValue = lx;
        lx += 10;
        reader = new PdfReader(pdf);
        while (lx <= rx) {
            rect = new Rectangle(lx, ly, rx, ry);
            filter = new RegionTextRenderFilter(rect);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                strategy = new FilteredTextRenderListener(
                        new LocationTextExtractionStrategy(), filter);
                content = PdfTextExtractor.getTextFromPage(reader, i, strategy);
                if (!"".equals(content) && content.trim().contains(stringToFind)) {
                    //out.println(content.trim() + " | " + lx + " - " + ly + " - " + rx + " - " + ry);
                    tempValue = lx;
                    found = true;
                }
            }
            lx += 10;
        }
        lx = tempValue;
        reader.close();

        found = false;
        tempValue = rx;
        rx -= 10;
        reader = new PdfReader(pdf);
        while (rx >= lx) {
            rect = new Rectangle(lx, ly, rx, ry);
            filter = new RegionTextRenderFilter(rect);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                strategy = new FilteredTextRenderListener(
                        new LocationTextExtractionStrategy(), filter);
                content = PdfTextExtractor.getTextFromPage(reader, i, strategy);
                if (!"".equals(content) && content.trim().contains(stringToFind)) {
                    //out.println(content.trim() + " | " + lx + " - " + ly + " - " + rx + " - " + ry);
                    tempValue = rx;
                    found = true;
                }
            }
            rx -= 10;
        }
        rx = tempValue;
        reader.close();

        found = false;
        tempValue = ly;
        ly += 10;
        reader = new PdfReader(pdf);
        while (ly <= ry) {
            rect = new Rectangle(lx, ly, rx, ry);
            filter = new RegionTextRenderFilter(rect);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                strategy = new FilteredTextRenderListener(
                        new LocationTextExtractionStrategy(), filter);
                content = PdfTextExtractor.getTextFromPage(reader, i, strategy);
                if (!"".equals(content) && content.trim().contains(stringToFind)) {
                    //out.println(content.trim() + " | " + lx + " - " + ly + " - " + rx + " - " + ry);
                    tempValue = ly;
                    found = true;
                }
            }
            ly += 10;
        }
        ly = tempValue;
        reader.close();

        found = false;
        tempValue = ry;
        ry -= 10;
        reader = new PdfReader(pdf);
        while (ry >= ly) {
            rect = new Rectangle(lx, ly, rx, ry);
            filter = new RegionTextRenderFilter(rect);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                strategy = new FilteredTextRenderListener(
                        new LocationTextExtractionStrategy(), filter);
                content = PdfTextExtractor.getTextFromPage(reader, i, strategy);
                if (!"".equals(content) && content.trim().contains(stringToFind)) {
                    //out.println(content.trim() + " | " + lx + " - " + ly + " - " + rx + " - " + ry);
                    tempValue = ry;
                    found = true;
                }
            }
            ry -= 10;
        }
        ry = tempValue;
        reader.close();
        Long lxNew = (long) lx;
        Long lyNew = (long) ly;
        return String.valueOf(pageNumberFound) + ";" + String.valueOf(lxNew) + ";" + String.valueOf(lyNew);
    }
    
       public void emptySignatureWithText(String src, String dest, String fieldname, Certificate[] chain, 
            int pageNumber, String imageFile, int lx, int ly, int width, int height,String text) throws IOException, DocumentException, GeneralSecurityException {
        BouncyCastleProvider providerBC = new BouncyCastleProvider();
        Security.addProvider(providerBC);
        PdfReader reader = new PdfReader(src);
        
        int numberOfPages = reader.getNumberOfPages();
        
        if (pageNumber < 1 || pageNumber > numberOfPages){
            pageNumber = 1;            
        }
        
        FileOutputStream os = new FileOutputStream(dest);
        PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0', null, true);
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setVisibleSignature(new Rectangle(lx, ly, lx + width,ly + height), pageNumber, fieldname);
        //appearance.setCertificate(chain[0]);
        appearance.setLayer2Text(text);
        appearance.setLocation("Hà Nội");
        appearance.setReason("Test");
        //Image image = Image.getInstance(imageFile);
        //appearance.setImage(image);
        ExternalSignatureContainer external = new ExternalBlankSignatureContainer(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
        MakeSignature.signExternalContainer(appearance, external, 8192);
        reader.close();
    }
    
*/}
