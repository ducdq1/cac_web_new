package com.viettel.voffice.config;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import com.viettel.utils.LogUtils;
import com.viettel.voffice.dbbo.DateTimeRule;
import com.viettel.voffice.dbbo.LocationRule;
import com.viettel.voffice.dbbo.ModRule;

public class Config {

    private HashMap<String, String> sessions = new HashMap<String, String>();
    private RuleSelector ruleSelector = new RuleSelector();

    public void loadInstance(String cfFile)
            throws IOException, SAXException {
        try {
            Digester digester = new Digester();
            digester.setValidating(false);
            digester.push(this);

            digester.addObjectCreate("ConfigDBC/Session-factory", SessionFactoryElement.class);

            digester.addBeanPropertySetter("ConfigDBC/Session-factory/name", "name");
            digester.addBeanPropertySetter("ConfigDBC/Session-factory/url", "url");
            digester.addSetNext("ConfigDBC/Session-factory", "addSessionFactory");

//            digester.addObjectCreate("ConfigDBC/RuleSellector/Locations/location", LocationRule.class);
//
//            digester.addBeanPropertySetter("ConfigDBC/RuleSellector/Locations/location/name", "name");
//            digester.addBeanPropertySetter("ConfigDBC/RuleSellector/Locations/location/sesion-name", "session_name");
//            digester.addSetNext("ConfigDBC/RuleSellector/Locations/location", "addLocationRule");
//
//            digester.addObjectCreate("ConfigDBC/RuleSellector/Mods/Mod", ModRule.class);
//            digester.addSetProperties("ConfigDBC/RuleSellector/Mods/Mod");
//            digester.addBeanPropertySetter("ConfigDBC/RuleSellector/Mods/Mod/mod-value", "mod_value");
//            digester.addBeanPropertySetter("ConfigDBC/RuleSellector/Mods/Mod/session-name", "session_name");
//            digester.addSetNext("ConfigDBC/RuleSellector/Mods/Mod", "addModRule");
//
//            digester.addObjectCreate("ConfigDBC/RuleSellector/Date-times/Date-time", DateTimeRule.class);
//            digester.addCallMethod("ConfigDBC/RuleSellector/Date-times/Date-time/Start", "setStartDate", 0);
//            digester.addCallMethod("ConfigDBC/RuleSellector/Date-times/Date-time/End", "setEndDate", 0);
//            digester.addBeanPropertySetter("ConfigDBC/RuleSellector/Date-times/Date-time/session-name", "session_name");
//            digester.addSetNext("ConfigDBC/RuleSellector/Date-times/Date-time", "addDateTimeRule");
            URL file = Thread.currentThread().getContextClassLoader().getResource(cfFile);
            File inputFile = new File(URLDecoder.decode(file.getPath()));
            if (!inputFile.exists()) {
                //System.out.println("Tuancx: Khong tim thay file " + file.getPath());
                LogUtils.addLog("Khong tim thay file " + file.getPath());
            }

            digester.parse(Config.class.getResourceAsStream(cfFile));
        } catch (IOException e) {
            LogUtils.addLog(e);
            throw e;
        }
    }

    public void addSessionFactory(SessionFactoryElement sessionFactory) {
        this.sessions.put(sessionFactory.getName().toLowerCase(), sessionFactory.getUrl());
    }

    public void addLocationRule(LocationRule loR) {
        this.ruleSelector.addLocationRule(loR);
    }

    public void addModRule(ModRule loR) {
        this.ruleSelector.addModRule(loR);
    }

    public void addDateTimeRule(DateTimeRule loR) {
        this.ruleSelector.addDateTimeRule(loR);
    }

    public HashMap getSessions() {
        return this.sessions;
    }

    public void setSessions(HashMap sessions) {
        this.sessions = sessions;
    }

    public RuleSelector getRuleSelector() {
        return this.ruleSelector;
    }
}

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.database.config.Config
 * JD-Core Version:    0.6.2
 */