package gov.wa.wsdot.mobile.client.widget.button.image;

import com.googlecode.mgwt.ui.client.widget.button.ImageButton;
import gov.wa.wsdot.mobile.client.widget.image.LocalImageHolder;

public class Camera2ImageButton extends ImageButton {

    public Camera2ImageButton() {
        super(LocalImageHolder.get().camera2());
    }

}
