package org.icube.hyrsmart.admin;

import java.util.List;

import org.icube.hyrsmart.java.test.SummaryHelper;
import org.icube.hyrsmart.java.test.TestCount;
import org.icube.hyrsmart.java.user.User;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class SuperAdminView extends SuperAdmin implements View {
	User u = null;

	public SuperAdminView(Navigator navigator) {
		super();
		//navigator.addView("adminview", (View) vlyt_view.getUI());

	}

	public void showSummaryTable(boolean show) {
		grd_summary.setVisible(show);
	}

	public void addSummaryTableHeader() {
		grd_summary.addColumn("ID");
		grd_summary.addColumn("ALLOWED");
		grd_summary.addColumn("COMPLETED");
		grd_summary.addColumn("EXITED");
		grd_summary.addColumn("LIVE");
		grd_summary.addColumn("BALANCE");
	}

	@Override
	public void enter(ViewChangeEvent event) {
		u = (User) VaadinSession.getCurrent().getAttribute("currentUser");

		if (u != null) {
			SummaryHelper sh = new SummaryHelper();
			TestCount tc = sh.getTotalTestListCount(u.getCompanyId(), u.getCompanyId());
			txt_allowed_count.setValue(String.valueOf(tc.getAllowed()));
			txt_balance_count.setValue(String.valueOf(tc.getBalance()));
			txt_completed_count.setValue(String.valueOf(tc.getCompleted()));
			txt_exited_count.setValue(String.valueOf(tc.getExited()));
			txt_live_count.setValue(String.valueOf(tc.getLive()));

			txt_allowed_count.setReadOnly(true);
			txt_balance_count.setReadOnly(true);
			txt_completed_count.setReadOnly(true);
			txt_exited_count.setReadOnly(true);
			txt_live_count.setReadOnly(true);

			if (u.getRoleId() == 1) {
				showSummaryTable(true);
				List<TestCount> tcList = sh.getTestCountList(u.getCompanyId(), u.getUserId());
				if (tcList.size() > 0) {
					addSummaryTableHeader();
				}
				for (TestCount tc1 : tcList) {
					User uObj = new User();
					User subAdmin = uObj.getUser(u.getCompanyId(), tc1.getUserId());
					
					Button btn_sub_admin = new Button();
					btn_sub_admin.setCaption(subAdmin.getDisplayName());
					btn_sub_admin.addClickListener(new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							try {
								 
								
							} catch (Exception e) {
								org.apache.log4j.Logger.getLogger(SuperAdminView.class).debug("Cannot go to the view tab for " + subAdmin.getDisplayName(), e);
							}

						}
					});
				
					
					grd_summary.addRow(subAdmin.getDisplayName(), String.valueOf(tc1.getAllowed()), String.valueOf(tc1.getCompleted()), String
							.valueOf(tc1.getExited()), String.valueOf(tc1.getLive()), String.valueOf(tc1.getBalance()));
				}

			} else {
				showSummaryTable(false);
			}
		}
	}

}
