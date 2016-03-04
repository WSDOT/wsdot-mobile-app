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

package gov.wa.wsdot.mobile.client.activities.ferries.schedules.departures;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.i18n.client.TimeZoneInfo;
import com.google.gwt.i18n.client.constants.TimeZoneConstants;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.input.listbox.MListBox;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowHeader;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel.Pullhandler;
import com.googlecode.mgwt.ui.client.widget.panel.scroll.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.progress.ProgressIndicator;
import com.googlecode.mgwt.ui.client.widget.tabbar.TabPanel;

import gov.wa.wsdot.mobile.client.activities.camera.CameraCell;
import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;
import gov.wa.wsdot.mobile.shared.CameraItem;
import gov.wa.wsdot.mobile.shared.FerriesScheduleTimesItem;

public class FerriesRouteDeparturesViewGwtImpl extends Composite
		implements FerriesRouteDeparturesView {
	
	/**
	 * The UiBinder interface.
	 */
	interface FerriesRouteDeparturesViewGwtImplUiBinder extends
			UiBinder<Widget, FerriesRouteDeparturesViewGwtImpl> {
	}

	/**
	 * The UiBinder used to generate the view.
	 */
	private static FerriesRouteDeparturesViewGwtImplUiBinder uiBinder = GWT
			.create(FerriesRouteDeparturesViewGwtImplUiBinder.class);	

	
	@UiField(provided = true)
	CellList<FerriesScheduleTimesItem> cellList;
	
    @UiField(provided = true)
    CellList<CameraItem> cameraCellList;

    @UiField
    TabPanel tabPanel;
    
    @UiField
    static
    ScrollPanel cameraScrollPanel;
    
	@UiField
	BackImageButton backButton;
    
	@UiField
	FlexSpacer leftFlexSpacer;
	
	@UiField(provided = true)
    PullPanel pullToRefresh;	
	
	@UiField
    HTML title;
	
	@UiField
	ProgressIndicator progressIndicator;
	
	@UiField(provided = true)
	MListBox daysOfWeek;
	
	private Presenter presenter;
	private PullArrowHeader pullArrowHeader;
	private DateTimeFormat dateFormat = DateTimeFormat.getFormat("hh:mm a");
	private DateTimeFormat dayOfWeekFormat = DateTimeFormat.getFormat("EEEE");
	
	private final TimeZoneConstants timeZoneConstants = GWT.create(TimeZoneConstants.class);
    private final TimeZone usPacific = TimeZone.createTimeZone(TimeZoneInfo
            .buildTimeZoneData(timeZoneConstants.americaLosAngeles()));	

	public FerriesRouteDeparturesViewGwtImpl() {
		
        pullToRefresh = new PullPanel();
        pullArrowHeader = new PullArrowHeader();
        pullToRefresh.setHeader(pullArrowHeader);
	    
		daysOfWeek = new MListBox();
		
		handleOnLoad();
		
		cellList = new CellList<FerriesScheduleTimesItem>(
				new FerriesRouteDeparturesCell<FerriesScheduleTimesItem>() {
			
			@Override
			public String getDeparting(FerriesScheduleTimesItem model) {
                Date departingTime = new Date(Long.parseLong(model
                        .getDepartingTime()));
		    
				return dateFormat.format(departingTime, usPacific);					
			}

			@Override
			public String getArriving(FerriesScheduleTimesItem model) {
				if (!model.getArrivingTime().equals("N/A")) {
                    Date arrivingTime = new Date(Long.parseLong(model
                            .getArrivingTime()));
				    
					return dateFormat.format(arrivingTime, usPacific);
				} else {
					return "";
				}
			}

			@Override
			public SafeHtml getAnnotation(FerriesScheduleTimesItem model) {
				if (model.getAnnotations() != null) {
					return SafeHtmlUtils.fromTrustedString(model.getAnnotations());
				} else {
					return SafeHtmlUtils.fromString("");
				}
			}

			@Override
			public boolean canBeSelected(FerriesScheduleTimesItem model) {
				return false;
			}

            @Override
            public String getDriveUpSpaces(FerriesScheduleTimesItem model) {
                return String.valueOf(model.getDriveUpSpaceCount());
            }

            @Override
            public String getMaxSpaceCount(FerriesScheduleTimesItem model) {
                return String.valueOf(model.getMaxSpaceCount());
            }

            @Override
            public String getLastUpdated(FerriesScheduleTimesItem model) {
                if (model.getLastUpdated() != null) {
                    return ParserUtils.relativeTime(model.getLastUpdated(),
                            "MMMM d, yyyy h:mm a", false);
                } else {
                    return "";
                }
            }
			
		});
		
        cameraCellList = new CellList<CameraItem>(new CameraCell<CameraItem>() {

            @Override
            public String getUrl(CameraItem model) {
                return model.getImageUrl();
            }
            
            @Override
            public boolean canBeSelected(CameraItem model) {
                return true;
            }
        });
		
		initWidget(uiBinder.createAndBindUi(this));

        if (MGWT.getOsDetection().isAndroid()) {
            leftFlexSpacer.setVisible(false);
            cameraScrollPanel.setBounce(false);
        }
	}

    /**
     * ScrollPanel doesn't allow scrolling to the bottom if it contains a CellList with images.
     * 
     * See: https://code.google.com/p/mgwt/issues/detail?id=276
     * 
     * ScrollPanel.refresh() must be explicitly called after the images are loaded.
     * Since the onload event of images is not bubbling up, the LoadHandler can't be attached
     * to the CellList. Instead, the onload event needs to be captured at the <img>, and directly
     * trigger the ScrollPanel.refresh() from there.
     */
    private native void handleOnLoad() /*-{
        $wnd.refreshPanel = @gov.wa.wsdot.mobile.client.activities.ferries.schedules.departures.FerriesRouteDeparturesViewGwtImpl::refreshPanel();
    }-*/;
	
    public static void refreshPanel() {
        cameraScrollPanel.refresh();
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
	
	@UiHandler("daysOfWeek")
	protected void onChange(ChangeEvent event) {
		if (presenter != null) {
			MListBox source = (MListBox) event.getSource();
			presenter.onDayOfWeekSelected(source.getSelectedIndex());
		}
	}

    @UiHandler("cameraCellList")
    protected void onCameraCellSelected(CellSelectedEvent event) {
        if (presenter != null) {
            int index = event.getIndex();
            presenter.onCameraSelected(index);
        }
    }

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
    @Override
    public void setTitle(String title) {
        this.title.setHTML(title);
    }
	
	@Override
	public void render(List<FerriesScheduleTimesItem> departureTimesList) {
		cellList.render(departureTimesList);
	}
	
    @Override
    public void renderCameras(List<CameraItem> cameraList) {
        cameraCellList.render(cameraList);
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
    public void refreshCameras() {
        cameraScrollPanel.refresh();
    }

	@Override
	public int getDayOfWeekSelected() {
		return daysOfWeek.getSelectedIndex();
	}

	@Override
	public void setDayOfWeekSelected(int index) {
		daysOfWeek.setSelectedIndex(index);
	}
	
	@Override
	public void renderDaysOfWeek(List<String> days) {
		daysOfWeek.clear();
		
		for (String day: days) {
            daysOfWeek.addItem(dayOfWeekFormat.format(new Date(Long
                    .parseLong(day))));
		}
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
    public void setCameraSelected(int lastIndex, boolean b) {
        cameraCellList.setSelectedIndex(lastIndex, b);
    }

    @Override
    public void removeTab(int tabIndex) {
        this.tabPanel.tabBar.remove(tabIndex);
        this.tabPanel.tabContainer.container.remove(tabIndex);
        this.tabPanel.tabContainer.refresh();
    }

    @Override
    public int getTabCount() {
        return this.tabPanel.tabContainer.container.getWidgetCount();
    }

}
