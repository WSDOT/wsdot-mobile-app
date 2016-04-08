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

package gov.wa.wsdot.mobile.client.activities.trafficmap.menu.traveltimes;

import gov.wa.wsdot.mobile.client.css.AppBundle;
import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;
import gov.wa.wsdot.mobile.shared.TravelTimesItem;

import java.util.List;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.button.Button;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.input.search.MSearchBox;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FixedSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowHeader;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel.Pullhandler;
import com.googlecode.mgwt.ui.client.widget.progress.ProgressIndicator;

public class TravelTimesViewGwtImpl extends Composite implements
		TravelTimesView {

	/**
	 * The UiBinder interface.
	 */	
	interface TravelTimesViewGwtImplUiBinder extends
			UiBinder<Widget, TravelTimesViewGwtImpl> {
	}
	
	/**
	 * The UiBinder used to generate the view.
	 */
	private static TravelTimesViewGwtImplUiBinder uiBinder = GWT
			.create(TravelTimesViewGwtImplUiBinder.class);
	
	@UiField
	HeaderTitle heading;
	
	@UiField(provided = true)
	CellList<TravelTimesItem> cellList;
	
	@UiField
	BackImageButton backButton;
	
	@UiField
	FlexSpacer leftFlexSpacer;
	
	@UiField(provided = true)
	PullPanel pullToRefresh;
	
	@UiField
	FlowPanel flowPanel;
	
	@UiField(provided = true)
	MSearchBox searchBox;
	
	@UiField
	ProgressIndicator progressIndicator;
	
	private Presenter presenter;
	private PullArrowHeader pullArrowHeader;
	
	public TravelTimesViewGwtImpl() {
		
		pullToRefresh = new PullPanel();
		pullArrowHeader = new PullArrowHeader();
		pullToRefresh.setHeader(pullArrowHeader);
		
		cellList = new CellList<TravelTimesItem>(
				new TravelTimesCell<TravelTimesItem>() {

			@Override
			public String getDisplayTitle(TravelTimesItem model) {
				return model.getTitle();
			}

			@Override
			public String getDisplayDistanceAverageTime(TravelTimesItem model) {
				int average = model.getAverageTime();
				String averageTime;
				
				if (average == 0) {
					averageTime = "Not Available";
				} else {
					averageTime = average + " min";
				}
				
				return model.getDistance() + " miles / " + averageTime;
			}

			@Override
			public String getDisplayLastUpdated(TravelTimesItem model) {
				return ParserUtils.relativeTime(model.getUpdated(), "yyyy-MM-dd h:mm a", false);
			}

			@Override
			public String getDisplayCurrentTime(TravelTimesItem model) {
				return model.getCurrentTime() + " min";
			}

			@Override
			public String getDisplayCurrentTimeColor(TravelTimesItem model) {
				int average = model.getAverageTime();
				int current = model.getCurrentTime();
				String textColor;
				
	        	if (current < average) {
	        		textColor = AppBundle.INSTANCE.css().colorGreen();
	        	} else if ((current > average) && (average != 0)) {
	        		textColor = AppBundle.INSTANCE.css().colorRed();
	        	} else {
	        		textColor = AppBundle.INSTANCE.css().colorBlack();
	        	}
	        	
	        	return textColor;
			}
			
		});
		
		searchBox = new MSearchBox();
		searchBox.setPlaceHolder("Search Travel Times");
		searchBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (presenter != null) {
					presenter.onSearchTextChanged("%" + searchBox.getText() + "%");
				}
				
			}
		});
		
		initWidget(uiBinder.createAndBindUi(this));
        
		accessibilityPrepare();
		
		if (MGWT.getOsDetection().isAndroid()) {
            leftFlexSpacer.setVisible(false);
        }		
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@UiHandler("backButton")
	protected void onBackButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onDoneButtonPressed();
		}
	}
	
	@UiHandler("cellList")
	protected void onTravelTimeCellSelected(CellSelectedEvent event) {
		if (presenter != null) {
			int index = event.getIndex();
			presenter.onTravelTimeSelected(index);
		}
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
	public void render(List<TravelTimesItem> createTopicsList) {
		cellList.render(createTopicsList);
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
		Roles.getHeadingRole().set(heading.getElement());

		// TODO Hide pull down until we can figure out how to get VoiceOver to work with it
		Roles.getButtonRole().setAriaHiddenState(pullArrowHeader.getElement(), true);
	}
}
