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

package gov.wa.wsdot.mobile.client.activities.home;

import gov.wa.wsdot.mobile.client.activities.ferries.schedules.FerriesRouteSchedulesCell;
import gov.wa.wsdot.mobile.client.activities.trafficmap.traveltimes.TravelTimesCell;
import gov.wa.wsdot.mobile.client.css.AppBundle;
import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.CellDetailsWithIcon;
import gov.wa.wsdot.mobile.client.widget.celllist.BasicCell;
import gov.wa.wsdot.mobile.shared.CameraItem;
import gov.wa.wsdot.mobile.shared.FerriesRouteItem;
import gov.wa.wsdot.mobile.shared.HighwayAlertItem;
import gov.wa.wsdot.mobile.shared.MountainPassItem;
import gov.wa.wsdot.mobile.shared.TravelTimesItem;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.button.Button;
import com.googlecode.mgwt.ui.client.widget.button.image.AboutImageButton;
import com.googlecode.mgwt.ui.client.widget.carousel.Carousel;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FixedSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowHeader;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel.Pullhandler;
import com.googlecode.mgwt.ui.client.widget.panel.scroll.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.progress.ProgressIndicator;
import com.googlecode.mgwt.ui.client.widget.tabbar.TabPanel;

public class HomeViewGwtImpl extends Composite implements HomeView {

	/**
	 * The UiBinder interface.
	 */	
	interface HomeViewGwtImplUiBinder extends UiBinder<Widget, HomeViewGwtImpl> {
	}
	
	/**
	 * The UiBinder used to generate the view.
	 */
	private static HomeViewGwtImplUiBinder uiBinder = GWT
			.create(HomeViewGwtImplUiBinder.class);
	
	@UiField
	AboutImageButton aboutButton;
	
	@UiField
	Button trafficButton;
	
	@UiField
	Button ferriesButton;
	
	@UiField
	Button passesButton;
	
	@UiField
	Button socialButton;
	
	@UiField
	Button tollingButton;
	
	@UiField
	Button borderButton;
	
	@UiField
	Button amtrakButton;
	
	@UiField
	HTMLPanel highImpactAlertsPanel;
	
	@UiField
	ProgressIndicator progressIndicator;
	
	@UiField(provided = true)
	PullPanel pullToRefresh;
	
	@UiField
	ScrollPanel scrollPanel;
	
	@UiField
	FixedSpacer leftFixedSpacer;
	
	@UiField
	FlexSpacer leftFlexSpacer;
	
	@UiField
	HTML camerasHeader;
	
	@UiField
	HTML ferriesHeader;
	
	@UiField
	HTML mountainPassesHeader;
	
	@UiField
	HTML travelTimesHeader;
	
	@UiField
	HTMLPanel emptyFavorites;
	
	@UiField
	HTML colorOfStar;
	
	@UiField(provided = true)
	CellList<CameraItem> camerasCellList;
	
	@UiField(provided = true)
	CellList<FerriesRouteItem> ferriesCellList;
	
	@UiField(provided = true)
	CellList<MountainPassItem> mountainPassesCellList;
	
	@UiField(provided = true)
	CellList<TravelTimesItem> travelTimesCellList;
	
	@UiField(provided = true)
	Carousel alertsCarousel;
	
	@UiField
	TabPanel tabPanel;
	
	private Presenter presenter;
	private PullArrowHeader pullArrowHeader;

	public HomeViewGwtImpl() {
		
	    pullToRefresh = new PullPanel();
		pullArrowHeader = new PullArrowHeader();
		pullToRefresh.setHeader(pullArrowHeader);

		alertsCarousel = new Carousel();
		
		camerasCellList = new CellList<CameraItem>(new BasicCell<CameraItem>() {

			@Override
			public String getDisplayString(CameraItem model) {
				return model.getTitle();
			}

            @Override
            public boolean canBeSelected(CameraItem model) {
                return true;
            }

		});
		
		ferriesCellList = new CellList<FerriesRouteItem>(
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


		});

		mountainPassesCellList = new CellList<MountainPassItem>(
				new CellDetailsWithIcon<MountainPassItem>() {

			@Override
			public String getDisplayString(MountainPassItem model) {
				return model.getMountainPassName();
			}

			@Override
			public String getDisplayImage(MountainPassItem model) {
				return model.getWeatherIcon();
			}
			
			@Override
			public String getDisplayDescription(MountainPassItem model) {
				return model.getWeatherCondition();
			}
			
			@Override
			public String getDisplayLastUpdated(MountainPassItem model) {
				return ParserUtils.relativeTime(model.getDateUpdated(),
						"MMMM d, yyyy h:mm a", false);
			}
			
			@Override
			public boolean canBeSelected(MountainPassItem model) {
				return true;
			}

		});
		
		travelTimesCellList = new CellList<TravelTimesItem>(
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
				return ParserUtils.relativeTime(model.getUpdated(),
						"yyyy-MM-dd h:mm a", false);
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
		
		initWidget(uiBinder.createAndBindUi(this));
		
        if (MGWT.getOsDetection().isAndroid()) {
            leftFixedSpacer.setWidth("12px");
            leftFlexSpacer.setVisible(false);
            scrollPanel.setBounce(false);
            colorOfStar.setHTML("icon to turn it white.");
        }
	}

	@UiHandler("aboutButton")
	protected void onAboutButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onAboutButtonPressed();
		}
	}

	@UiHandler("trafficButton")
	protected void onTrafficButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onTrafficMapButtonPressed();
		}
	}

	@UiHandler("ferriesButton")
	protected void onFerriesButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onFerriesButtonPressed();
		}
	}	
	
	@UiHandler("passesButton")
	protected void onMountainPassesButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onMountainPassesButtonPressed();
		}
	}
	
	@UiHandler("socialButton")
	protected void onSocialMediaButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onSocialMediaButtonPressed();
		}
	}
	
	@UiHandler("tollingButton")
	protected void onTollRatesButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onTollRatesButtonPressed();
		}
	}
	
	@UiHandler("borderButton")
	protected void onBorderWaitButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onBorderWaitButtonPressed();
		}
	}

    @UiHandler("amtrakButton")
    protected void onAmtrakButtonPressed(TapEvent event) {
        if (presenter != null) {
            presenter.onAmtrakButtonPressed();
        }
    }
	
	@UiHandler("camerasCellList")
	protected void onCameraCellSelected(CellSelectedEvent event) {
		if (presenter != null) {
			int index = event.getIndex();
			presenter.onCameraSelected(index);
		}
	}
	
	@UiHandler("ferriesCellList")
	protected void onFerriesCellSelected(CellSelectedEvent event) {
		if (presenter != null) {
			int index = event.getIndex();
			presenter.onFerriesSelected(index);
		}
	}
	
	@UiHandler("mountainPassesCellList")
	protected void onMountainPassCellSelected(CellSelectedEvent event) {
		if (presenter != null) {
			int index = event.getIndex();
			presenter.onMountainPassSelected(index);
		}
	}
	
	@UiHandler("travelTimesCellList")
	protected void onTravelTimeCellSelected(CellSelectedEvent event) {
		if (presenter != null) {
			int index = event.getIndex();
			presenter.onTravelTimeSelected(index);
		}
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void render(List<HighwayAlertItem> alerts) {
        int length = alerts.size();
        
        if (!alerts.isEmpty()) {
            for (final HighwayAlertItem alert: alerts) {
                HTMLPanel alertPanel = new HTMLPanel("");
                String text = alert.getHeadlineDescription();
                String shortenedAlert = ParserUtils.ellipsis(text, 128);
                HTML html = new HTML("<div><p>" + shortenedAlert + "</p></div><div></div>");
                
                if (length == 1) {
                    alertsCarousel.setShowCarouselIndicator(false);
                    if (alert.getAlertId() == -1) {
                        html.addStyleName(AppBundle.INSTANCE.css().noHighImpactAlerts());
                    } else {
                        html.addStyleName(AppBundle.INSTANCE.css().highImpactAlert());
                        
                        if (alert.getAlertId() != -1) {
                            html.addClickHandler(new ClickHandler() {
    
                                @Override
                                public void onClick(ClickEvent event) {
                                    presenter.onHighImpactAlertSelected(alert.getAlertId());
                                }
                            });
                        }
                    }
                    
                    alertPanel.add(html);
                    alertsCarousel.add(alertPanel);
                } else {
                    alertsCarousel.setShowCarouselIndicator(true);
                    html.addClickHandler(new ClickHandler() {
    
                        @Override
                        public void onClick(ClickEvent event) {
                            presenter.onHighImpactAlertSelected(alert.getAlertId());
                        }
                    });
                    
                    html.addStyleName(AppBundle.INSTANCE.css().highImpactAlert());
                    alertPanel.add(html);
                    alertsCarousel.add(alertPanel);
                }
            }
            
            alertsCarousel.setHeight("75px");
            alertsCarousel.refresh();
        }
	}
	
	@Override
	public void refresh() {
        tabPanel.tabContainer.refresh();
	    scrollPanel.refresh();
		pullToRefresh.refresh();
	}
	
	@Override
	public void clear() {
		alertsCarousel.clear();
	    alertsCarousel.refresh();
	    alertsCarousel.setHeight("0px");
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
	public void renderCameras(List<CameraItem> createCameraList) {
		camerasCellList.render(createCameraList);	
	}

	@Override
	public void showCamerasHeader() {
		camerasHeader.setVisible(true);
	}

	@Override
	public void hideCamerasHeader() {
		camerasHeader.setVisible(false);
	}

	@Override
	public void showCamerasList() {
		camerasCellList.setVisible(true);
	}

	@Override
	public void hideCamerasList() {
		camerasCellList.setVisible(false);
	}

	@Override
	public void renderFerries(List<FerriesRouteItem> createFerriesList) {
		ferriesCellList.render(createFerriesList);
	}

	@Override
	public void showFerriesHeader() {
		ferriesHeader.setVisible(true);
	}

	@Override
	public void hideFerriesHeader() {
		ferriesHeader.setVisible(false);
	}

	@Override
	public void showFerriesList() {
		ferriesCellList.setVisible(true);
	}

	@Override
	public void hideFerriesList() {
		ferriesCellList.setVisible(false);
	}

	@Override
	public void renderMountainPasses(List<MountainPassItem> createMountainPassList) {
		mountainPassesCellList.render(createMountainPassList);
	}

	@Override
	public void showMountainPassesHeader() {
		mountainPassesHeader.setVisible(true);
	}

	@Override
	public void hideMountainPassesHeader() {
		mountainPassesHeader.setVisible(false);
	}

	@Override
	public void showMountainPassesList() {
		mountainPassesCellList.setVisible(true);
	}

	@Override
	public void hideMountainPassesList() {
		mountainPassesCellList.setVisible(false);
	}

	@Override
	public void renderTravelTimes(List<TravelTimesItem> createTravelTimesList) {
		travelTimesCellList.render(createTravelTimesList);
	}

	@Override
	public void showTravelTimesHeader() {
		travelTimesHeader.setVisible(true);
	}

	@Override
	public void hideTravelTimesHeader() {
		travelTimesHeader.setVisible(false);
	}

	@Override
	public void showTravelTimesList() {
		travelTimesCellList.setVisible(true);
	}

	@Override
	public void hideTravelTimesList() {
		travelTimesCellList.setVisible(false);
	}

	@Override
	public void showEmptyFavoritesMessage() {
		emptyFavorites.setVisible(true);
	}

	@Override
	public void hideEmptyFavoritesMessage() {
		emptyFavorites.setVisible(false);
	}

}