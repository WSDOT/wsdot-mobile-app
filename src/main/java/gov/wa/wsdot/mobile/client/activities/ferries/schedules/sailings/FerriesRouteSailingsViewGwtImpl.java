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

package gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings;

import gov.wa.wsdot.mobile.client.css.AppBundle;
import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.TitleLastUpdatedCell;
import gov.wa.wsdot.mobile.shared.FerriesRouteAlertItem;
import gov.wa.wsdot.mobile.shared.FerriesTerminalItem;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.widget.Button;
import com.googlecode.mgwt.ui.client.widget.CellList;
import com.googlecode.mgwt.ui.client.widget.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.ProgressBar;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.celllist.BasicCell;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedEvent;

public class FerriesRouteSailingsViewGwtImpl extends Composite
		implements FerriesRouteSailingsView {
	
	/**
	 * The UiBinder interface.
	 */
	interface FerriesRouteSailingsViewGwtImplUiBinder extends
			UiBinder<Widget, FerriesRouteSailingsViewGwtImpl> {
	}

	/**
	 * The UiBinder used to generate the view.
	 */
	private static FerriesRouteSailingsViewGwtImplUiBinder uiBinder = GWT
			.create(FerriesRouteSailingsViewGwtImplUiBinder.class);	

	
	@UiField
	ScrollPanel sailingsPanel;
	
	@UiField(provided = true)
	CellList<FerriesTerminalItem> sailingsCellList;
	
	@UiField(provided = true)
	CellList<FerriesRouteAlertItem> alertsCellList;
	
	@UiField
	HeaderButton backButton;
	
	@UiField
	HTML title;
	
	@UiField
	Button starButton;
	
	@UiField
	ProgressBar progressBar;
	
	private Presenter presenter;
	
	public FerriesRouteSailingsViewGwtImpl() {
		
		sailingsCellList = new CellList<FerriesTerminalItem>(
				new BasicCell<FerriesTerminalItem>() {
			
			@Override
			public String getDisplayString(FerriesTerminalItem model) {
				return model.getDepartingTerminalName() + " to " + model.getArrivingTerminalName();
			}

			@Override
			public boolean canBeSelected(FerriesTerminalItem model) {
				return true;
			}
		});
		
		sailingsCellList.setRound(false);
		
		alertsCellList = new CellList<FerriesRouteAlertItem>(
				new TitleLastUpdatedCell<FerriesRouteAlertItem>() {

			@Override
			public String getDisplayDescription(FerriesRouteAlertItem model) {
				return model.getAlertFullTitle();
			}

			@Override
			public String getDisplayLastUpdated(FerriesRouteAlertItem model) {
					return ParserUtils.relativeTime(model.getPublishDate(),
							"MMMM d, yyyy h:mm a", false);
			}
			
			@Override
			public boolean canBeSelected(FerriesRouteAlertItem model) {
				return true;
			}
			
		});
		
		alertsCellList.setRound(false);
		
		initWidget(uiBinder.createAndBindUi(this));
		
	}

	@UiHandler("sailingsCellList")
	protected void onSailingCellSelected(CellSelectedEvent event) {
		if (presenter != null) {
			int index = event.getIndex();
			presenter.onSailingItemSelected(index);
		}
	}
	
	@UiHandler("alertsCellList")
	protected void onRouteAlertCellSelected(CellSelectedEvent event) {
		if (presenter != null) {
			int index = event.getIndex();
			presenter.onRouteAlertSelected(index);
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
	public void setTitle(String title) {
		this.title.setText(title);
	}

	@Override
	public void render(List<FerriesTerminalItem> createTopicsList) {
		sailingsCellList.render(createTopicsList);
	}

	@Override
	public void renderRouteAlerts(
			List<FerriesRouteAlertItem> createRouteAlertsList) {
		alertsCellList.render(createRouteAlertsList);
		
	}
	
	@Override
	public void setSelected(int lastIndex, boolean b) {
		sailingsCellList.setSelectedIndex(lastIndex, b);
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
		sailingsPanel.refresh();
	}

	@Override
	public void toggleStarButton(boolean isStarred) {
		if (isStarred) {
			starButton.setStyleName(AppBundle.INSTANCE.css().starButtonOn());
		} else {
			starButton.setStyleName(AppBundle.INSTANCE.css().starButtonOff());
		}
	}

}
