package com.quandoo.restaurant.model;

/**
 * Created by 972391 on 7/26/2018.
 */

public class ReservationTable {
    private int tableId;
    private boolean isReserved;
    private String tableReserverName;

    public String getTableReserverName() {
        return tableReserverName;
    }

    public void setTableReserverName(String tableReserverName) {
        this.tableReserverName = tableReserverName;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }
}
