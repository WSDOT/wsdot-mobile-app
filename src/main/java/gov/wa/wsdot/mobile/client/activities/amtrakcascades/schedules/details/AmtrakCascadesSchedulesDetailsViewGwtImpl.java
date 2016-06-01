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

package gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules.details;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.core.client.GWT;
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
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowHeader;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel.Pullhandler;
import com.googlecode.mgwt.ui.client.widget.progress.ProgressIndicator;
import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;
import gov.wa.wsdot.mobile.shared.AmtrakCascadesServiceItem;

import java.util.Date;
import java.util.List;

public class AmtrakCascadesSchedulesDetailsViewGwtImpl extends Composite
		implements AmtrakCascadesSchedulesDetailsView {
	
	/**
	 * The UiBinder interface.
	 */
	interface AmtrakCascadesSchedulesDetailsViewGwtImplUiBinder extends
			UiBinder<Widget, AmtrakCascadesSchedulesDetailsViewGwtImpl> {
	}

	/**
	 * The UiBinder used to generate the view.
	 */
	private static AmtrakCascadesSchedulesDetailsViewGwtImplUiBinder uiBinder = GWT
			.create(AmtrakCascadesSchedulesDetailsViewGwtImplUiBinder.class);	

	@UiField
	HeaderTitle heading;
	
	@UiField(provided = true)
	CellList<AmtrakCascadesServiceItem> cellList;
	
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
	
	private Presenter presenter;
	private PullArrowHeader pullArrowHeader;
	private DateTimeFormat dateFormat = DateTimeFormat.getFormat("hh:mm a");
	private DateTimeFormat updateDateFormat = DateTimeFormat.getFormat("MMMM d, yyyy h:mm a");
		
	private final TimeZoneConstants timeZoneConstants = GWT.create(TimeZoneConstants.class);
    private final TimeZone usPacific = TimeZone.createTimeZone(TimeZoneInfo
            .buildTimeZoneData(timeZoneConstants.americaLosAngeles()));	
    private String fromLocation;
    private String toLocation;
	
	public AmtrakCascadesSchedulesDetailsViewGwtImpl() {
		
        pullToRefresh = new PullPanel();
        pullArrowHeader = new PullArrowHeader();
        pullToRefresh.setHeader(pullArrowHeader);
		
		cellList = new CellList<AmtrakCascadesServiceItem>(
				new AmtrakCascadesSchedulesDetailsCell<AmtrakCascadesServiceItem>() {
			
			@Override
			public SafeHtml getScheduledDeparture(AmtrakCascadesServiceItem model) {
                String schedDepartureTime = model.getLocation().get(0).get(fromLocation).getScheduledDepartureTime();

                if (schedDepartureTime != null) {
                    Date scheduledDepartureTime = new Date(Long
                            .parseLong(model.getLocation().get(0)
                                    .get(fromLocation)
                                    .getScheduledDepartureTime()));
                    
                    return SafeHtmlUtils.fromString(dateFormat.format(
                            scheduledDepartureTime, usPacific));
                
                } else {
                    return SafeHtmlUtils.fromString("");
                }			        
			}

			@Override
			public SafeHtml getScheduledArrival(AmtrakCascadesServiceItem model) {
                String schedDepartureTime = model.getLocation().get(0).get(fromLocation).getScheduledDepartureTime();
                String schedArrivalTime = model.getLocation().get(0).get(toLocation).getScheduledArrivalTime();
                
                if ((fromLocation.equalsIgnoreCase(toLocation))
                        && (schedDepartureTime != null && schedArrivalTime != null)) {

                    return SafeHtmlUtils.fromString("");
                } else {
                    if (schedArrivalTime != null) {
                        Date scheduledArrivalTime = new Date(Long
                                .parseLong(model.getLocation().get(0)
                                        .get(toLocation)
                                        .getScheduledArrivalTime()));                   
                        
                        return SafeHtmlUtils.fromString(dateFormat.format(
                                scheduledArrivalTime, usPacific));
    
                    } else if (schedDepartureTime != null && !toLocation.equalsIgnoreCase(fromLocation)) {
                        /* When a station only has a scheduled departure time and not arrival,
                         * it is assumed that the train only stops for a short time at that station.
                         * In these cases, the Amtrak site appears to show this scheduled departure
                         * time as the scheduled arrival time so we'll do the same thing for the app.
                         */ 
                        Date scheduledArrivalTime = new Date(Long
                                .parseLong(model.getLocation().get(0)
                                        .get(toLocation)
                                        .getScheduledDepartureTime()));
    
                        return SafeHtmlUtils.fromString(dateFormat.format(
                                scheduledArrivalTime, usPacific));
    
                    } else {
                        return SafeHtmlUtils.fromString("");
                    }                 
                }
			}

            @Override
            public SafeHtml getLastUpdated(AmtrakCascadesServiceItem model) {
                if (model.getLocation().get(0).get(fromLocation).getUpdateTime() != null) {
                    String updateTime = updateDateFormat
                            .format(new Date(Long.parseLong(model
                                    .getLocation().get(0)
                                    .get(fromLocation).getUpdateTime())));
                    
                    return SafeHtmlUtils.fromString(ParserUtils
                            .relativeTime(updateTime,
                                    "MMMM d, yyyy h:mm a", false));
                } else {
                    return SafeHtmlUtils.fromString("");
                }
            }

            @Override
            public SafeHtml getTrain(AmtrakCascadesServiceItem model) {
                if (model.getLocation().get(0).get(fromLocation).getTrainMessage() == null) {
                    return SafeHtmlUtils.fromString(model.getLocation()
                            .get(0).get(fromLocation).getTrainName());
                } else {
                    return SafeHtmlUtils.fromString(model.getLocation()
                            .get(0).get(fromLocation).getTrainName()
                            + " - "
                            + model.getLocation().get(0)
                                    .get(fromLocation)
                                    .getTrainMessage());
                }
            }

            @Override
            public SafeHtml getDepartureComment(AmtrakCascadesServiceItem model) {
                if (model.getLocation().get(0).get(fromLocation).getDepartureTime() != null) {
                    Date departureTime = new Date(Long.parseLong(model.getLocation().get(0).get(fromLocation).getDepartureTime()));
                    Date scheduledDepartureTime = new Date(Long.parseLong(model.getLocation().get(0).get(fromLocation).getScheduledDepartureTime()));
                    int minutesDiff = (int) (((departureTime.getTime() - scheduledDepartureTime.getTime()) / 1000) / 60);
                    String scheduleType = model.getLocation().get(0).get(fromLocation).getDepartureScheduleType();
                    String timelyType = "on time";
                    if (minutesDiff < 0) {
                        timelyType = " early ";
                    } else if (minutesDiff > 0) {
                        timelyType = " late ";
                    }
                    
                    if (scheduleType.equalsIgnoreCase("Estimated")) {
                        if (minutesDiff == 0) {
                            return SafeHtmlUtils.fromString("Estimated " + timelyType);
                        } else {
                            return SafeHtmlUtils.fromString("Estimated "
                                    + getHoursMinutes(Math.abs(minutesDiff))
                                    + timelyType
                                    + " at "
                                    + dateFormat.format(departureTime));
                        }
                    } else {
                        if (minutesDiff == 0) {
                            return SafeHtmlUtils.fromString("Departed " + timelyType);
                        } else {
                            return SafeHtmlUtils.fromString("Departed "
                                    + getHoursMinutes(Math.abs(minutesDiff))
                                    + timelyType + " at "
                                    + dateFormat.format(departureTime));
                        } 
                    }
                } else {
                    return SafeHtmlUtils.fromString("");
                }
            }

            @Override
            public SafeHtml getArrivalComment(AmtrakCascadesServiceItem model) {
                String schedDepartureTime = model.getLocation().get(0).get(fromLocation).getScheduledDepartureTime();
                String schedArrivalTime = model.getLocation().get(0).get(toLocation).getScheduledArrivalTime();
                
                if ((fromLocation.equalsIgnoreCase(toLocation))
                        && (schedDepartureTime != null && schedArrivalTime != null)) {
                    
                    return SafeHtmlUtils.fromString("");
                } else {
                    if (model.getLocation().get(0).get(toLocation).getArrivalTime() != null) {
                        Date arrivalTime = new Date(Long.parseLong(model.getLocation().get(0).get(toLocation).getArrivalTime()));
                        Date scheduledArrivalTime;

                        if (schedArrivalTime != null) {
                            scheduledArrivalTime = new Date(Long.parseLong(schedArrivalTime));
                        } else {
                            /* Stop looking for the scheduled departure time if origin and destination
                             * stations are the same
                             */ 
                            if (!toLocation.equalsIgnoreCase(fromLocation)) {
                                /* When a station only has a scheduled departure time and not arrival,
                                 * it is assumed that the train only stops for a short time at that station.
                                 * In these cases, the Amtrak site appears to show this scheduled departure
                                 * time as the scheduled arrival time so we'll do the same thing for the app.
                                 */ 
                                scheduledArrivalTime = new Date(
                                        Long.parseLong(model
                                                .getLocation()
                                                .get(0)
                                                .get(toLocation)
                                                .getScheduledDepartureTime()));
                            } else {
                                return SafeHtmlUtils.fromString("");
                            }
                        }
                        
                        int minutesDiff = (int) (((arrivalTime.getTime() - scheduledArrivalTime.getTime()) / 1000) / 60);
                        String scheduleType = model.getLocation().get(0).get(toLocation).getArrivalScheduleType();
                        String timelyType = "on time";
                        if (minutesDiff < 0) {
                            timelyType = " early ";
                        } else if (minutesDiff > 0) {
                            timelyType = " late ";
                        }
                        
                        if (scheduleType.equalsIgnoreCase("Estimated")) {
                            if (minutesDiff == 0) {
                                return SafeHtmlUtils.fromString("Estimated " + timelyType);
                            } else {
                                return SafeHtmlUtils.fromString("Estimated "
                                        + getHoursMinutes(Math.abs(minutesDiff))
                                        + timelyType
                                        + " at "
                                        + dateFormat.format(arrivalTime));
                            }
                        } else {
                            if (minutesDiff == 0) {
                                return SafeHtmlUtils.fromString("Arrived " + timelyType);
                            } else {
                                return SafeHtmlUtils.fromString("Arrived "
                                        + getHoursMinutes(Math.abs(minutesDiff))
                                        + timelyType
                                        + " at "
                                        + dateFormat.format(arrivalTime));
                            }
                        }
                    } else {
                        return SafeHtmlUtils.fromString("");
                    }
                }
            }
            
            @Override
            public boolean canBeSelected(AmtrakCascadesServiceItem model) {
                /*
                if (model.getLocation().get(0).size() > 2) {
                    return true;
                } else {
                    return false;
                }
                */
                return false;
            }

            /**
             *
             * @param minutesDiff
             * @return
             */
            private String getHoursMinutes(int minutesDiff) {
                int hours = (int) Math.floor(minutesDiff / 60);
                int minutes = (minutesDiff % 60);

                if (hours == 0) {
                    return minutes
                            + ParserUtils.pluralize(minutes,
                                    " minute ", " minutes ");
                } else {
                    if (minutes == 0) {
                        return hours
                                + ParserUtils.pluralize(minutes,
                                        " hour ", " hours ");
                    } else {
                        return hours
                                + ParserUtils.pluralize(hours,
                                        " hour ", " hours ")
                                + minutes
                                + ParserUtils.pluralize(minutes,
                                        " minute ", " minutes ");
                    }
                }
            }
            
		});
		
		initWidget(uiBinder.createAndBindUi(this));

		accessibilityPrepare();
		
        if (MGWT.getOsDetection().isAndroid()) {
            leftFlexSpacer.setVisible(false);
        }

	}

	@UiHandler("backButton")
	protected void onBackButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onBackButtonPressed();
		}
	}

    @UiHandler("cellList")
    protected void onCellSelected(CellSelectedEvent event) {
        if (presenter != null) {
            int index = event.getIndex();
            presenter.onItemSelected(index);
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
	public void render(List<AmtrakCascadesServiceItem> createTopicsList) {
		cellList.render(createTopicsList);
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
    public void setFromToLocation(String fromLocation, String toLocation) {
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
    }

    @Override
    public void setSelected(int lastIndex, boolean b) {
        cellList.setSelectedIndex(lastIndex, b);
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
