package org.icube.hyrsmart.admin;

@SuppressWarnings("serial")
public class SuperAdminView extends SuperAdmin {

	public SuperAdminView() {
		super();
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
	
	
}
