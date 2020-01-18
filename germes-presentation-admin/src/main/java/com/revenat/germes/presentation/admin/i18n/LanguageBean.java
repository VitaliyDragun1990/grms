package com.revenat.germes.presentation.admin.i18n;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.Locale;

/**
 * @author Vitaliy Dragun
 */
@ManagedBean(name = "language")
@SessionScoped
public class LanguageBean {

    /**
     * Locale for the current user
     */
    private Locale locale;

    public Locale getLocale() {
        return locale;
    }

    public void setLanguage(final String lang) {
        locale = new Locale(lang);
    }

    @PostConstruct
    void init() {
        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }
}
