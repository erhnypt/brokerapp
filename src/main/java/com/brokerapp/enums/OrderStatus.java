package com.brokerapp.enums;

/**
 * İşlem durumunu belirten enum. PENDING: Bekleyen işlem MATCHED: Eşleşen işlem
 * CANCELED: İptal edilen işlem
 */
public enum OrderStatus {
    PENDING, // Pending transaction
    MATCHED, // Matched transaction
    CANCELED    // Canceled transaction
}
