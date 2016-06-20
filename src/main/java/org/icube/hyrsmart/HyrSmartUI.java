package org.icube.hyrsmart;

import javax.servlet.annotation.WebServlet;

import org.icube.hyrsmart.admin.SuperAdminView;
import org.icube.hyrsmart.admin.TestView;
import org.icube.hyrsmart.login.LoginView;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("hyrsmart")
public class HyrSmartUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = HyrSmartUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		ComponentContainer myViewContainer = new AbsoluteLayout();
		this.setContent(myViewContainer);

		Navigator navigator = new Navigator(this, myViewContainer);
		navigator.addView("", new LoginView(navigator));
		navigator.addView("summary", new SuperAdminView(navigator));
		navigator.addView("testWelcome", new TestView(navigator));
	}

}