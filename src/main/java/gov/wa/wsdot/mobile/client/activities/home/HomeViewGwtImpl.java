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

package gov.wa.wsdot.mobile.client.activities.home;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.aria.client.Id;
import com.google.gwt.aria.client.Roles;
import com.google.gwt.aria.client.SelectedValue;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.button.Button;
import com.googlecode.mgwt.ui.client.widget.button.image.AboutImageButton;
import com.googlecode.mgwt.ui.client.widget.carousel.Carousel;
import com.googlecode.mgwt.ui.client.widget.dialog.Dialogs;
import com.googlecode.mgwt.ui.client.widget.dialog.Dialogs.ButtonType;
import com.googlecode.mgwt.ui.client.widget.dialog.Dialogs.OptionsDialogEntry;
import com.googlecode.mgwt.ui.client.widget.dialog.Dialogs.OptionCallback;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.list.widgetlist.WidgetList;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FixedSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.flex.RootFlexPanel;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowHeader;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel.Pullhandler;
import com.googlecode.mgwt.ui.client.widget.panel.scroll.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.progress.ProgressIndicator;
import com.googlecode.mgwt.ui.client.widget.tabbar.TabPanel;

import gov.wa.wsdot.mobile.client.activities.ferries.schedules.FerriesRouteSchedulesCell;
import gov.wa.wsdot.mobile.client.activities.trafficmap.menu.traveltimes.TravelTimesCell;
import gov.wa.wsdot.mobile.client.css.AppBundle;
import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.CellDetailsWithIcon;
import gov.wa.wsdot.mobile.client.widget.celllist.MyBasicCell;
import gov.wa.wsdot.mobile.client.widget.tabbar.FavoritesTabBarButton;
import gov.wa.wsdot.mobile.client.widget.tabbar.HomeTabBarButton;
import gov.wa.wsdot.mobile.shared.*;

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
	HeaderTitle heading;
	
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
	HTMLPanel trafficTitle;
	
	@UiField
	HTMLPanel ferriesTitle;
	
	@UiField
	HTMLPanel passesTitle;
	
	@UiField
	HTMLPanel socialTitle;
	
	@UiField
	HTMLPanel tollingTitle;
	
	@UiField
	HTMLPanel borderTitle;
	
	@UiField
	HTMLPanel amtrakTitle;
	
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
	HTML locationsHeader;

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
	Image star;
	
	@UiField
	HTML colorOfStar;

	@UiField(provided = true)
    WidgetList locationsWidgetList;

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

	@UiField
	HomeTabBarButton homeTab;
	
	@UiField
	FavoritesTabBarButton favoritesTab;
	
	@UiField
	RootFlexPanel home;
	
	@UiField
	RootFlexPanel favorites;


	private Presenter presenter;
	private PullArrowHeader pullArrowHeader;

	public HomeViewGwtImpl() {
	    pullToRefresh = new PullPanel();
		pullArrowHeader = new PullArrowHeader();
		pullToRefresh.setHeader(pullArrowHeader);
		
		alertsCarousel = new Carousel();

        locationsWidgetList = new WidgetList();


		camerasCellList = new CellList<CameraItem>(new MyBasicCell<CameraItem>() {

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

            @Override
            public String getCrossingTime(FerriesRouteItem model) {
                try {
                    if (model.getCrossingTime().equalsIgnoreCase("null")) {
                        return "";
                    } else {
                        return "Crossing Time: ~ " + model.getCrossingTime() + " min";
                    }
                } catch (Exception e) {
                    return "";
                }
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

		tabPanel.tabContainer.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				if (presenter != null) {
					int index = event.getSelectedItem();
					presenter.onTabSelected(index);
				}
			}
		});

		accessibilityPrepare();	
		
        if (MGWT.getOsDetection().isAndroid()) {
            leftFixedSpacer.setWidth("12px");
            leftFlexSpacer.setVisible(false);
            scrollPanel.setBounce(false);
            colorOfStar.setHTML("icon to turn it white.");
        }
	}
	
    @UiHandler("homeTab")
    protected void onHomeTabPressed(TapEvent event) {
	    accessibilityShowHome();
    }
    
    @UiHandler("favoritesTab")
    protected void onFavoritesTabPressed(TapEvent event) {
    	accessibilityShowFavorites();
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
	public TabPanel getTabPanel() { return this.tabPanel; }

	@Override
	public void renderLocations(List<LocationItem> createLocationList) {
        locationsWidgetList.clear();
        int i = 0;

        for (LocationItem item : createLocationList) {

			FlowPanel cellPanel = new FlowPanel();
			cellPanel.setStyleName(AppBundle.INSTANCE.css().cellLocation());

            HTML html = new HTML(item.getTitle());

			html.setWordWrap(true);
			html.setStyleName(AppBundle.INSTANCE.css().cellLocationTitle());

            final int index = i;
            cellPanel.sinkEvents(Event.ONCLICK);
            cellPanel.addHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    // handle the click event
                    if (presenter != null){
                        presenter.onLocationSelected(index);
                    }
                }
            }, ClickEvent.getType());

            Button b = new Button();
            b.setStyleName(AppBundle.INSTANCE.css().gearIcon());
            b.addTapHandler(new TapHandler() {
				@Override
				public void onTap(TapEvent event) {
					List<OptionsDialogEntry> list = new ArrayList<>();
                    list.add(new OptionsDialogEntry("Delete Location", ButtonType.IMPORTANT));
					list.add(new OptionsDialogEntry("Edit Name", ButtonType.NORMAL));
					list.add(new OptionsDialogEntry("Cancel", ButtonType.NORMAL));
					Dialogs.options(list, new OptionCallback() {
						@Override
						public void onOptionSelected(int position) {
							switch(position){
								case 1:
									presenter.onLocationRemove(index);
									break;
								case 2:
                                    presenter.onLocationEdit(index);
									break;
								default:
									break;
							}
						}
					});
				}
            });

			cellPanel.add(html);
			cellPanel.add(b);

            locationsWidgetList.add(cellPanel);
            i++;
        }
	}

	@Override
	public void showLocationsHeader(){
		locationsHeader.setVisible(true);
	}

	@Override
	public void hideLocationsHeader(){
		locationsHeader.setVisible(false);
	}

	@Override
	public void showLocationsList(){
		locationsWidgetList.setVisible(true);
	}

	@Override
	public void hideLocationsList(){
		locationsWidgetList.setVisible(false);
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

	private void accessibilityShowHome(){
		Roles.getMainRole().setAriaHiddenState(home.getElement(), false);
		Roles.getMainRole().setAriaHiddenState(favorites.getElement(), true);
		Roles.getTabRole().setAriaSelectedState(homeTab.getElement(), SelectedValue.TRUE);
		Roles.getTabRole().setAriaSelectedState(favoritesTab.getElement(), SelectedValue.FALSE);
	}

	private void accessibilityShowFavorites(){
		Roles.getMainRole().setAriaHiddenState(home.getElement(), true);
		Roles.getMainRole().setAriaHiddenState(favorites.getElement(), false);
		Roles.getTabRole().setAriaSelectedState(homeTab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaSelectedState(favoritesTab.getElement(), SelectedValue.TRUE);
	}
	
	private void accessibilityPrepare(){
		
		// Set UI labels for accessibility
		trafficButton.setTitle("traffic Map");
		ferriesButton.setTitle("ferries");
		passesButton.setTitle("mountain passes");
		socialButton.setTitle("social media");
		tollingButton.setTitle("toll rates");
		borderButton.setTitle("border waits");
		amtrakButton.setTitle("amtrak cascades");
		aboutButton.setTitle("about the app");
		alertsCarousel.setTitle("high impact alerts");
		star.setAltText("star");
		
		// Set ARIA roles for accessibility
		Roles.getButtonRole().set(trafficButton.getElement());
		Roles.getButtonRole().set(ferriesButton.getElement());
		Roles.getButtonRole().set(passesButton.getElement());
		Roles.getButtonRole().set(socialButton.getElement());
		Roles.getButtonRole().set(tollingButton.getElement());
		Roles.getButtonRole().set(borderButton.getElement());
		Roles.getButtonRole().set(amtrakButton.getElement());
		
		Roles.getButtonRole().set(aboutButton.getElement());

		Roles.getHeadingRole().set(highImpactAlertsPanel.getElement());

        Roles.getHeadingRole().set(heading.getElement());

		Roles.getTabRole().set(homeTab.getElement());
		Roles.getTabRole().setAriaSelectedState(homeTab.getElement(), SelectedValue.TRUE);
		Roles.getTabRole().setAriaLabelProperty(homeTab.getElement(), "home");
		
		Roles.getTabRole().set(favoritesTab.getElement());
		Roles.getTabRole().setAriaSelectedState(favoritesTab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaLabelProperty(favoritesTab.getElement(), "favorites");
		
		Roles.getProgressbarRole().set(progressIndicator.getElement());
		Roles.getProgressbarRole().setAriaLabelProperty(progressIndicator.getElement(), "loading indicator");

		// Define flow
		Roles.getHeadingRole().setAriaFlowtoProperty(heading.getElement(), Id.of(trafficButton.getElement()));

		// Hide redundant content from VoiceOver
		Roles.getHeadingRole().setAriaHiddenState(trafficTitle.getElement(), true);
		Roles.getHeadingRole().setAriaHiddenState(ferriesTitle.getElement(), true);
		Roles.getHeadingRole().setAriaHiddenState(passesTitle.getElement(), true);
		Roles.getHeadingRole().setAriaHiddenState(socialTitle.getElement(), true);
		Roles.getHeadingRole().setAriaHiddenState(tollingTitle.getElement(), true);
		Roles.getHeadingRole().setAriaHiddenState(borderTitle.getElement(), true);
		Roles.getHeadingRole().setAriaHiddenState(amtrakTitle.getElement(), true);

        // TODO Hide pull down until we can figure out how to get VoiceOver to work with it
        Roles.getButtonRole().setAriaHiddenState(pullArrowHeader.getElement(), true);

		accessibilityShowHome();
	}
}