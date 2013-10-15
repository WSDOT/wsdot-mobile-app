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

package gov.wa.wsdot.mobile.client.activities.camera;

import gov.wa.wsdot.mobile.client.css.AppBundle;
import gov.wa.wsdot.mobile.shared.CameraItem;

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
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.base.PullArrowHeader;
import com.googlecode.mgwt.ui.client.widget.base.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.base.PullPanel;
import com.googlecode.mgwt.ui.client.widget.base.PullPanel.Pullhandler;

public class CameraViewGwtImpl extends Composite implements CameraView {

	/**
	 * The UiBinder interface.
	 */	
	interface CameraViewGwtImplUiBinder extends
			UiBinder<Widget, CameraViewGwtImpl> {
	}
	
	/**
	 * The UiBinder used to generate the view.
	 */
	private static CameraViewGwtImplUiBinder uiBinder = GWT
			.create(CameraViewGwtImplUiBinder.class);

	@UiField
	HeaderButton backButton;
	
	@UiField
	HTML title;
	
	@UiField
	Button starButton;
	
	@UiField(provided = true)
	PullPanel pullToRefresh;

	private PullArrowHeader pullArrowHeader;
	
	@UiField(provided = true)
	CellList<CameraItem> cameraCellList;
	
	private Presenter presenter;
	
	public CameraViewGwtImpl() {

		pullToRefresh = new PullPanel();
		pullArrowHeader = new PullArrowHeader();
		pullToRefresh.setHeader(pullArrowHeader);
		
		cameraCellList = new CellList<CameraItem>(new CameraCell<CameraItem>() {

			@Override
			public String getUrl(CameraItem model) {
				return model.getImageUrl();
			}

			@Override
			public boolean canBeSelected(CameraItem model) {
				return false;
			}
		});
		
		cameraCellList.setGroup(false);
		cameraCellList.setRound(false);
		
		initWidget(uiBinder.createAndBindUi(this));
		
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
	public void renderCamera(List<CameraItem> createCameraList) {
		cameraCellList.render(createCameraList);		
	}
	
	@Override
	public void setTitle(String title) {
		this.title.setText(title);
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
	public void toggleStarButton(boolean isStarred) {
		if (isStarred) {
			starButton.setStyleName(AppBundle.INSTANCE.css().starButtonOn());
		} else {
			starButton.setStyleName(AppBundle.INSTANCE.css().starButtonOff());
		}
	}

}
