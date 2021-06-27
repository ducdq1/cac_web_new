/*     */ package com.viettel.voffice.config;
/*     */
/*     */ import com.viettel.voffice.dbbo.DateTimeRule;
/*     */ import com.viettel.voffice.dbbo.LocationRule;
/*     */ import com.viettel.voffice.dbbo.ModRule;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */
/*     */ public class RuleSelector /*     */ {
    /*  23 */ private HashMap locations = new HashMap<String, Object>();
    /*     */
    /*  25 */ private HashMap mods = new HashMap<String, Object>();
    /*     */
    /*  27 */ private HashMap times = new HashMap<String, Object>();
    /*     */
    /*     */ public void addLocationRule(LocationRule location) /*     */ {
        /*  33 */ this.locations.put(location.getName().toLowerCase(), location);
        /*     */    }
    /*     */
    /*     */ public void addModRule(ModRule modRule) /*     */ {
        /*  40 */ this.mods.put(Integer.valueOf(modRule.getModValue()), modRule);
        /*     */    }
    /*     */
    /*     */ public void addDateTimeRule(DateTimeRule dateTimeRule) /*     */ {
        /*  47 */ this.times.put(dateTimeRule.getSessionName(), dateTimeRule);
        /*     */    }
    /*     */
    /*     */ public HashMap getLocations() {
        /*  51 */ return this.locations;
        /*     */    }
    /*     */
    /*     */ public void setLocations(HashMap locations) {
        /*  55 */ this.locations = locations;
        /*     */    }
    /*     */
    /*     */ public HashMap getMods() {
        /*  59 */ return this.mods;
        /*     */    }
    /*     */
    /*     */ public void setMods(HashMap mods) {
        /*  63 */ this.mods = mods;
        /*     */    }
    /*     */
    /*     */ public HashMap getTimes() {
        /*  67 */ return this.times;
        /*     */    }
    /*     */
    /*     */ public void setTimes(HashMap times) {
        /*  71 */ this.times = times;
        /*     */    }
    /*     */
    /*     */ public void checkRule() /*     */ {
        /*     */    }
    /*     */
    /*     */ public String getValueLocation(String key) /*     */ {
        /*  84 */ LocationRule tmp = (LocationRule) this.locations.get(key.toLowerCase());
        /*  85 */ return tmp.getSessionName();
        /*     */    }
    /*     */
    /*     */ public String getValueMod(String key) /*     */ {
        /*  93 */ ModRule tmp = (ModRule) this.mods.get(Integer.valueOf(Integer.parseInt(key)));
        /*  94 */ return tmp.getSessionName();
        /*     */    }
    /*     */
    /*     */ public String getValueDateTimeRule(Date d) /*     */ {
        /* 102 */ Collection cols = this.times.values();
        /* 103 */ Iterator iter = cols.iterator();
        /* 104 */ while (iter.hasNext()) {
            /* 105 */ DateTimeRule dateTimeRule = (DateTimeRule) iter.next();
            /* 106 */ if ((d.after(dateTimeRule.getStartDate())) && (d.before(dateTimeRule.getEndDate()))) {
                /* 107 */ return dateTimeRule.getSessionName();
                /*     */            }
            /*     */        }
        /* 110 */ return "";
        /*     */    }
    /*     */ }

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.database.config.RuleSelector
 * JD-Core Version:    0.6.2
 */