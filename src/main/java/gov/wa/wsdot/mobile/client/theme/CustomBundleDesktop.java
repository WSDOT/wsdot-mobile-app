/*
 * Copyright 2011 Daniel Kurka
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package gov.wa.wsdot.mobile.client.theme;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.googlecode.mgwt.ui.client.theme.MGWTClientBundle;
import com.googlecode.mgwt.ui.client.theme.base.ButtonBarButtonCss;
import com.googlecode.mgwt.ui.client.theme.base.ButtonBarCss;
import com.googlecode.mgwt.ui.client.theme.base.ButtonCss;
import com.googlecode.mgwt.ui.client.theme.base.CarouselCss;
import com.googlecode.mgwt.ui.client.theme.base.CheckBoxCss;
import com.googlecode.mgwt.ui.client.theme.base.DialogCss;
import com.googlecode.mgwt.ui.client.theme.base.GroupingList;
import com.googlecode.mgwt.ui.client.theme.base.HeaderCss;
import com.googlecode.mgwt.ui.client.theme.base.InputCss;
import com.googlecode.mgwt.ui.client.theme.base.LayoutCss;
import com.googlecode.mgwt.ui.client.theme.base.ListCss;
import com.googlecode.mgwt.ui.client.theme.base.MSearchBoxCss;
import com.googlecode.mgwt.ui.client.theme.base.MainCss;
import com.googlecode.mgwt.ui.client.theme.base.PanelCss;
import com.googlecode.mgwt.ui.client.theme.base.ProgressBarCss;
import com.googlecode.mgwt.ui.client.theme.base.ProgressIndicatorCss;
import com.googlecode.mgwt.ui.client.theme.base.PullToRefreshCss;
import com.googlecode.mgwt.ui.client.theme.base.ScrollPanelCss;
import com.googlecode.mgwt.ui.client.theme.base.SliderCss;
import com.googlecode.mgwt.ui.client.theme.base.TabBarCss;
import com.googlecode.mgwt.ui.client.theme.base.UtilCss;

public interface CustomBundleDesktop extends ClientBundle, MGWTClientBundle {

	@Source({ "com/googlecode/mgwt/ui/client/theme/base/css/groupinglist.css", "com/googlecode/mgwt/ui/client/theme/base/css/ipad/groupinglist.css", "gov/wa/wsdot/mobile/client/css/groupinglist.css" })
	GroupingList getGroupingList();

	@Source({ "com/googlecode/mgwt/ui/client/theme/base/css/progressbar.css", "com/googlecode/mgwt/ui/client/theme/base/css/ipad/progressbar.css", "gov/wa/wsdot/mobile/client/css/progressbar.css" })
	ProgressBarCss getProgressBarCss();

	// This is a very nasty workaround because GWT CssResource does not support
	// @media correctly!
	@Source("com/googlecode/mgwt/ui/client/theme/base/css/util_fake.css")
	UtilCss getUtilCss();

	// This is a very nasty workaround because GWT CssResource does not support
	// @media correctly!
	@Source("com/googlecode/mgwt/ui/client/theme/base/css/util.css")
	TextResource utilTextResource();

	@Source({ "com/googlecode/mgwt/ui/client/theme/base/css/progressindicator.css", "com/googlecode/mgwt/ui/client/theme/base/css/ipad/progressindicator.css", "gov/wa/wsdot/mobile/client/css/progressindicator.css" })
	ProgressIndicatorCss getProgressIndicatorCss();

	@Source({ "com/googlecode/mgwt/ui/client/theme/base/css/header.css", "com/googlecode/mgwt/ui/client/theme/base/css/ipad/header.css" })
	HeaderCss getHeaderCss();

	@Source({ "com/googlecode/mgwt/ui/client/theme/base/css/slider.css", "com/googlecode/mgwt/ui/client/theme/base/css/ipad/slider.css", "gov/wa/wsdot/mobile/client/css/slider.css" })
	SliderCss getSliderCss();

	@Source({ "com/googlecode/mgwt/ui/client/theme/base/css/carousel.css", "com/googlecode/mgwt/ui/client/theme/base/css/ipad/carousel.css", "gov/wa/wsdot/mobile/client/css/carousel.css" })
	CarouselCss getCarouselCss();

	@Source({ "com/googlecode/mgwt/ui/client/theme/base/css/list.css", "com/googlecode/mgwt/ui/client/theme/base/css/ipad/list.css", "gov/wa/wsdot/mobile/client/css/list.css" })
	ListCss getListCss();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/list/arrow.png")
	DataResource listArrow();

	@Source({ "com/googlecode/mgwt/ui/client/theme/base/css/searchbox.css", "com/googlecode/mgwt/ui/client/theme/base/css/ipad/searchbox.css", "gov/wa/wsdot/mobile/client/css/searchbox.css" })
	MSearchBoxCss getSearchBoxCss();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/search/glass.png")
	DataResource searchSearchImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/search/search_clear.png")
	DataResource searchClearImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/search/search_clear_touched.png")
	DataResource searchClearTouchedImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/css/checkbox.css")
	CheckBoxCss getCheckBoxCss();

	@Source({ "com/googlecode/mgwt/ui/client/theme/base/css/buttons.css", "com/googlecode/mgwt/ui/client/theme/base/css/ipad/buttons.css", "gov/wa/wsdot/mobile/client/css/buttons.css" })
	ButtonCss getButtonCss();

	@Source({ "com/googlecode/mgwt/ui/client/theme/base/css/scrollpanel.css", "com/googlecode/mgwt/ui/client/theme/base/css/ipad/scrollpanel.css", "gov/wa/wsdot/mobile/client/css/scrollpanel.css" })
	ScrollPanelCss getScrollPanelCss();

	@Source({ "com/googlecode/mgwt/ui/client/theme/base/css/buttonbar.css", "com/googlecode/mgwt/ui/client/theme/base/css/ipad/buttonbar.css", "gov/wa/wsdot/mobile/client/css/buttonbar.css" })
	ButtonBarCss getButtonBarCss();

	@Source({ "com/googlecode/mgwt/ui/client/theme/base/css/dialog.css", "com/googlecode/mgwt/ui/client/theme/base/css/ipad/dialog.css", "gov/wa/wsdot/mobile/client/css/dialog.css" })
	DialogCss getDialogCss();

	@Source({ "com/googlecode/mgwt/ui/client/theme/base/css/main.css", "com/googlecode/mgwt/ui/client/theme/base/css/ipad/main.css", "gov/wa/wsdot/mobile/client/css/main.css" })
	MainCss getMainCss();

	@Source({ "com/googlecode/mgwt/ui/client/theme/base/css/input.css", "com/googlecode/mgwt/ui/client/theme/base/css/ipad/input.css", "gov/wa/wsdot/mobile/client/css/input.css" })
	InputCss getInputCss();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/input/ios_check.png")
	DataResource inputCheckImage();

	@Source({ "com/googlecode/mgwt/ui/client/theme/base/css/panel.css", "gov/wa/wsdot/mobile/client/css/panel.css" })
	PanelCss getPanelCss();

	@Source({ "com/googlecode/mgwt/ui/client/theme/base/css/layout.css", "gov/wa/wsdot/mobile/client/css/layout.css" })
	LayoutCss getLayoutCss();

	@Source({ "com/googlecode/mgwt/ui/client/theme/base/css/pulltorefresh.css", "com/googlecode/mgwt/ui/client/theme/base/css/ipad/pulltorefresh.css", "gov/wa/wsdot/mobile/client/css/pulltorefresh.css" })
	PullToRefreshCss getPullToRefreshCss();

	@Source({ "com/googlecode/mgwt/ui/client/theme/base/css/tabbar.css", "com/googlecode/mgwt/ui/client/theme/base/css/ipad/tabbar.css", "gov/wa/wsdot/mobile/client/css/tabbar.css" })
	TabBarCss getTabBarCss();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/tabbar/bookmarks.png")
	ImageResource tabBarBookMarkImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/tabbar/contacts.png")
	ImageResource tabBarContactsImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/tabbar/downloads.png")
	ImageResource tabBarDownloadsImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/tabbar/favorites.png")
	ImageResource tabBarFavoritesImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/tabbar/featured.png")
	ImageResource tabBarFeaturedImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/tabbar/history.png")
	ImageResource tabBarHistoryImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/tabbar/more.png")
	ImageResource tabBarMoreImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/tabbar/mostrecent.png")
	ImageResource tabBarMostRecentImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/tabbar/mostviewed.png")
	ImageResource tabBarMostViewedImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/tabbar/search.png")
	ImageResource tabBarSearchImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/spinner.png")
	DataResource spinnerImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/spinner_white.png")
	DataResource spinnerWhiteImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/error.png")
	DataResource errorImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/input/check_android_checked.png")
	DataResource android_check_checked();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/input/check_android_not_checked.png")
	DataResource android_check_not_checked();

	@Source("com/googlecode/mgwt/ui/client/theme/base/css/buttonbarbutton.css")
	public ButtonBarButtonCss getButtonBarButtonCss();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/pressed.png")
	public ImageResource getButtonBarHighlightImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/action.png")
	public ImageResource getButtonBarActionImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/arrowdown.png")
	public ImageResource getButtonBarArrowDownImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/arrowleft.png")
	public ImageResource getButtonBarArrowLeftImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/arrowright.png")
	public ImageResource getButtonBarArrowRightImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/arrowup.png")
	public ImageResource getButtonBarArrowUpImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/bookmarks.png")
	public ImageResource getButtonBarBookmarkImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/camera.png")
	public ImageResource getButtonBarCameraImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/compose.png")
	public ImageResource getButtonBarComposeImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/fastforward.png")
	public ImageResource getButtonBarFastForwardImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/info.png")
	public ImageResource getButtonBarInfoImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/locate.png")
	public ImageResource getButtonBarLocateImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/minus.png")
	public ImageResource getButtonBarMinusImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/new.png")
	public ImageResource getButtonBarNewImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/nextslide.png")
	public ImageResource getButtonBarNextSlideImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/organize.png")
	public ImageResource getButtonBarOrganizeImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/pause.png")
	public ImageResource getButtonBarPauseImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/play.png")
	public ImageResource getButtonBarPlayImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/plus.png")
	public ImageResource getButtonBarPlusImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/previousslide.png")
	public ImageResource getButtonBarPreviousSlideImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/refresh.png")
	public ImageResource getButtonBarRefreshImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/reply.png")
	public ImageResource getButtonBarReplyImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/rewind.png")
	public ImageResource getButtonBarRewindImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/search.png")
	public ImageResource getButtonBarSearchImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/stop.png")
	public ImageResource getButtonBarStopImage();

	@Source("com/googlecode/mgwt/ui/client/theme/base/resources/toolbar/trash.png")
	public ImageResource getButtonBarTrashImage();

}
