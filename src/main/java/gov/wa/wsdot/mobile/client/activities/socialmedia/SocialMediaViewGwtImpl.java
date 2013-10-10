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

package gov.wa.wsdot.mobile.client.activities.socialmedia;

import gov.wa.wsdot.mobile.client.widget.CellWithIcon;
import gov.wa.wsdot.mobile.shared.TopicWithImage;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.widget.CellList;
import com.googlecode.mgwt.ui.client.widget.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedEvent;

public class SocialMediaViewGwtImpl extends Composite implements
		SocialMediaView {

	/**
	 * The UiBinder interface.
	 */	
	interface SocialMediaViewGwtImplUiBinder extends
			UiBinder<Widget, SocialMediaViewGwtImpl> {
	}
	
	/**
	 * The UiBinder used to generate the view.
	 */
	private static SocialMediaViewGwtImplUiBinder uiBinder = GWT
			.create(SocialMediaViewGwtImplUiBinder.class);

	@UiField(provided = true)
	CellList<TopicWithImage> cellList;
	
	@UiField
	HeaderButton backButton;
	
	private Presenter presenter;
	
	public SocialMediaViewGwtImpl() {
		
		cellList = new CellList<TopicWithImage>(new CellWithIcon<TopicWithImage>() {

			@Override
			public String getDisplayString(TopicWithImage model) {
				return model.getName();
			}

			@Override
			public String getDisplayImage(TopicWithImage model) {
				return model.getImage();
			}
			
			@Override
			public boolean canBeSelected(TopicWithImage model) {
				return true;
			}

		});
		
		cellList.setRound(true);

		initWidget(uiBinder.createAndBindUi(this));

	}

	@UiHandler("cellList")
	protected void onCellSelected(CellSelectedEvent event) {
		if (presenter != null) {
			int index = event.getIndex();
			presenter.onItemSelected(index);
		}
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
	public void render(List<TopicWithImage> createTopicsList) {
		cellList.render(createTopicsList);
	}

	@Override
	public void setSelected(int lastIndex, boolean b) {
		cellList.setSelectedIndex(lastIndex, b);
	}
	
}
