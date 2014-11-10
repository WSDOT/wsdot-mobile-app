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

package gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules.details;

import gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules.AmtrakCascadesSchedulesCell;
import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.shared.AmtrakCascadesScheduleItem;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.googlecode.mgwt.ui.client.widget.button.image.PreviousitemImageButton;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;

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

	
	@UiField(provided = true)
	CellList<AmtrakCascadesScheduleItem> cellList;
	
	@UiField
	PreviousitemImageButton backButton;
    
	@UiField
    HTML title;
	
	private Presenter presenter;
	private DateTimeFormat dateFormat = DateTimeFormat.getFormat("hh:mm a");
	private DateTimeFormat updateDateFormat = DateTimeFormat.getFormat("MMMM d, yyyy h:mm a");
		
	private final TimeZoneConstants timeZoneConstants = GWT.create(TimeZoneConstants.class);
    private final TimeZone usPacific = TimeZone.createTimeZone(TimeZoneInfo
            .buildTimeZoneData(timeZoneConstants.americaLosAngeles()));	
    private String fromLocation = "VAC";
    private String toLocation = "TAC";
	
	public AmtrakCascadesSchedulesDetailsViewGwtImpl() {

	    cellList = new CellList<AmtrakCascadesScheduleItem>(
				new AmtrakCascadesSchedulesCell<AmtrakCascadesScheduleItem>() {
			
			@Override
			public SafeHtml getScheduledDeparture(AmtrakCascadesScheduleItem model) {
                String schedDepartureTime = model.getScheduledDepartureTime();

                if (schedDepartureTime != null) {
                    Date scheduledDepartureTime = new Date(Long
                            .parseLong(model.getScheduledDepartureTime()));
                    
                    return SafeHtmlUtils.fromString(dateFormat.format(
                            scheduledDepartureTime, usPacific));
                
                } else {
                    return SafeHtmlUtils.fromString("");
                }			        
			}

			@Override
			public SafeHtml getScheduledArrival(AmtrakCascadesScheduleItem model) {
                String schedDepartureTime = model.getScheduledDepartureTime();
                String schedArrivalTime = model.getScheduledArrivalTime();
                
                if ((fromLocation.equalsIgnoreCase(toLocation))
                        && (schedDepartureTime != null && schedArrivalTime != null)) {

                    return SafeHtmlUtils.fromString("");
                } else {
                    if (schedArrivalTime != null) {
                        Date scheduledArrivalTime = new Date(Long
                                .parseLong(model.getScheduledArrivalTime()));                   
                        
                        return SafeHtmlUtils.fromString(dateFormat.format(
                                scheduledArrivalTime, usPacific));
    
                    } else if (schedDepartureTime != null && !toLocation.equalsIgnoreCase(fromLocation)) {
                        /* When a station only has a scheduled departure time and not arrival,
                         * it is assumed that the train only stops for a short time at that station.
                         * In these cases, the Amtrak site appears to show this scheduled departure
                         * time as the scheduled arrival time so we'll do the same thing for the app.
                         */ 
                        Date scheduledArrivalTime = new Date(Long
                                .parseLong(model.getScheduledDepartureTime()));
    
                        return SafeHtmlUtils.fromString(dateFormat.format(
                                scheduledArrivalTime, usPacific));
    
                    } else {
                        return SafeHtmlUtils.fromString("");
                    }                 
                }
			}

            @Override
            public SafeHtml getLastUpdated(AmtrakCascadesScheduleItem model) {
                if (model.getUpdateTime() != null) {
                    String updateTime = updateDateFormat
                            .format(new Date(Long.parseLong(model.getUpdateTime())));
                    
                    return SafeHtmlUtils.fromString(ParserUtils
                            .relativeTime(updateTime,
                                    "MMMM d, yyyy h:mm a", false));
                } else {
                    return SafeHtmlUtils.fromString("");
                }
            }

            @Override
            public SafeHtml getTrain(AmtrakCascadesScheduleItem model) {
                if (model.getTrainMessage() == null) {
                    return SafeHtmlUtils.fromString(model.getTrainName());
                } else {
                    return SafeHtmlUtils.fromString(model.getTrainName()
                            + " - "
                            + model.getTrainMessage());
                }
            }

            @Override
            public SafeHtml getDepartureComment(AmtrakCascadesScheduleItem model) {
                if (model.getDepartureTime() != null) {
                    Date departureTime = new Date(Long.parseLong(model.getDepartureTime()));
                    Date scheduledDepartureTime = new Date(Long.parseLong(model.getScheduledDepartureTime()));
                    int minutesDiff = (int) (((departureTime.getTime() - scheduledDepartureTime.getTime()) / 1000) / 60);
                    String scheduleType = model.getDepartureScheduleType();
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
            public SafeHtml getArrivalComment(AmtrakCascadesScheduleItem model) {
                String schedDepartureTime = model.getScheduledDepartureTime();
                String schedArrivalTime = model.getScheduledArrivalTime();
                
                if ((fromLocation.equalsIgnoreCase(toLocation))
                        && (schedDepartureTime != null && schedArrivalTime != null)) {
                    
                    return SafeHtmlUtils.fromString("");
                } else {
                    if (model.getArrivalTime() != null) {
                        Date arrivalTime = new Date(Long.parseLong(model.getArrivalTime()));
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
                                        Long.parseLong(model.getScheduledDepartureTime()));
                            } else {
                                return SafeHtmlUtils.fromString("");
                            }
                        }
                        
                        int minutesDiff = (int) (((arrivalTime.getTime() - scheduledArrivalTime.getTime()) / 1000) / 60);
                        String scheduleType = model.getArrivalScheduleType();
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
            public boolean canBeSelected(AmtrakCascadesScheduleItem model) {
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
    public void setTitle(String title) {
        this.title.setHTML(title);
    }

    @Override
    public void render(List<Map<String, AmtrakCascadesScheduleItem>> item) {
        cellList.render((List<AmtrakCascadesScheduleItem>) item.get(0));
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub
        
    }

}
