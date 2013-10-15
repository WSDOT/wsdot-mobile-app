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

package gov.wa.wsdot.mobile.client.activities.trafficmap.seattleincidents;

import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.SimpleListItem;
import gov.wa.wsdot.mobile.shared.SeattleIncidentItem;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.widget.CellList;
import com.googlecode.mgwt.ui.client.widget.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.ProgressBar;
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.base.PullArrowHeader;
import com.googlecode.mgwt.ui.client.widget.base.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.base.PullPanel;
import com.googlecode.mgwt.ui.client.widget.base.PullPanel.Pullhandler;

public class SeattleTrafficAlertsViewGwtImpl extends Composite implements
		SeattleTrafficAlertsView {

	/**
	 * The UiBinder interface.
	 */	
	interface SeattleTrafficAlertsViewGwtImplUiBinder extends
			UiBinder<Widget, SeattleTrafficAlertsViewGwtImpl> {
	}

	/**
	 * The UiBinder used to generate the view.
	 */
	private static SeattleTrafficAlertsViewGwtImplUiBinder uiBinder = GWT
			.create(SeattleTrafficAlertsViewGwtImplUiBinder.class);
	

	@UiField
	HeaderButton doneButton;
	
	@UiField
	HTML amberAlertsHeader;
	
	@UiField(provided = true)
	CellList<SeattleIncidentItem> amberAlertsCellList;
	
	@UiField(provided = true)
	CellList<SeattleIncidentItem> blockingCellList;

	@UiField(provided = true)
	CellList<SeattleIncidentItem> constructionCellList;

	@UiField(provided = true)
	CellList<SeattleIncidentItem> specialCellList;
	
	@UiField(provided = true)
	PullPanel pullToRefresh;
	
	@UiField
	FlowPanel flowPanel;
	
	@UiField
	ProgressBar progressBar;
	
	private Presenter presenter;
	private PullArrowHeader pullArrowHeader;
	
	public SeattleTrafficAlertsViewGwtImpl() {
		
		pullToRefresh = new PullPanel();
		pullArrowHeader = new PullArrowHeader();
		pullToRefresh.setHeader(pullArrowHeader);
		
		amberAlertsCellList = new CellList<SeattleIncidentItem>(new SimpleListItem<SeattleIncidentItem>() {

            @Override
            public String getDisplayDescription(SeattleIncidentItem model) {
                return model.getDescription();
            }

            @Override
            public String getDisplayLastUpdated(SeattleIncidentItem model) {
                if (model.getLastUpdatedTime() == null) {
                    return "";
                } else {
                    return ParserUtils.relativeTime(model.getLastUpdatedTime(),
                            "MMMM d, yyyy h:mm a", false);
                }
            }
        });

		amberAlertsCellList.setGroup(false);
		amberAlertsCellList.setRound(false);
		
		blockingCellList = new CellList<SeattleIncidentItem>(new SimpleListItem<SeattleIncidentItem>() {

			@Override
			public String getDisplayDescription(SeattleIncidentItem model) {
				return model.getDescription();
			}

			@Override
			public String getDisplayLastUpdated(SeattleIncidentItem model) {
			    if (model.getLastUpdatedTime() == null) {
			        return "";
			    } else {
    			    return ParserUtils.relativeTime(model.getLastUpdatedTime(),
                            "MMMM d, yyyy h:mm a", false);
			    }
			}

		});
		
		blockingCellList.setGroup(false);
		blockingCellList.setRound(false);

		constructionCellList = new CellList<SeattleIncidentItem>(new SimpleListItem<SeattleIncidentItem>() {

			@Override
			public String getDisplayDescription(SeattleIncidentItem model) {
				return model.getDescription();
			}

			@Override
			public String getDisplayLastUpdated(SeattleIncidentItem model) {
			    if (model.getLastUpdatedTime() == null) {
			        return "";
			    } else {
    			    return ParserUtils.relativeTime(model.getLastUpdatedTime(),
                            "MMMM d, yyyy h:mm a", false);
			    }
			}

		});
		
		constructionCellList.setGroup(false);
		constructionCellList.setRound(false);
		
		specialCellList = new CellList<SeattleIncidentItem>(new SimpleListItem<SeattleIncidentItem>() {

			@Override
			public String getDisplayDescription(SeattleIncidentItem model) {
				return model.getDescription();
			}

			@Override
			public String getDisplayLastUpdated(SeattleIncidentItem model) {
				if (model.getLastUpdatedTime() == null) {
				    return "";
				} else {
    			    return ParserUtils.relativeTime(model.getLastUpdatedTime(),
                            "MMMM d, yyyy h:mm a", false);
				}
			}

		});
		
		specialCellList.setGroup(false);
		specialCellList.setRound(false);
		
		initWidget(uiBinder.createAndBindUi(this));

	}

	@UiHandler("doneButton")
	protected void onDoneButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onDoneButtonPressed();
		}
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

    @Override
    public void renderAmberAlerts(List<SeattleIncidentItem> amberAlertsList) {
        amberAlertsCellList.render(amberAlertsList);
    }
	
	@Override
	public void renderBlocking(List<SeattleIncidentItem> blockingList) {
		blockingCellList.render(blockingList);
	}

	@Override
	public void renderConstruction(List<SeattleIncidentItem> constructionList) {
		constructionCellList.render(constructionList);
	}

	@Override
	public void renderSpecial(List<SeattleIncidentItem> specialList) {
		specialCellList.render(specialList);
	}
	
	@Override
	public void showProgressBar() {
		progressBar.setVisible(false);
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

    @Override
    public void showAmberAlerts() {
        amberAlertsHeader.setVisible(true);
        amberAlertsCellList.setVisible(true);
    }

    @Override
    public void hideAmberAlerts() {
        amberAlertsHeader.setVisible(false);
        amberAlertsCellList.setVisible(false);
    }

}