/*
 * Copyright (c) 2016 Washington State Department of Transportation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */

package gov.wa.wsdot.mobile.client.activities.tollrates;


import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;
import gov.wa.wsdot.mobile.client.widget.tabbar.I405TabBarButton;
import gov.wa.wsdot.mobile.client.widget.tabbar.SR167TabBarButton;
import gov.wa.wsdot.mobile.client.widget.tabbar.SR16TabBarButton;
import gov.wa.wsdot.mobile.client.widget.tabbar.SR520TabBarButton;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.aria.client.SelectedValue;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.scroll.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.tabbar.TabPanel;

import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;

public class TollRatesViewGwtImpl extends Composite implements TollRatesView {

	/**
	 * The UiBinder interface.
	 */	
	interface TollRatesViewGwtImplUiBinder extends
			UiBinder<Widget, TollRatesViewGwtImpl> {
	}
	
	/**
	 * The UiBinder used to generate the view.
	 */
	private static TollRatesViewGwtImplUiBinder uiBinder = GWT
			.create(TollRatesViewGwtImplUiBinder.class);

	@UiField
	HeaderTitle heading;
	
	@UiField
	BackImageButton backButton;
	
	@UiField
	FlexSpacer leftFlexSpacer;
	
	@UiField
	ScrollPanel sr520ScrollPanel;
	
	@UiField
	ScrollPanel sr16ScrollPanel;
	
	@UiField
	ScrollPanel sr167ScrollPanel;
	
	@UiField
	ScrollPanel i405ScrollPanel;
	
    @UiField
    SR520TabBarButton sr520Tab;
    
    @UiField
    SR16TabBarButton sr16Tab;
    
    @UiField
    SR167TabBarButton sr167Tab;
    
    @UiField
    I405TabBarButton i405Tab;
	
	
	@UiField
	TabPanel tabPanel;
	
	private Presenter presenter;

	public TollRatesViewGwtImpl() {
		
		initWidget(uiBinder.createAndBindUi(this));

		accessibilityPrepare();
		
		if (MGWT.getOsDetection().isAndroid()) {
            leftFlexSpacer.setVisible(false);
            sr520ScrollPanel.setBounce(false);
            sr16ScrollPanel.setBounce(false);
            sr167ScrollPanel.setBounce(false);
        }
	}

    @UiHandler("tabPanel")
    protected void onTabSelected(SelectionEvent<Integer> event) {
        if (presenter != null) {
            int index = event.getSelectedItem();
            presenter.onTabSelected(index);
        }
    }

	@UiHandler("backButton")
	protected void onBackButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onBackButtonPressed();
		}
	}	
	
    @UiHandler("sr520Tab")
    protected void on520TabPressed(TapEvent event) {
    		accessibilityShowSr520();
    }
    
    @UiHandler("sr16Tab")
    protected void on16TabPressed(TapEvent event) {
    		accessibilityShowSr16();
    }
    @UiHandler("sr167Tab")
    protected void on167TabPressed(TapEvent event) {
    		accessibilityShowSr167();
    }
    
    @UiHandler("i405Tab")
    protected void on405TabPressed(TapEvent event) {
    		accessibilityShowI405();
    }
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

    @Override
    public void refresh() {
        sr520ScrollPanel.refresh();
        sr16ScrollPanel.refresh();
        sr167ScrollPanel.refresh();
        tabPanel.tabContainer.refresh();
    }
    
	private void accessibilityShowSr520(){
		Roles.getMainRole().setAriaHiddenState(sr520ScrollPanel.getElement(), false);
		Roles.getMainRole().setAriaHiddenState(sr16ScrollPanel.getElement(), true);
		Roles.getMainRole().setAriaHiddenState(sr167ScrollPanel.getElement(), true);
		Roles.getMainRole().setAriaHiddenState(i405ScrollPanel.getElement(), true);
		
		Roles.getTabRole().setAriaSelectedState(sr520Tab.getElement(), SelectedValue.TRUE);
		Roles.getTabRole().setAriaSelectedState(sr16Tab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaSelectedState(sr167Tab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaSelectedState(i405Tab.getElement(), SelectedValue.FALSE);
	}
	
	private void accessibilityShowSr16(){
		Roles.getMainRole().setAriaHiddenState(sr520ScrollPanel.getElement(), true);
		Roles.getMainRole().setAriaHiddenState(sr16ScrollPanel.getElement(), false);
		Roles.getMainRole().setAriaHiddenState(sr167ScrollPanel.getElement(), true);
		Roles.getMainRole().setAriaHiddenState(i405ScrollPanel.getElement(), true);
		
		Roles.getTabRole().setAriaSelectedState(sr520Tab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaSelectedState(sr16Tab.getElement(), SelectedValue.TRUE);
		Roles.getTabRole().setAriaSelectedState(sr167Tab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaSelectedState(i405Tab.getElement(), SelectedValue.FALSE);
	}
	
	private void accessibilityShowSr167(){
		Roles.getMainRole().setAriaHiddenState(sr520ScrollPanel.getElement(), true);
		Roles.getMainRole().setAriaHiddenState(sr16ScrollPanel.getElement(), true);
		Roles.getMainRole().setAriaHiddenState(sr167ScrollPanel.getElement(), false);
		Roles.getMainRole().setAriaHiddenState(i405ScrollPanel.getElement(), true);
		
		Roles.getTabRole().setAriaSelectedState(sr520Tab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaSelectedState(sr16Tab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaSelectedState(sr167Tab.getElement(), SelectedValue.TRUE);
		Roles.getTabRole().setAriaSelectedState(i405Tab.getElement(), SelectedValue.FALSE);
	}
	
	private void accessibilityShowI405(){
		Roles.getMainRole().setAriaHiddenState(sr520ScrollPanel.getElement(), true);
		Roles.getMainRole().setAriaHiddenState(sr16ScrollPanel.getElement(), true);
		Roles.getMainRole().setAriaHiddenState(sr167ScrollPanel.getElement(), true);
		Roles.getMainRole().setAriaHiddenState(i405ScrollPanel.getElement(), false);
		
		Roles.getTabRole().setAriaSelectedState(sr520Tab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaSelectedState(sr16Tab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaSelectedState(sr167Tab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaSelectedState(i405Tab.getElement(), SelectedValue.TRUE);
	}
    
    private void accessibilityPrepare(){
		
		// Add ARIA roles for accessibility
		Roles.getButtonRole().set(backButton.getElement());
		Roles.getButtonRole().setAriaLabelProperty(backButton.getElement(), "back");
		
		Roles.getHeadingRole().set(heading.getElement());
		
		Roles.getMainRole().set(sr520ScrollPanel.getElement());
		Roles.getMainRole().set(sr16ScrollPanel.getElement());
		Roles.getMainRole().set(sr167ScrollPanel.getElement());
		Roles.getMainRole().set(i405ScrollPanel.getElement());
		
		Roles.getTabRole().set(sr520Tab.getElement());
		Roles.getTabRole().setAriaSelectedState(sr520Tab.getElement(), SelectedValue.TRUE);
		Roles.getTabRole().setAriaLabelProperty(sr520Tab.getElement(), "S R 520");
		
		Roles.getTabRole().set(sr16Tab.getElement());
		Roles.getTabRole().setAriaSelectedState(sr16Tab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaLabelProperty(sr16Tab.getElement(), "S R 16");
		
		Roles.getTabRole().set(sr167Tab.getElement());
		Roles.getTabRole().setAriaSelectedState(sr167Tab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaLabelProperty(sr167Tab.getElement(), "S R 167");
		
		Roles.getTabRole().set(i405Tab.getElement());
		Roles.getTabRole().setAriaSelectedState(i405Tab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaLabelProperty(i405Tab.getElement(), "I 4 0 5");
		
		accessibilityShowSr520();	
	}
}
