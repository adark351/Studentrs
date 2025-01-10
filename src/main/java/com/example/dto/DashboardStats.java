package com.example.dto;

public class DashboardStats {
    private long totalRooms;
    private long availableRooms;
    private long pendingRequestsCount;
    private long overduePaymentsCount;

    // Constructor, getters, and setters
    public DashboardStats(long totalRooms, long availableRooms, long pendingRequestsCount, long overduePaymentsCount) {
        this.totalRooms = totalRooms;
        this.availableRooms = availableRooms;
        this.pendingRequestsCount = pendingRequestsCount;
        this.overduePaymentsCount = overduePaymentsCount;
    }

    public long getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(long totalRooms) {
        this.totalRooms = totalRooms;
    }

    public long getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(long availableRooms) {
        this.availableRooms = availableRooms;
    }

    public long getPendingRequestsCount() {
        return pendingRequestsCount;
    }

    public void setPendingRequestsCount(long pendingRequestsCount) {
        this.pendingRequestsCount = pendingRequestsCount;
    }

    public long getOverduePaymentsCount() {
        return overduePaymentsCount;
    }

    public void setOverduePaymentsCount(long overduePaymentsCount) {
        this.overduePaymentsCount = overduePaymentsCount;
    }
}