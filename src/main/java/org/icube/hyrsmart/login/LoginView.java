package org.icube.hyrsmart.login;

import java.net.InetAddress;

import org.icube.hyrsmart.java.login.LoginHelper;
import org.icube.hyrsmart.java.user.User;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class LoginView extends Login implements View {
	private User USER = null;

	public LoginView(Navigator navigator) {
		btn_login.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					validateUser();
					if (USER != null && USER.getRoleId() != 3) {
						navigator.navigateTo("summary");
					} else{
						// TODO navigate to test directly 
					}
				} catch (Exception e) {
					org.apache.log4j.Logger.getLogger(LoginView.class).debug("Invalid username and/or password", e);
				}

			}
		});
	}

	public User validateUser() {
		String userName = txt_username.getValue();
		String password = txt_password.getValue();
		InetAddress ipAddr = null;
		try {
			ipAddr = InetAddress.getLocalHost();
		} catch (Exception e) {
			org.apache.log4j.Logger.getLogger(LoginView.class).debug("Unable to get the IP");
		}

		try {
			LoginHelper lh = new LoginHelper();
			USER = lh.login(userName, password, ipAddr.toString());
			VaadinSession.getCurrent().setAttribute("currentUser", USER);
		} catch (Exception e) {
			org.apache.log4j.Logger.getLogger(LoginView.class).debug("Invalid username and/or password!!!");
		}
		return USER;
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}
