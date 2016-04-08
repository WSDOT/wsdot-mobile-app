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

import com.google.gwt.aria.client.CheckedValue;
import com.google.gwt.aria.client.Roles;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.button.image.NotimportantImageButton;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.image.ImageHolder;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.scroll.ScrollPanel;

public class TravelTimeDetailsViewGwtImpl extends Composite implements
		TravelTimeDetailsView {

	/**
	 * The UiBinder interface.
	 */	
	interface TravelTimeDetailsViewGwtImplUiBinder extends
			UiBinder<Widget, TravelTimeDetailsViewGwtImpl> {
	}

	/**
	 * The UiBinder used to generate the view.
	 */
	private static TravelTimeDetailsViewGwtImplUiBinder uiBinder = GWT
			.create(TravelTimeDetailsViewGwtImplUiBinder.class);

	@UiField
	HeaderTitle heading;
	
	@UiField
	BackImageButton backButton;
	
    @UiField(provided = true)
    NotimportantImageButton starButton;
	
    @UiField
    FlexSpacer leftFlexSpacer;
    
    @UiField
    ScrollPanel scrollPanel;
    
	@UiField(provided = true)
	CellList<TravelTimesItem> travelTimeCellList;
	
	private Presenter presenter;
	
	public TravelTimeDetailsViewGwtImpl() {
	    
	    starButton = new NotimportantImageButton();
		
		travelTimeCellList = new CellList<TravelTimesItem>(new TravelTimesCell<TravelTimesItem>() {

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

			@Override
			public boolean canBeSelected(TravelTimesItem model) {
				return false;
			}
			
		});

		initWidget(uiBinder.createAndBindUi(this));
        
		accessibilityPrepare();
		
		if (MGWT.getOsDetection().isAndroid()) {
            leftFlexSpacer.setVisible(false);
            scrollPanel.setBounce(false);
        }
	}

	@UiHandler("backButton")
	protected void onBackButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onBackButtonPressed();
		}
	}
	
	@UiHandler("starButton")
	protected void onStarButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onStarButtonPressed();
		}
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void renderTravelTime(List<TravelTimesItem> createTravelTimeList) {
		travelTimeCellList.render(createTravelTimeList);
	}

	@Override
	public void toggleStarButton(boolean isStarred) {
        if (isStarred) {
            starButton.setIcon(ImageHolder.get().important());
            Roles.getCheckboxRole().setAriaCheckedState(starButton.getElement(), CheckedValue.TRUE);
        } else {
            starButton.setIcon(ImageHolder.get().notImportant());
            Roles.getCheckboxRole().setAriaCheckedState(starButton.getElement(), CheckedValue.FALSE);
        }		
	}
private void accessibilityPrepare(){
		
		// Add ARIA roles for accessibility
		Roles.getButtonRole().set(backButton.getElement());
		Roles.getButtonRole().setAriaLabelProperty(backButton.getElement(), "back");
		
		Roles.getCheckboxRole().set(starButton.getElement());
		Roles.getCheckboxRole().setAriaLabelProperty(starButton.getElement(), "favorite");
		
		Roles.getHeadingRole().set(heading.getElement());
	}
}