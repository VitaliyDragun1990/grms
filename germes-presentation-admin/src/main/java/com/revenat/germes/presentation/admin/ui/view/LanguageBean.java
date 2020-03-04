package com.revenat.germes.presentation.admin.ui.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Locale;

/**
 * CDI-managed bean to store user locale for i18n
 *
 * @author Vitaliy Dragun
 */
@Named("language")
@SessionScoped
public class LanguageBean implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LanguageBean.class);

    private static final long serialVersionUID = 1538715230418655991L;
    /**
     * Locale for the current user
     */
    private Locale locale;

    public Locale getLocale() {
        return locale;
    }

    public void setLanguage(final String lang) {
        locale = new Locale(lang);
        LOGGER.info("Set language:{}", lang);
    }

    @PostConstruct
    void init() {
        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }
}
