package org.icube.hyrsmart.admin;

import java.util.HashMap;
import java.util.Map;

import org.icube.hyrsmart.java.test.LabelHelper;
import org.icube.hyrsmart.java.test.TestHelper;
import org.icube.hyrsmart.java.user.User;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class TestView extends Test implements View {

	User u = null;
	TestHelper th = new TestHelper();
	public TestView(Navigator navigator) {
		btn_signout.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				navigator.navigateTo("");
			}
		});

		btn_startNewTest.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				tbs_test.getTab(vtly_welcome).setEnabled(false);
				tbs_test.getTab(vtly_registration).setEnabled(true);
				tbs_test.setSelectedTab(vtly_registration);
				btn_cancel.setEnabled(true);
				btn_continue.setEnabled(false);
				Map<Integer,String> cityMasterMap = new HashMap<>();
				dpn_city.clear();
				Map<Integer, String> languageMap = getLanguageMaster();
				int selectedLanguageId = getSelectedLangId(languageMap);
				cityMasterMap = th.getCityMaster(u.getCompanyId(),selectedLanguageId);
				for (int i : cityMasterMap.keySet()) {
					String key = cityMasterMap.get(i);
					dpn_city.addItem(key);
				}
				getPersonalInfoLabels(selectedLanguageId);
				dpn_selectlang.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						// TODO Auto-generated method stub
						dpn_city.setImmediate(true);
						int selectedLanguageId = getSelectedLangId(languageMap);
						dpn_city.clear();
						getPersonalInfoLabels(selectedLanguageId);
						Map<Integer,String> cityMasterMap = th.getCityMaster(u.getCompanyId(),selectedLanguageId);
						for (int i : cityMasterMap.keySet()) {
							String key = cityMasterMap.get(i);
							dpn_city.addItem(key);
						
						}
						
					}
				});
			}

			private void getPersonalInfoLabels(int selectedLanguageId) {
				LabelHelper lh = new LabelHelper();
				Map<String, String> labelMap = lh.getPersonalDetailsLabelMap(u.getCompanyId(), selectedLanguageId);
				lbl_personalinfo.setValue(labelMap.get("personal_information"));
				lbl_name.setValue(labelMap.get("name"));
				txt_firstname.setInputPrompt(labelMap.get("first_name"));
				txt_middlename.setInputPrompt(labelMap.get("middle_name"));
				txt_lastname.setInputPrompt(labelMap.get("last_name"));
				txt_mobile.setInputPrompt(labelMap.get("mobile"));
				txt_email.setInputPrompt(labelMap.get("email"));
				txt_degree.setInputPrompt(labelMap.get("degree"));
				txt_stream.setInputPrompt(labelMap.get("stream"));
				dpn_city.setCaption(labelMap.get("your_city"));
				lbl_email.setValue(labelMap.get("email"));
				lbl_mobile.setValue(labelMap.get("mobile"));
				lbl_degree.setValue(labelMap.get("degree"));
				lbl_stream.setValue(labelMap.get("stream"));
				lbl_city.setValue(labelMap.get("your_city"));
				lbl_aadharno.setValue(labelMap.get("aadhar_no"));
				btn_cancel.setCaption(labelMap.get("cancel"));
				btn_continue.setCaption(labelMap.get("continue"));
			}
		});
				
				btn_cancel.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						tbs_test.getTab(vtly_welcome).setEnabled(true);
						tbs_test.setSelectedTab(vtly_welcome);
						tbs_test.getTab(vtly_registration).setEnabled(false);
						
					}
				});
				

		opg_agreement.setImmediate(true);
		opg_agreement.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {

				if (event
						.getProperty()
						.getValue()
						.toString()
						.equalsIgnoreCase(
								"I have read and agree to the terms and conditions and wish to continue to take the test")) {
					btn_startNewTest.setEnabled(true);
					btn_signout.setEnabled(false);
				} else if (event
						.getProperty()
						.getValue()
						.toString()
						.equalsIgnoreCase(
								"I have read and disagree to the terms and conditions and do not wish to continue to take the test.")) {
					btn_startNewTest.setEnabled(false);
					btn_signout.setEnabled(true);
				} else {
					btn_startNewTest.setEnabled(false);
					btn_signout.setEnabled(true);
				}
			}
		});

	}

	@Override
	public void enter(ViewChangeEvent event) {
		
		u = (User) VaadinSession.getCurrent().getAttribute("currentUser");
		if (u != null) {
			String inst = th.getUserAgreement(u.getCompanyId(), 1);
			lbl_welcome.setCaptionAsHtml(true);
			lbl_welcome.setCaption(inst);
			tbs_test.getTab(vtly_registration).setEnabled(false);
			tbs_test.getTab(vtly_instructions).setEnabled(false);
			tbs_test.getTab(vtly_test).setEnabled(false);
			tbs_test.getTab(vtly_thankyou).setEnabled(false);
			btn_startNewTest.setEnabled(false);
			opg_agreement.clear();

			// fill the languages dropdown and select the default language as
			// English

			Map<Integer, String> languageMap = getLanguageMaster();
			dpn_selectlang.setNullSelectionAllowed(false);
			dpn_selectlang.setValue("English");

			dpn_selectlang
					.addValueChangeListener(new Property.ValueChangeListener() {

						@Override
						public void valueChange(ValueChangeEvent event) {
							int selectedLanguageId = getSelectedLangId(languageMap);
							String inst = th.getUserAgreement(u.getCompanyId(),
									selectedLanguageId);
							lbl_welcome.setCaptionAsHtml(true);
							lbl_welcome.setCaption(inst);

						}

					});

		}

	}
	
	private int getSelectedLangId(
			Map<Integer, String> languageMap) {
		String selectedLanguage = dpn_selectlang.getValue()
				.toString();
		int selectedLanguageId = 1;
		for (int i : languageMap.keySet()) {
			selectedLanguageId = (languageMap.get(i)
					.equalsIgnoreCase(selectedLanguage)) ? i
					: selectedLanguageId;
		}
		return selectedLanguageId;
	}

	private Map<Integer, String> getLanguageMaster() {
		Map<Integer, String> languageMap = th.getLanguageMaster(u
				.getCompanyId());
		for (int i : languageMap.keySet()) {
			String key = languageMap.get(i);
			dpn_selectlang.addItem(key);
		}
		return languageMap;
	}
}

// }
