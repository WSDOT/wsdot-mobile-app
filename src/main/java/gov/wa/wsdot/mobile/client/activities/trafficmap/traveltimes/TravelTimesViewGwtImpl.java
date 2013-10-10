/*
 * Copyright (c) 2013 Washington State Department of Transportation
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

package gov.wa.wsdot.mobile.client.activities.trafficmap.traveltimes;

import gov.wa.wsdot.mobile.client.css.AppBundle;
import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.shared.TravelTimesItem;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.widget.CellList;
import com.googlecode.mgwt.ui.client.widget.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.MSearchBox;
import com.googlecode.mgwt.ui.client.widget.ProgressBar;
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.base.PullArrowHeader;
import com.googlecode.mgwt.ui.client.widget.base.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.base.PullPanel;
import com.googlecode.mgwt.ui.client.widget.base.PullPanel.Pullhandler;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedEvent;

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
	
	@UiField(provided = true)
	CellList<TravelTimesItem> cellList;
	
	@UiField
	HeaderButton doneButton;
	
	@UiField(provided = true)
	PullPanel pullToRefresh;
	
	@UiField
	FlowPanel flowPanel;
	
	@UiField(provided = true)
	MSearchBox searchBox;
	
	@UiField
	ProgressBar progressBar;
	
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
		
		cellList.setRound(true);
		
		searchBox = new MSearchBox();
		searchBox.setPlaceHolder("Search Travel Times");
		searchBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (presenter != null) {
					presenter.onSearchTextChanged("%" + searchBox.getText() + "%");
				}
			}

		});
		
		searchBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (presenter != null) {
					presenter.onSearchTextChanged("%" + searchBox.getText() + "%");
				}
				
			}
		});
		
		initWidget(uiBinder.createAndBindUi(this));
		
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@UiHandler("doneButton")
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
	public void showProgressBar() {
		progressBar.setVisible(true);
	}

	@Override
	public void hideProgressBar() {
		progressBar.setVisible(false);
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
		pullToRefresh.setHeaderPullhandler(pullHandler);
	}

	@Override
	public PullArrowWidget getPullHeader() {
		return pullArrowHeader;
	}

	@Override
	public HasRefresh getPullPanel() {
		return pullToRefresh;
	}

}
