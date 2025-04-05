package com.brokerapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Kaynak bulunamadığında fırlatılan özel istisna sınıfı. HTTP 404 (Not Found)
 * durum kodu ile eşleştirilmiştir.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Varsayılan mesajla istisna oluşturur.
     */
    public ResourceNotFoundException() {
        super("Kaynak bulunamadı.");
    }

    /**
     * Özel mesajla istisna oluşturur.
     *
     * @param message hata mesajı
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Hata mesajı ve nedenle istisna oluşturur.
     *
     * @param message hata mesajı
     * @param cause istisnanın nedeni
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
