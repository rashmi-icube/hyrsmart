package org.icube.hyrsmart.login;

import java.net.InetAddress;

import org.icube.hyrsmart.java.login.LoginHelper;
import org.icube.hyrsmart.java.user.User;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class LoginView extends Login implements View {

	public LoginView(Navigator navigator) {
		btn_login.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				try {
					validateUser();
					navigator.navigateTo("summary");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}

	public void validateUser() {
		String userName = txt_username.getValue();
		String password = txt_password.getValue();
		InetAddress ipAddr = null;
		try {
			ipAddr = InetAddress.getLocalHost();
		} catch (Exception e) {
			org.apache.log4j.Logger.getLogger(LoginView.class).debug("Unable to get the IP");
		}

		LoginHelper lh = new LoginHelper();
		try {
			User u = lh.login(userName, password, ipAddr.toString());
		} catch (Exception e) {
			org.apache.log4j.Logger.getLogger(LoginView.class).debug("Invalid username and/or password!!!");
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
}
