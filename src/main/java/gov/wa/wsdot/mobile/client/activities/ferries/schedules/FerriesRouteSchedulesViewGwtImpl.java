/*
 * Copyright (c) 2015 Washington State Department of Transportation
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

package gov.wa.wsdot.mobile.client.activities.ferries.schedules;

import gov.wa.wsdot.mobile.client.css.AppBundle;
import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;
import gov.wa.wsdot.mobile.shared.FerriesRouteItem;

import java.util.List;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowHeader;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel.Pullhandler;
import com.googlecode.mgwt.ui.client.widget.progress.ProgressIndicator;

public class FerriesRouteSchedulesViewGwtImpl extends Composite implements
		FerriesRouteSchedulesView {
	
	final PhoneGap phoneGap = GWT.create(PhoneGap.class);
	
	/**
	 * The UiBinder interface.
	 */
	interface FerriesRouteSchedulesViewGwtImplUiBinder extends
			UiBinder<Widget, FerriesRouteSchedulesViewGwtImpl> {
	}

	/**
	 * The UiBinder used to generate the view.
	 */
	private static FerriesRouteSchedulesViewGwtImplUiBinder uiBinder = GWT
			.create(FerriesRouteSchedulesViewGwtImplUiBinder.class);	

	@UiField
	HeaderTitle heading;
	
	@UiField(provided = true)
	CellList<FerriesRouteItem> cellList;
	
	@UiField
	BackImageButton backButton;
	
	@UiField
	FlexSpacer leftFlexSpacer;
	
	@UiField(provided = true)
	PullPanel pullToRefresh;
	
	@UiField
	FlowPanel flowPanel;
	
	@UiField
	ProgressIndicator progressIndicator;
	
	private Presenter presenter;
	private PullArrowHeader pullArrowHeader;
	
	public FerriesRouteSchedulesViewGwtImpl() {
		
		pullToRefresh = new PullPanel();
		pullArrowHeader = new PullArrowHeader();
		pullToRefresh.setHeader(pullArrowHeader);
		
		cellList = new CellList<FerriesRouteItem>(
				new FerriesRouteSchedulesCell<FerriesRouteItem>() {

			private ImageResourceRenderer imageRenderer = new ImageResourceRenderer();
			
			@Override
			public String getDescription(FerriesRouteItem model) {
				return model.getDescription();
			}

			@Override
			public String getLastUpdated(FerriesRouteItem model) {
				return ParserUtils.relativeTime(model.getCacheDate(),
						"MMMM d, yyyy h:mm a", false);
			}

			@Override
			public SafeHtml getAlertImage(FerriesRouteItem model) {
				boolean hasAlerts = false;
				
				if (!model.getRouteAlert().equals("[]")) hasAlerts = true;
				SafeHtml image = imageRenderer.render(AppBundle.INSTANCE.btnAlertPNG());
				
				return hasAlerts ? image : SafeHtmlUtils.fromString("");
			}

            @Override
            public String getCrossingTime(FerriesRouteItem model) {
                try {
                    if (model.getCrossingTime().equalsIgnoreCase("null")) {
                        return "";
                    } else {
                        return "Crossing Time: ~ " + model.getCrossingTime() + " min";
                    }
                } catch (Exception e) {
                    return "";
                }
            }
		});
		
		initWidget(uiBinder.createAndBindUi(this));

		accessibilityPrepare();
		
		if (MGWT.getOsDetection().isAndroid()) {
            leftFlexSpacer.setVisible(false);
        }
	}

	@UiHandler("cellList")
	protected void onCellSelected(CellSelectedEvent event) {
		if (presenter != null) {
			int index = event.getIndex();
			presenter.onItemSelected(index);
		}
	}

	@UiHandler("backButton")
	protected void onBackButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onBackButtonPressed();
		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void render(List<FerriesRouteItem> createTopicsList) {
		cellList.render(createTopicsList);
	}

	@Override
	public void setSelected(int lastIndex, boolean b) {
		cellList.setSelectedIndex(lastIndex, b);
	}
	
	@Override
	public void showProgressIndicator() {
		progressIndicator.setVisible(true);
	}

	@Override
	public void hideProgressIndicator() {
		progressIndicator.setVisible(false);
	}

	@Override
	public void refresh() {
		pullToRefresh.refresh();
	}
	
	@Override
	public void setHeaderPullHandler(Pullhandler pullHandler) {
		pullToRefresh.setHeaderPullHandler(pullHandler);
	}
	
	@Override
	public PullArrowWidget getPullHeader() {
		return pullArrowHeader;
	}

	@Override
	public HasRefresh getPullPanel() {
		return pullToRefresh;
	}
	
	private void accessibilityPrepare(){
		
		// Add ARIA roles for accessibility
		Roles.getButtonRole().set(backButton.getElement());
		Roles.getButtonRole().setAriaLabelProperty(backButton.getElement(), "back");
		
		Roles.getHeadingRole().set(heading.getElement());

		Roles.getProgressbarRole().set(progressIndicator.getElement());
		Roles.getProgressbarRole().setAriaLabelProperty(progressIndicator.getElement(), "loading indicator");

		// TODO Hide pull down until we can figure out how to get VoiceOver to work with it
		Roles.getButtonRole().setAriaHiddenState(pullArrowHeader.getElement(), true);
	
	}

}
