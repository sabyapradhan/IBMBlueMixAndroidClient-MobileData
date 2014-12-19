package shastra.com.ibmbluemixandroidclient;

import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMDataObjectSpecialization;

/**
 * Created by sachi on 12/17/2014.
 */
@IBMDataObjectSpecialization("MessageText")
public class MessageText extends IBMDataObject {
    public static final String CLASS_NAME = "MessageText";
    private static final String NAME = "text";

    /**
     * Gets the name of the Item.
     * @return String itemName
     */
    public String getName() {
        return (String) getObject(NAME);
    }

    /**
     * Sets the name of a list item, as well as calls setCreationTime().
     * @param itemName
     */
    public void setName(String itemName) {
        setObject(NAME, (itemName != null) ? itemName : "");
    }

    /**
     * When calling toString() for an item, we'd really only want the name.
     * @return String theItemName
     */
    public String toString() {
        String theItemName = "";
        theItemName = getName();
        return theItemName;
    }
}
