/*
 * Copyright (c) 2014 Washington State Department of Transportation
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

package gov.wa.wsdot.mobile.client.activities.trafficmap.trafficincidents;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.button.Button;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FixedSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowHeader;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel.Pullhandler;
import com.googlecode.mgwt.ui.client.widget.progress.ProgressIndicator;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.SimpleListItem;
import gov.wa.wsdot.mobile.shared.HighwayAlertItem;

import java.util.List;

public class TrafficAlertsViewGwtImpl extends Composite implements
		TrafficAlertsView {

	/**
	 * The UiBinder interface.
	 */	
	interface TrafficAlertsViewGwtImplUiBinder extends
			UiBinder<Widget, TrafficAlertsViewGwtImpl> {
	}

	/**
	 * The UiBinder used to generate the view.
	 */
	private static TrafficAlertsViewGwtImplUiBinder uiBinder = GWT
			.create(TrafficAlertsViewGwtImplUiBinder.class);

	@UiField
	HeaderTitle heading;

	@UiField
	Button doneButton;
	
	@UiField
	FixedSpacer leftFixedSpacer;
	
	@UiField
	FlexSpacer leftFlexSpacer;
	
	@UiField
	HTML amberAlertsHeader;
	
	@UiField(provided = true)
	CellList<HighwayAlertItem> amberAlertsCellList;
	
	@UiField(provided = true)
	CellList<HighwayAlertItem> blockingCellList;

	@UiField(provided = true)
	CellList<HighwayAlertItem> constructionCellList;

	@UiField(provided = true)
	CellList<HighwayAlertItem> closureCellList;
	
	@UiField(provided = true)
	CellList<HighwayAlertItem> specialCellList;
	
	@UiField(provided = true)
	PullPanel pullToRefresh;
	
	@UiField
	FlowPanel flowPanel;
	
	@UiField
	ProgressIndicator progressIndicator;
	
	private Presenter presenter;
	private PullArrowHeader pullArrowHeader;
	
	public TrafficAlertsViewGwtImpl() {
		
		pullToRefresh = new PullPanel();
		pullArrowHeader = new PullArrowHeader();
		pullToRefresh.setHeader(pullArrowHeader);
		
		amberAlertsCellList = new CellList<HighwayAlertItem>(new SimpleListItem<HighwayAlertItem>() {

            @Override
            public String getDisplayDescription(HighwayAlertItem model) {
                return model.getHeadlineDescription();
            }

            @Override
            public String getDisplayLastUpdated(HighwayAlertItem model) {
                if (model.getLastUpdatedTime() == null) {
                    return "";
                } else {
                    return ParserUtils.relativeTime(model.getLastUpdatedTime(),
                            "MMMM d, yyyy h:mm a", false);
                }
            }
        });
		
		blockingCellList = new CellList<HighwayAlertItem>(new SimpleListItem<HighwayAlertItem>() {

			@Override
			public String getDisplayDescription(HighwayAlertItem model) {
				return model.getHeadlineDescription();
			}

			@Override
			public String getDisplayLastUpdated(HighwayAlertItem model) {
			    if (model.getLastUpdatedTime() == null) {
			        return "";
			    } else {
    			    return ParserUtils.relativeTime(model.getLastUpdatedTime(),
                            "MMMM d, yyyy h:mm a", false);
			    }
			}

		});

		constructionCellList = new CellList<HighwayAlertItem>(new SimpleListItem<HighwayAlertItem>() {

			@Override
			public String getDisplayDescription(HighwayAlertItem model) {
				return model.getHeadlineDescription();
			}

			@Override
			public String getDisplayLastUpdated(HighwayAlertItem model) {
			    if (model.getLastUpdatedTime() == null) {
			        return "";
			    } else {
    			    return ParserUtils.relativeTime(model.getLastUpdatedTime(),
                            "MMMM d, yyyy h:mm a", false);
			    }
			}

		});
		
		closureCellList = new CellList<HighwayAlertItem>(new SimpleListItem<HighwayAlertItem>() {

			@Override
			public String getDisplayDescription(HighwayAlertItem model) {
				return model.getHeadlineDescription();
			}

			@Override
			public String getDisplayLastUpdated(HighwayAlertItem model) {
				if (model.getLastUpdatedTime() == null) {
				    return "";
				} else {
    			    return ParserUtils.relativeTime(model.getLastUpdatedTime(),
                            "MMMM d, yyyy h:mm a", false);
				}
			}

		});
		
		specialCellList = new CellList<HighwayAlertItem>(new SimpleListItem<HighwayAlertItem>() {

			@Override
			public String getDisplayDescription(HighwayAlertItem model) {
				return model.getHeadlineDescription();
			}

			@Override
			public String getDisplayLastUpdated(HighwayAlertItem model) {
				if (model.getLastUpdatedTime() == null) {
				    return "";
				} else {
    			    return ParserUtils.relativeTime(model.getLastUpdatedTime(),
                            "MMMM d, yyyy h:mm a", false);
				}
			}

		});
		
		initWidget(uiBinder.createAndBindUi(this));

		accessibilityPrepare();

		if (MGWT.getOsDetection().isAndroid()) {
            leftFixedSpacer.setWidth("12px");
            leftFlexSpacer.setVisible(false);
        }
	}

	
	@UiHandler("blockingCellList")
	protected void onBlockingPressed(CellSelectedEvent event) {
		if (presenter != null) {
			int index = event.getIndex();
			presenter.onItemSelected(Consts.BLOCKING, index);
		}
	}
	@UiHandler("constructionCellList")
	protected void onconstructionPressed(CellSelectedEvent event) {
		if (presenter != null) {
			int index = event.getIndex();
			presenter.onItemSelected(Consts.CONSTRUCTION, index);
		}
	}
	@UiHandler("closureCellList")
	protected void onClosurePressed(CellSelectedEvent event) {
		if (presenter != null) {
			int index = event.getIndex();
			presenter.onItemSelected(Consts.CLOSURES, index);
		}
	}
	@UiHandler("specialCellList")
	protected void onSpecialPressed(CellSelectedEvent event) {
		if (presenter != null) {
			int index = event.getIndex();
			presenter.onItemSelected(Consts.SPECIAL, index);
		}
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
    public void renderAmberAlerts(List<HighwayAlertItem> amberAlertsList) {
        amberAlertsCellList.render(amberAlertsList);
    }
	
	@Override
	public void renderBlocking(List<HighwayAlertItem> blockingList) {
		blockingCellList.render(blockingList);
	}

	@Override
	public void renderConstruction(List<HighwayAlertItem> constructionList) {
		constructionCellList.render(constructionList);
	}

	@Override
	public void renderClosure(List<HighwayAlertItem> closureList) {
		closureCellList.render(closureList);
	}
	
	@Override
	public void renderSpecial(List<HighwayAlertItem> specialList) {
		specialCellList.render(specialList);
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

	private void accessibilityPrepare(){
		// Add ARIA roles for accessibility
		Roles.getHeadingRole().set(heading.getElement());

		Roles.getProgressbarRole().set(progressIndicator.getElement());
		Roles.getProgressbarRole().setAriaLabelProperty(progressIndicator.getElement(), "loading indicator");
		
		// TODO Hide pull down until we can figure out how to get VoiceOver to work with it
		Roles.getButtonRole().setAriaHiddenState(pullArrowHeader.getElement(), true);
	}

}