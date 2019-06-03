package com.core.ui.restaurantListActivity;

/**
 * This interface is used to get the click events on the views.
 *
 * @author Akhil Aravind
 */
public interface RecyclerViewClickInterface {

    /**
     * This method will handle the click on the parent view
     */
    public void recyclerviewOnClick(int position, String resId);

}