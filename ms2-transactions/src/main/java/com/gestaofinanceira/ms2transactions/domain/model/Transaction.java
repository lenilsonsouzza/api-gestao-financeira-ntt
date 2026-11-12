package com.gestaofinanceira.ms2transactions.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private Long id;
    private Long userId;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String description ;
    private String category;
    private Long destinationUserId;
    private TransactionStatus status;
    private String rejectionReason;

    public Transaction(){
    }

    public Transaction(Long userId, TransactionType type, BigDecimal amount, String description, String category, Long destinationUserId) {
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.destinationUserId = destinationUserId;
        this.timestamp = java.time.LocalDateTime.now(); // Atribui a data atual automaticamente
    }

    public Transaction(Long id, Long userId, TransactionType type, BigDecimal amount, LocalDateTime timestamp, String description, String category, Long destinationUserId, TransactionStatus status, String rejectionReason) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.description = description;
        this.category = category;
        this.destinationUserId = destinationUserId;
        this.status = status;
        this.rejectionReason = rejectionReason;
    }

    public void approve(){
        this.status=TransactionStatus.APPROVED;
        this.rejectionReason = null;
    }

    public void reject(String reason) {
        this.status = TransactionStatus.REJECTED;
        this.rejectionReason = reason;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public TransactionType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public Long getDestinationUserId() { return destinationUserId; }
    public TransactionStatus getStatus() { return status; }
    public String getRejectionReason() { return rejectionReason; }

}


