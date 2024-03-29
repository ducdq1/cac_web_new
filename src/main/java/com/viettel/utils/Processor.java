/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.utils;
import java.util.List;
import java.util.Stack;

import javax.xml.bind.JAXBElement;

import org.docx4j.math.CTOMath;
import org.docx4j.math.CTOMathArg;
import org.docx4j.math.CTR;
import org.docx4j.math.CTSSup;
import org.docx4j.math.CTText;
import org.docx4j.wml.P;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.SdtBlock;
/**
 *
 * @author binhnt53
 */
public class Processor {
//    public static void main(String[] args) throws Exception
//    {
//        File inFile = new File(args[0]);
//        File outFile = new File(args[1]);
//
//        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(inFile);
//        MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
//
//        Processor processor = new Processor();
//        processor.processContent(mdp.getContent());
//
//        wordMLPackage.save(outFile);
//    }

    private Stack<String> tags = new Stack<String>();
    private void pushTag(String tag)
    {
        tags.push(tag);
    }
    private String getTag()
    {
        return tags.peek();
    }
    private void popTag()
    {
        tags.pop();
    }

    private static final org.docx4j.wml.ObjectFactory wmlFactory = new org.docx4j.wml.ObjectFactory();
    private static final org.docx4j.math.ObjectFactory mathFactory = new org.docx4j.math.ObjectFactory();

    public void processContent(List<Object> content)
    {
        for (Object child : content)
        {
            if (child instanceof SdtBlock)
            {
                processBlock((SdtBlock) child);
            }
            else if (child instanceof P)
            {
                processP((P) child);
            }
            else if (child instanceof JAXBElement)
            {
                JAXBElement<?> elem = (JAXBElement<?>) child;
                Class<?> elemType = elem.getDeclaredType();
                if (elemType.equals(CTOMath.class))
                {
                    processOMath((CTOMath) elem.getValue());
                }
            }
        }
    }

    public void processP(P p)
    {
        processContent(p.getContent());
    }

    public void processBlock(SdtBlock block)
    {
        String tag = block.getSdtPr().getTag().getVal();
        pushTag(tag);
        processContent(block.getSdtContent().getContent());
        popTag();
    }

    public void processOMath(CTOMath oMath)
    {
        if (("MyEquation").equals(getTag()))
        {
            List<Object> content = oMath.getEGOMathElements();
            content.clear();

            content.add(makeRun("A=\u03c0"));

            content.add(makeSSup(makeRun("r"), makeRun("2")));
        }
    }

    private JAXBElement<CTR> makeRun(String text)
    {
        // <m:r>
        CTR run = mathFactory.createCTR();
        List<Object> content = run.getContent();

        // <w:rPr><w:rFonts>
        RPr pr = wmlFactory.createRPr();
        RFonts rFonts = wmlFactory.createRFonts();
        rFonts.setAscii("Cambria Math");
        rFonts.setHAnsi("Cambria Math");
        pr.setRFonts(rFonts);
        content.add(wmlFactory.createSdtPrRPr(pr));

        // <m:t>
        CTText ctText = mathFactory.createCTText();
        ctText.setValue(text);
        content.add(mathFactory.createCTRTMath(ctText));

        return mathFactory.createCTOMathArgR(run);
    }

    private JAXBElement<CTSSup> makeSSup(Object expr, Object exp)
    {
        // <m:ssup>
        CTSSup ssup = mathFactory.createCTSSup();

        // <m:e>
        CTOMathArg eArg = mathFactory.createCTOMathArg();
        eArg.getEGOMathElements().add(expr);
        ssup.setE(eArg);

        // <m:sup>
        CTOMathArg supArg = mathFactory.createCTOMathArg();
        supArg.getEGOMathElements().add(exp);
        ssup.setSup(supArg);

        return mathFactory.createCTOMathArgSSup(ssup);
    }
}
