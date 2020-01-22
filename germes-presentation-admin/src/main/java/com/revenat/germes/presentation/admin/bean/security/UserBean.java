package com.revenat.germes.presentation.admin.bean.security;

import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

/**
 * CDI-managed bean to store user credentials for authentication
 *
 * @author Vitaliy Dragun
 */
@Named
@ViewScoped
@Getter
@Setter
public class UserBean implements Serializable {
    private static final long serialVersionUID = -1265336409381898282L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserBean.class);

    private String username;

    private String password;

    public void login() {
        final Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(getUsername(), getPassword());

        try {
            subject.login(token);

            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (UnknownAccountException e) {
            facesError("Unknown account");
            LOGGER.warn(e.getMessage(), e);
        } catch (IncorrectCredentialsException e) {
            facesError("Wrong password");
            LOGGER.warn(e.getMessage(), e);
        } catch (LockedAccountException e) {
            facesError("Locked account");
            LOGGER.warn(e.getMessage(), e);
        } catch (AuthenticationException | IOException e) {
            facesError("Unknown error: " + e.getMessage());
            LOGGER.warn(e.getMessage(), e);
        } finally {
            token.clear();
        }
    }

    private void facesError(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }
}
