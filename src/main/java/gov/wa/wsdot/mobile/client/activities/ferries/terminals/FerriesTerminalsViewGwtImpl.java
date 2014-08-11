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

package gov.wa.wsdot.mobile.client.activities.ferries.terminals;

import gov.wa.wsdot.mobile.client.widget.celllist.BasicCell;
import gov.wa.wsdot.mobile.shared.Topic;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.header.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellSelectedEvent;

public class FerriesTerminalsViewGwtImpl extends Composite implements
        FerriesTerminalsView {

    /**
     * The UiBinder interface.
     */ 
    interface FerriesTerminalsViewGwtImplUiBinder extends
            UiBinder<Widget, FerriesTerminalsViewGwtImpl> {
    }
    
    /**
     * The UiBinder used to generate the view.
     */
    private static FerriesTerminalsViewGwtImplUiBinder uiBinder = GWT
            .create(FerriesTerminalsViewGwtImplUiBinder.class);
    
    @UiField(provided = true)
    CellList<Topic> cellList;
    
    @UiField
    HeaderButton backButton;
    
    private Presenter presenter;

    public FerriesTerminalsViewGwtImpl() {
        cellList = new CellList<Topic>(new BasicCell<Topic>() {

            @Override
            public String getDisplayString(Topic model) {
                return model.getName();
            }
            
            @Override
            public boolean canBeSelected(Topic model) {
                return true;
            }
        });
        
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
    public void render(List<Topic> createTopicsList) {
        cellList.render(createTopicsList);
    }

    @Override
    public void setSelected(int lastIndex, boolean b) {
        cellList.setSelectedIndex(lastIndex, b);
    }

}
