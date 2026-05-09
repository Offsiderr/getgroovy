package seng201.team67.gui.instantiable;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import seng201.team67.models.items.Item;

public class TooltipController {


    //THERE IS A BUILT IN CLASS
    @FXML
    private Label tooltipDescription;

    @FXML private Label tooltipTitle;

    public TooltipController(Item item)
    {
        tooltipTitle.setText(item.getName());
        tooltipDescription.setText(item.getDescription());
    }
}
