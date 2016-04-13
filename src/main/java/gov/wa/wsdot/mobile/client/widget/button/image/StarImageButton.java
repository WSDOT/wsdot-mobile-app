package gov.wa.wsdot.mobile.client.widget.button.image;

import com.googlecode.mgwt.ui.client.widget.button.ImageButton;
import gov.wa.wsdot.mobile.client.widget.image.LocalImageHolder;

public class StarImageButton extends ImageButton {

    public StarImageButton() {
        super(LocalImageHolder.get().star());
    }

}